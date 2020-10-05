/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import com.digiroots.foldale.controls.HingeControl;
import com.digiroots.main.project.ProjectDesc;
import com.digiroots.training.graph.OrigamiEdge;
import com.digiroots.training.graph.OrigamiShape;
import com.digiroots.training.graph.OrigamiSurface;
import com.digiroots.training.graph.Point2D;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author khaddam
 */
public class OrigamiShape3D {
    OrigamiShape shape;
    public Transformer transformer; 
    ProjectDesc project;

    public void setClicked3DPoint(Point2D point, String surfaceName){
        shape.setClicked3DPoint(point, surfaceName);
    }
    public OrigamiShape3D(OrigamiShape shape,     ProjectDesc project){
        this.shape = shape;
        float ratioX = shape.ratioX3D;
        float ratioY = shape.ratioY3D;
        transformer = new Transformer(shape, ratioX, ratioY);
        this.project = project;
    }
    TreeMap<String, Point3D> vertexMap = new  TreeMap<>();
    TreeMap<String, Point3D> loadedVertexMap = null;
    
    TreeMap<String, OrigamiEdge3D> edgeMap = new  TreeMap<>();
    TreeMap<String, OrigamiSurface3D> surfaceLookupById = new  TreeMap<>();
    UndirectedGraph<OrigamiSurface3D, OrigamiEdge3D> shapeGraph = new SimpleGraph<>(
                        new ClassBasedEdgeFactory<>(OrigamiEdge3D.class));
    private Point3D transform2dPoint(Point2D point, String surfaceId){
        String pointId = point.getId();
        if (vertexMap.containsKey(pointId)){
            //System.out.println("Point "+pointId+" found in vertexMap");
            Point3D p = vertexMap.get(pointId);
            p.addSurface(surfaceId);
            return p;
        }                    
        //System.out.println("Point "+pointId+" NOT found in vertexMap");
        Vector3f res = transformer.transform2DCoordinates(point.x, point.y);
        Point3D p3d = new Point3D(pointId, res, surfaceId);
        
        vertexMap.put(pointId, p3d);
        return p3d;
    }
    private OrigamiSurface3D create3DSurface(OrigamiSurface s){
        //Vector3f[] surfacePoints = new Vector3f[s.points.length];
        Point3D[] surfacePoints = new Point3D[s.points.length];
        for (int i=0; i<s.points.length; i++){
            surfacePoints[i] = transform2dPoint(s.points[i], s.getId());
        }
        OrigamiSurface3D surface3d = new OrigamiSurface3D(s, surfacePoints);
        surfaceLookupById.put(s.getId(), surface3d);
        shapeGraph.addVertex(surface3d);
        return surface3d;
    }
    private OrigamiEdge3D create3DEdge(OrigamiEdge edge, Point2D start, Point2D end){
        String [] edgeSurf = getEdgeSurfaces(start.getId(), end.getId());
        if (edgeSurf==null)
            return null;
        String surf1Id = edgeSurf[0];
        String surf2Id = edgeSurf[1];   
        String edgeKey1 = surf2Id+"#"+surf1Id;
        String edgeKey = edgeKey1;
        if (edgeMap.containsKey(edgeKey)){
            System.out.println("Edge "+edgeKey+" found. Not creating");
            return edgeMap.get(edgeKey);
        }
        edgeKey = surf1Id+"#"+surf2Id;
        if (edgeMap.containsKey(edgeKey)){
            System.out.println("Edge "+edgeKey+" found. Not creating");
            return edgeMap.get(edgeKey);
        }
        OrigamiSurface3D s1 = surfaceLookupById.get(surf1Id);
        OrigamiSurface3D s2 = surfaceLookupById.get(surf2Id);
        Vector3f fromPoint = vertexMap.get(start.getId()).getVertex();
        Vector3f toPoint = vertexMap.get(end.getId()).getVertex();
        
        OrigamiEdge3D e = new OrigamiEdge3D(edge, fromPoint, toPoint, s1,s2);
        shapeGraph.addEdge(s1, s2,e);
        e.setEdgeAttributes(shape.getEdgeAttribute(e.getInternalId()));
        e.setOrigamiHingeEventListener(shape.getOrigamiHingeEventListeners());     

        edgeMap.put(edgeKey, e);
        return e;
    }
    public String[] getEdgeSurfaces(String pointId1, String pointId2){
        Point3D p1 = vertexMap.get(pointId1);
        Point3D p2 = vertexMap.get(pointId1);
        ArrayList<String> commonSurfaces = new ArrayList<>();
        for (String surfaceId1: p1.getSurfaceList()){
            for (String surfaceId2: p2.getSurfaceList()){
                if (surfaceId1.equalsIgnoreCase(surfaceId2))
                    commonSurfaces.add(surfaceId1);
            }            
        }
        if (commonSurfaces.size()!=2)
            return null;
        return commonSurfaces.toArray(new String[0]);
    }
    public ArrayList<OrigamiSurface3D> create3DSurfaces() {
        ArrayList<OrigamiSurface> surfaceList = shape.getColoredSurfaces();
        ArrayList<OrigamiSurface3D> surface3dList = new ArrayList<>();
        for (OrigamiSurface s: surfaceList){
            OrigamiSurface3D surface3d = create3DSurface(s);//new OrigamiSurface3D(s, transformer);
            surface3dList.add(surface3d);            
        }
        
        for (OrigamiEdge<Point2D> edge: shape.getShapeGraph().edgeSet()){           
            if (edge.isPhysical()){
                System.out.println(" edge "+edge.toString()+" is physical");
                continue;
            }
            System.out.println(" edge "+edge.toString()+" is not physical");

            Point2D start = edge.getV1(); 
            Point2D end = edge.getV2();
            
            create3DEdge(edge, start, end);                    
        }    
        //Every edge classifies all surfaces positions according to it: left, right and unknown.
        for (OrigamiEdge3D e: shapeGraph.edgeSet()){
            for (OrigamiSurface3D s: surface3dList){
                e.classifySurface(s);
                s.setRelatedEdges(shapeGraph.edgesOf(s));
            }
        }
        return surface3dList; 
    }
    //public SkeletonControl skeletonControl;
    
