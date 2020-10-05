/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.Set;
import com.digiroots.training.graph.OrigamiSurface;
import quickhull3d.gdx.EarClippingTriangulator;
import quickhull3d.gdx.ShortArray;

/**
 *
 * @author khaddam
 */
public class OrigamiSurface3D {

    protected OrigamiSurface s;    
    Vector3f[] vertices;// = new Vector3f[4];
    Vector2f[] texCoord;// = new Vector2f[4];
    short[] indexes;// = {2, 0, 1, 1, 3, 2};
    Point3D[] vertices3D;
    int pointCount2D = 0;    

    public OrigamiSurface3D(OrigamiSurface s, Point3D[] surfacePoints3D) {
        this.s = s;
        this.pointCount2D = s.points.length;
        //this.transformer = transformer;
        this.vertices3D = surfacePoints3D;
        vertices = new Vector3f[surfacePoints3D.length];
        for (int i=0; i<surfacePoints3D.length; i++){
            vertices[i] = surfacePoints3D[i].getVertex();
        }
        transformTo3DPoints();        
    }

    private void transformTo3DPoints() {
        
        float[] p = new float[vertices.length*2];
        for (int i = 0; i < vertices.length; i++) {
            p[2 * i] = (float) vertices[i].x;
            p[2 * i + 1] = (float) vertices[i].y;
        }
        
        EarClippingTriangulator triang = new EarClippingTriangulator();
        ShortArray res = triang.computeTriangles(p);        
        short[] inx = res.toArray();
        //short[] inxInv = res.toArray();
        
        /*for (int i=0; i<inx.length; i+=3){
            Vector3f p1 = vertices[inx[i+1]];
            Vector3f v01 = vertices[inx[i]].subtract(p1);
            Vector3f v12 = p1.subtract(vertices[inx[i+2]]);
            if (v01.cross(v12).z<0){
                short aux = inx[i];
                inx[i] = inx[i+1];
                inx[i+1] = aux;
            }
        }*/
        indexes = new short[inx.length/*2*/];
        for (int i=0; i<inx.length; i++){
            //indexes[i] = inx[i];
            indexes[indexes.length-1-i] = inx[i];
        }
        //TEXTURE
        //1- find bounding box for the surface. Important to define texture
        float minX = vertices[0].x;
        float minY = vertices[0].y;
        float maxX = vertices[0].x;
        float maxY = vertices[0].y;
        for (int i=1; i<vertices.length; i++){
            if (minX>vertices[i].x)
                minX = vertices[i].x;
            if (minY>vertices[i].y)
                minY = vertices[i].y;
            if (maxX<vertices[i].x)
                maxX = vertices[i].x;
            if (maxY<vertices[i].y)
                maxY = vertices[i].y;
        }
        //2-define the texture
        texCoord = new Vector2f[vertices.length];
        for (int i=0; i<texCoord.length; i++){
            float transX = (vertices[i].x-minX)/(maxX-minX);
            float transY = (vertices[i].y-minY)/(maxY-minY);
            texCoord[i] = new Vector2f(transX,transY);
        }        
    }
    /**
     * This method is used solely to determine what surfaces are to the left or the right of an edge.
     * It ignores the z coordinate to determine if all the points of a surface are to the left or right of an edge. 
     * If the points of the surface are on both sides, it returns 0
     * @param direction
     * the direction of the edge. the z coordinate is ignored.
     * @return 
     * 1: the surface is completely to the left of the direction
     * -1: the surface is completely to the right of the direction
     * 0: points of the surface are on both sides
     */
    public static final int LEFT=1;
    public static final int RIGHT=-1;
    public static final int UNKNOWN=0;
    
    public int getPositionToDirection(Vector3f direction, Vector3f endPoint){
        int countLeft = 0;
        int countRight = 0;
        for (int i=0; i<pointCount2D; i++){
            float crossZ = direction.cross(vertices[i].subtract(endPoint)).z;
            if (crossZ==0)
                continue;
            if (crossZ>0)
                countLeft++;
            else
                countRight++;            
        }
        if (countLeft==0 && countRight!=0)
            return -1;
        if (countRight==0 && countLeft!=0)
            return 1;
        return 0;
    }
    public Mesh getMesh(){
        Mesh m = new Mesh();
        // Setting buffers
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        m.updateBound();
        return m;        
    }
    public Geometry geom;
    public Geometry getGeometry(AssetManager assetManager){
        if (geom==null){
            geom = getNode(assetManager);
            setGeometryColor();
        }
        
        return geom;
        
    }
    public void updatePosition(){
        if (geom==null)
            return;
        Mesh m = geom.getMesh();
        VertexBuffer vertx = m.getBuffer(VertexBuffer.Type.Position);
        vertx.updateData(BufferUtils.createFloatBuffer(vertices));
        vertx.setUpdateNeeded();
        m.updateBound();
        m.createCollisionData();
        geom.updateModelBound();
        

        /*m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.updateBound();
        geom.setMesh(m);
        
        geom.updateModelBound();*/
    }
    public Geometry getGeometry(){
        return geom;
    }
    public void setGeometryColor(ColorRGBA color){
        if (geom==null)
            return;
        Material mat = geom.getMaterial();
        mat.setColor("Color", color);
        geom.setMaterial(mat); 
    }

    public ColorRGBA getSurfaceColor(){
        return new ColorRGBA(s.getColor().getRed()/255f,s.getColor().getGreen()/255f, s.getColor().getBlue()/255f, s.getColor().getAlpha()/255f);
    }
    public void setGeometryColor(){
        if (geom==null)
            return;
        ColorRGBA color = getSurfaceColor();//new ColorRGBA(s.getColor().getRed()/255f,s.getColor().getGreen()/255f, s.getColor().getBlue()/255f, s.getColor().getAlpha()/255f);
        Material mat = geom.getMaterial();
        mat.setColor("Color", color);
        geom.setMaterial(mat);        
    }
    private Geometry getNode(AssetManager assetManager){
        Mesh m = getMesh();
        Geometry geom = new Geometry(getId(), m);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat);
        
        return geom;
    }
    public String getId(){
        return s.getId();
    }

/*    public Vector3f getCenter() {
        return center;
    }
*/
    Set<OrigamiEdge3D> relatedEdges;
    public void setRelatedEdges(Set<OrigamiEdge3D> edgesOf) {
        relatedEdges = edgesOf;
    }
    public Set<OrigamiEdge3D> getRelatedEdges() {
        return relatedEdges;
    }

    String getInternalId() {
        return s.internalId;
    }

    public void dumpSurface(){
        for (Vector3f v:vertices){
            System.out.print(v);
        }
    }
}