    public void rotateShape(OrigamiEdge3D edge, float radStep, OrigamiSurface3D s, Collection<OrigamiSurface3D> relatedSurfaces){
        TreeMap<String, Vector3f> updatePoints = new TreeMap<>();
        for (Point3D p: s.vertices3D){
            if (updatePoints.containsKey(p.getId()))
                    continue;
            updatePoints.put(p.getId(), p.getVertex());
        }
        if (relatedSurfaces!=null){
            for (OrigamiSurface3D s1: relatedSurfaces){
                for (Point3D p: s1.vertices3D){
                    if (updatePoints.containsKey(p.getId()))
                            continue;
                    updatePoints.put(p.getId(), p.getVertex());
                }                
            }
        }
        
        Vector3f lockAxis = edge.getToPoint().subtract(edge.getFromPoint()).normalize();
        Vector3f hinge = new Vector3f(edge.getFromPoint());
        Vector3f relRef = new Vector3f(hinge);
        Matrix3f relRot = new Matrix3f();
        relRot.fromAngleNormalAxis(radStep, lockAxis);
        relRot.mult(relRef, relRef);        
        Quaternion relQuat = new Quaternion();
        relQuat.apply(relRot); 

        for (Vector3f p: updatePoints.values()){        
            Vector3f newP = new Vector3f(p.subtract(edge.getFromPoint()));
            relRot.mult(newP, newP);
            Vector3f rotP = new Vector3f(newP.add(edge.getFromPoint()));
            p.x = rotP.x;
            p.y = rotP.y;
            p.z = rotP.z;
        }
        
        s.updatePosition();
        if (relatedSurfaces!=null){
            for (OrigamiSurface3D s1: relatedSurfaces)
                s1.updatePosition();
        }
   }

    public static boolean debugMode = false;
    
    private Node createNewNode(AssetManager assetManager){
        /*if (debugMode){
            SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", skeletonControl.getSkeleton());
            Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
            mat.getAdditionalRenderState().setWireframe(true);
            mat.setColor("Color", ColorRGBA.Green);
            mat.getAdditionalRenderState().setDepthTest(false);
            skeletonDebug.setMaterial(mat);
            Node node = new Node();
            node.attachChild(skeletonDebug);
            return node;            
        }*/
        return new Node();
    }
    Node shapeNode;
    public void prepareGeometryAndBones(AssetManager assetManager){       
        for (OrigamiSurface3D s: shapeGraph.vertexSet()){
            s.getGeometry(assetManager);//Creates the geometry in each surface. use s.getGeometry() with no params to get it later
        }        
    }
    public Node getNode(AssetManager assetManager){
        shapeNode = createNewNode(assetManager);
        for (OrigamiSurface3D s: shapeGraph.vertexSet()){
            shapeNode.attachChild(s.getGeometry());
            //s.onSurfaceAttachedToNode();
        }
        shapeNode.addControl(new HingeControl(this));
        for (OrigamiEdge3D edge: shapeGraph.edgeSet()){
            edge.attachHingeNodes(assetManager, shapeNode);
        }
        return shapeNode;
    }
    void setActiveEdge() {
        OrigamiEdge3D edge;
        Set<OrigamiEdge3D> edgeSet = shapeGraph.edgeSet();
        if (edgeSet.size()>0){
            edge = edgeSet.toArray(new OrigamiEdge3D[0])[0];
            setActiveEdge(edge);
        }
    }

    OrigamiEdge3D activeEdge = null;
    boolean setActiveEdge(OrigamiEdge3D edge) {
        if (!edge.isActive()){
            System.out.println("Edge "+edge.getId()+" is inactive...");
            printEdgeStatus(edge);
            return false;
        }
        
        if (shapeNode==null)
            return false;
        HingeControl controller = shapeNode.getControl(HingeControl.class);
        if (controller==null)
            return false;
        controller.setEdge(edge);
        activeEdge = edge;
        return true;
    }
    boolean setLocalBinging = true;

    OrigamiEdge3D getEdge(String s1, String s2) {
        OrigamiSurface3D surface1 = surfaceLookupById.get(s1);
        OrigamiSurface3D surface2 = surfaceLookupById.get(s2);
        if (surface1==null || surface2==null)
            return null;
        Set<OrigamiEdge3D> edgeList = shapeGraph.edgesOf(surface1);
        if (edgeList==null)
            return null;
        for (OrigamiEdge3D e: edgeList){
            if (shapeGraph.getEdgeTarget(e)==surface2 || shapeGraph.getEdgeSource(e)==surface2)
                return e;
        }
        return null;
        //return shapeGraph.getEdge(surface2, surface1);
    }

    void computeEdges() {
        OrigamiEdge3D[] edgeList = shapeGraph.edgeSet().toArray(new OrigamiEdge3D[0]);
        for(int i=0; i<edgeList.length; i++){
            edgeList[i].resetCollideList();
        }
        for(int i=0; i<edgeList.length; i++){
            OrigamiEdge3D edge1 = edgeList[i];
            for(int j=i+1; j<edgeList.length; j++){
                OrigamiEdge3D edge2 = edgeList[j];
                //Check if both edges collide: in the same plane and NOT related
                //A related edge has both surfaces on the same side of the edge.
                if (samePlane(edge1,edge2)){
                    boolean edgesAreRelated = edge1.bothSurfacesLeft(edge2.getSurfLeft(), edge2.getSurfRight())
                        ||edge1.bothSurfacesRight(edge2.getSurfLeft(), edge2.getSurfRight());
                    if (!edgesAreRelated){
                        edge1.addCollideEdge(edge2);
                        edge2.addCollideEdge(edge1);
                    }                    
                }
            }            
        }
        drawActiveEdges(edgeList);
        for(int i=0; i<edgeList.length; i++){
            //printEdgeStatus(edgeList[i]);
        }
    }
    public boolean showActiveEdges = false;
    private void drawActiveEdges(OrigamiEdge3D[] edgeList){
        BoundingBox boundingBox = calculateShapeBoundingBox();
        for(int i=0; i<edgeList.length; i++){
            OrigamiEdge3D edge = edgeList[i];
            if (edge.isActive()){
                    edge.setHingeVisible(boundingBox, showActiveEdges);
                    for (OrigamiEdge3D ed : edge.collideEdgeMap.values()){
                        ed.setHingeVisible(boundingBox, false);
                    }
            }
            else
                edge.setHingeVisible(boundingBox, false);
            
        }
    }
    private BoundingBox calculateShapeBoundingBox(){
        Point3D[] pArr = this.vertexMap.values().toArray(new Point3D[0]);
        float minX=pArr[0].vertex.x;
        float minY=pArr[0].vertex.y;
        float minZ=pArr[0].vertex.z;
        float maxX=pArr[0].vertex.x;
        float maxY=pArr[0].vertex.y;
        float maxZ=pArr[0].vertex.z;
        for (int i=1; i<pArr.length; i++){
            Point3D p = pArr[i];
            if (minX>p.vertex.x)
                minX = p.vertex.x;
            if (minY>p.vertex.y)
                minY = p.vertex.y;
            if (minZ>p.vertex.z)
                minZ = p.vertex.z;
            
            if (maxX<p.vertex.x)
                maxX = p.vertex.x;
            if (maxY<p.vertex.y)
                maxY = p.vertex.y;
            if (maxZ<p.vertex.z)
                maxZ = p.vertex.z;
        }
        float offset = 0;//0.06f;
        return new BoundingBox(new Vector3f(minX-offset, minY-offset, minZ-offset), new Vector3f(maxX+offset, maxY+offset, maxZ+offset));
        
    }

    float EPSILON = 0.000001f;
    private boolean samePlane(OrigamiEdge3D edge1, OrigamiEdge3D edge2) {
        Vector3f a = edge1.getToPoint().subtract(edge1.getFromPoint());
        Vector3f b = edge2.getToPoint().subtract(edge2.getFromPoint());
        Vector3f c = edge1.getToPoint().subtract(edge2.getFromPoint());
        //System.out.println("plane for "+edge1.getId()+","+edge2.getId()+"="+ a.dot(b.cross(c)));
        return a.dot(b.cross(c))<EPSILON;
    }

    private void printEdgeStatus(OrigamiEdge3D edge) {
         System.out.println("**Edge info "+edge.getId());
         System.out.println("  status "+(edge.isActive()?"Active":"Inactive"));
         System.out.println("  collideList: "+(edge.collideEdgesAreMerged()?"Merged":"Not erged"));
         Vector3f dir = edge.getNormalizedAxis();
         System.out.println("      edge Dir="+dir);
         for (OrigamiEdge3D e:edge.collideEdgeMap.values()){
             Vector3f dir2 = e.getNormalizedAxis();
             System.out.println("      "+e.getId()+" Dir="+dir2);
         }
         System.out.println();
         System.out.print("  Left surfaces:\n     ");
         for (OrigamiSurface3D e:edge.getLeftSurfaceMap().values()){
             System.out.print(e.getId()+", ");
         }
         System.out.println();
         System.out.print("  Right surfaces:\n     ");
         for (OrigamiSurface3D e:edge.getRightSurfaceMap().values()){
             System.out.print(e.getId()+", ");
         }
         System.out.println();
         
    }

    TreeMap<String, Float> angleMap = null;
    public void loadInitialState() {
        FileInputStream streamIn = null;
        ObjectInputStream objectinputstream = null;
        try {
            streamIn = new FileInputStream(project.getInitialStateFile());
            objectinputstream = new ObjectInputStream(streamIn);            
            this.loadedVertexMap = (TreeMap<String,Point3D> ) objectinputstream.readObject();
            this.angleMap = (TreeMap<String,Float> ) objectinputstream.readObject();
            System.out.println("Initial state loaded");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(streamIn);
            closeStream(objectinputstream);
        }        
    }
    private static void closeStream(Closeable s){
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }
    public void saveInitialState(){
        FileOutputStream fout=null;
        ObjectOutputStream oos=null;
        TreeMap<String, Float> angleMap = new TreeMap<>();
        for (String key: this.edgeMap.keySet()){
            angleMap.put(key, edgeMap.get(key).currAngle);
        }
        try {
            fout = new FileOutputStream(project.getInitialStateFile());
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.vertexMap);
            oos.writeObject(angleMap);
            project.setHasSavedInitialState(true);
            System.out.println("Initial state saved");

        } catch (IOException  ex) {
            System.out.println("Error saving initial state: ");
            ex.printStackTrace(System.out);
        } finally {
            closeStream(fout);
            closeStream(oos);            
        }   
    }

    void loadIfNecessary() {
        boolean loaded = false;
        if (angleMap!=null){
            loaded  = true;
            //System.out.println("angle map found");
            for (String edgeKey: edgeMap.keySet()){
                //System.out.println(" load angle for edge "+edgeKey);
                
                OrigamiEdge3D e = edgeMap.get(edgeKey);
                if (angleMap.containsKey(edgeKey))
                    e.currAngle= angleMap.get(edgeKey);
                //else if (angleMap.containsKey(edgeKey1))
                //    e.currAngle= angleMap.get(edgeKey1);
                else
                    System.out.println("ERROR:: Edge "+edgeKey+" is not found in the initial state...");
                //System.out.println("  Load angle "+e.currAngle+" for edge "+edgeKey);
            }
        }
        if (loadedVertexMap!=null){
            loaded  = true;
            //System.out.println("loaded vertex  map found");
            for (String pointId: vertexMap.keySet()){
                //System.out.println(" load vertex for point "+pointId);
                Point3D p3d = vertexMap.get(pointId);
                Point3D loadedp3d = loadedVertexMap.get(pointId);
                p3d.setVertexInternal(loadedp3d.getVertex());
            }
        }
        if (loaded){
            for (OrigamiSurface3D s: this.surfaceLookupById.values())
                s.updatePosition();
        }        
    }
    public static class Transformer{
        OrigamiShape shape;
        float ratioX;
        float ratioY;
        Transformer(OrigamiShape shape, float ratioX, float ratioY){
            this.shape = shape;
            this.ratioX = ratioX;
            this.ratioY = ratioY;
        }
        public Vector3f transform2DCoordinates(int x, int y){
            float rx = -shape.shiftX-shape.paperWidth/2;
            float ry = -shape.shiftY-shape.paperHeight/2;
            Vector3f res = new Vector3f((x + rx) * ratioX, (-y - ry) * ratioY, 0);
            return res;
        }
        public Point2D transform3DCoordinates(Vector3f p){
            int x = Math.round( p.x/ratioX+shape.shiftX+shape.paperWidth/2);
            int y = Math.round(-p.y/ratioY+shape.shiftY+shape.paperHeight/2);
            
            Point2D point = new Point2D(x,y);
            
            return point;
        }
    }
}
