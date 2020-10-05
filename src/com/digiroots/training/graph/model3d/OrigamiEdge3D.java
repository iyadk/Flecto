/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.TreeMap;
import com.digiroots.training.graph.OrigamiEdge;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.Point2D;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author khaddam
 */
public class OrigamiEdge3D extends DefaultWeightedEdge {
    private OrigamiSurface3D surfLeft;
    private OrigamiSurface3D surfRight;
    private OrigamiEdge edge;
    private Vector3f fromPoint;
    //private int fromPointInx = -1;
    //private int toPointInx = -1;
    private Vector3f toPoint;
    //private Vector3f center;
    //OrigamiShape3D.Transformer transformer;
    
       
    public static String decode(String val){
        return OrigamiEdge.decode(val);
    }
    
    public OrigamiEdge3D(OrigamiEdge edge, Vector3f start, Vector3f end, OrigamiSurface3D v1, OrigamiSurface3D v2) {
        //this.transformer = transformer;
        /*this.surfLeft = surfLeft;
        this.surfRight = surfRight;*/
        this.edge = edge;
        
        fromPoint = start;//transformer.transform2DCoordinates(((Point2D)edge.getV1()).x, ((Point2D)edge.getV1()).y);
        toPoint = end;//transformer.transform2DCoordinates(((Point2D)edge.getV2()).x, ((Point2D)edge.getV2()).y);
        /*We need to standarrdize the direction of the edge. Our conventions is: edges are in the upper half of the 2D space where y>0.
        horizontal edges are directed towards positive x axis.
        Method: 
         cross product the edge with the Z axes.
         If cross.x = 0 then edge is parallel to x. check cross.y: if >0, swap end and start
        else if cross.x<0 then revet start and end
        */
        Vector3f cross = toPoint.subtract(fromPoint).normalize().cross(Vector3f.UNIT_Z);
        boolean swap = (cross.x==0 && cross.y>0)|| cross.x<0;
        /*if (cross.x==0 && cross.y>0)
            swap = true;
        else
            if (cross.x<0)
                swap = true;*/
        if (swap){
            //System.out.println("swapping points");
            Vector3f aux = fromPoint;
            fromPoint = toPoint;
            toPoint = aux;
        }
        if (v1.getPositionToDirection(toPoint.subtract(fromPoint), toPoint)==OrigamiSurface3D.LEFT){
            //System.out.println("v1="+v1.getId()+" is left");
            this.surfLeft = v1;
            this.surfRight = v2;
        }
        else{
            this.surfLeft = v2;
            this.surfRight = v1;            
        }
        if (isMountain()){//Mountain hinges reverse direction and therefore surfaces.
            //System.out.println("Mountian swapping points");
            Vector3f aux = fromPoint;
            fromPoint = toPoint;
            toPoint = aux;
            OrigamiSurface3D vAux = this.surfLeft;
            this.surfLeft = this.surfRight;
            this.surfRight = vAux; 
        }
        leftSurfList.put(surfLeft.getId(), surfLeft);
        rightSurfList.put(surfRight.getId(), surfRight);
    }
    
    public String getType() {
        return edge.getType();
    }

    public String toString() {
        return edge.toString();
    }

    boolean isPhysical() {
        return edge.isPhysical();
    }

    public boolean isVally() {
        return edge.isVally();
    }

    public boolean isMountain() {
        return edge.isMountain();
    }

    public String getId(){
        return surfLeft.getId()+"#"+surfRight.getId();        
    }
    public OrigamiSurface3D getSurfLeft() {
        return surfLeft;
    }

    public OrigamiSurface3D getSurfRight() {
        return surfRight;
    }

    public Vector3f getCenter() {
        return getFromPoint().add(getToPoint()).divide(2);
    }

    public Vector3f getFromPoint() {
        return fromPoint;
    }
    public void setFromPoint(Vector3f subtract) {
        fromPoint = subtract;
        if (leftHinge!=null)
            leftHinge.setLocalTranslation(fromPoint);
    }
    
    public Vector3f getToPoint() {
        return toPoint;
    }
    public void setToPoint(Vector3f subtract) {
        toPoint = subtract;
        if (rightHinge!=null)
            rightHinge.setLocalTranslation(toPoint);
    }
    
    TreeMap<String, OrigamiSurface3D> leftSurfList = new TreeMap<>();
    TreeMap<String, OrigamiSurface3D> rightSurfList = new TreeMap<>();
    TreeMap<String, OrigamiSurface3D> unknownSurfList = new TreeMap<>();
    public void classifySurface(OrigamiSurface3D s) {
        //The edge is not translated to the center of the surface, while the surface points are all moved to translated to the surface center.
        Vector3f fromPointCentered  = fromPoint;//.subtract(s.center);
        Vector3f toPointCentered  = toPoint;//.subtract(s.center);
        
                
        int position = s.getPositionToDirection(toPointCentered.subtract(fromPointCentered), toPointCentered);
        switch(position){
            case OrigamiSurface3D.LEFT: leftSurfList.put(s.getId(), s); break;
            case OrigamiSurface3D.RIGHT:rightSurfList.put(s.getId(), s); break;
            default: unknownSurfList.put(s.getId(), s);
        }
    }

    public boolean bothSurfacesLeft(String surf1, String surf2) {
        return surfaceIsLeft(surf1) && surfaceIsLeft(surf2);
    }
    public boolean bothSurfacesLeft(OrigamiSurface3D surf1, OrigamiSurface3D surf2) {
        return surfaceIsLeft(surf1.getId()) && surfaceIsLeft(surf2.getId());
    }
    public boolean bothSurfacesRight(String surf1, String surf2) {
        return surfaceIsRight(surf1) && surfaceIsRight(surf2);
    }
    public boolean bothSurfacesRight(OrigamiSurface3D surf1, OrigamiSurface3D surf2) {
        return surfaceIsRight(surf1.getId()) && surfaceIsRight(surf2.getId());
    }

    private boolean surfaceIsLeft(String surf1) {
        return leftSurfList.containsKey(surf1);
        /*for (OrigamiSurface3D s: this.leftSurfList)
            if (s.getId().equalsIgnoreCase(surf1))
                return true;
        return false;*/
    }
    private boolean surfaceIsRight(String surf1) {
        return rightSurfList.containsKey(surf1);
        /*for (OrigamiSurface3D s: this.rightSurfList)
            if (s.getId().equalsIgnoreCase(surf1))
                return true;
        return false;*/
    }
    public TreeMap<String, OrigamiSurface3D> getLeftSurfaceMap(){
        TreeMap<String, OrigamiSurface3D> map = new TreeMap<>(this.leftSurfList);
        //This method is called on active edges. Therefore, we arer sure that collideEdges are equal ones. 
        for (OrigamiEdge3D edge: collideEdgeMap.values()){
            if (this.getType().equalsIgnoreCase(edge.getType())){//Both edges are mountain or vally. Copy as organized
                map.putAll(edge.leftSurfList);
            }
            else
                map.putAll(edge.rightSurfList);
        }
        return map;
    }
    public TreeMap<String, OrigamiSurface3D> getRightSurfaceMap(){
        TreeMap<String, OrigamiSurface3D> map = new TreeMap<>(this.rightSurfList);
        //This method is called on active edges. Therefore, we arer sure that collideEdges are equal ones. 
        for (OrigamiEdge3D edge: collideEdgeMap.values()){
            if (this.getType().equalsIgnoreCase(edge.getType())){//Both edges are mountain or vally. Copy as organized
                map.putAll(edge.rightSurfList);
            }
            else
                map.putAll(edge.leftSurfList);
        }
        return map;
    }
    public boolean isLeftSurface(OrigamiSurface3D surf){
        return this.surfLeft==surf;
    }
    public boolean isRightSurface(OrigamiSurface3D surf){
        return this.surfRight==surf;
    }

    float currAngle = FastMath.PI;
    float MaxAngle = Math.max(0, currAngle);
    float MingAngle = Math.min(0,FastMath.PI);
    public float getAngle() {
        return currAngle;
    }

    public void setAngle(float currAngle) {
           this.currAngle= currAngle;
    }

    boolean coordUpdated = false;
    public void setCoordUpdated(boolean b) {
        coordUpdated = b;
    }
    public boolean getCoordUpdated(){
        return coordUpdated;
    }

    public float getMaxAngle() {
        return MaxAngle;
    }

    public float getAngleStep() {
        return (getMaxAngle()-getMinAngle())/1000;
    }

    public float getMinAngle() {
        return MingAngle;
    }

    private void updateCollidedEdges(int direction) {
        if (this.collideEdgesAreMerged()){
            for (OrigamiEdge3D e: this.collideEdgeMap.values()){
                e.checkHingeEvent(e.currAngle, currAngle, direction);
                e.currAngle = this.currAngle;
            }
        }
    }
    public float openAngle() {
        float currAngle = getAngle()+getAngleStep();
        if (!(currAngle<=getMaxAngle() && currAngle>=getMinAngle())){
            if (currAngle>getMaxAngle()){//The last step
                float res = getMaxAngle()-this.currAngle;
                checkHingeEvent(this.currAngle, getMaxAngle(), 1);
                this.currAngle=getMaxAngle();
                updateCollidedEdges(1);
                return res;
            }            
            return 0;
        }
        checkHingeEvent(this.currAngle, currAngle, 1);
        this.currAngle  = currAngle;
        updateCollidedEdges(1);
        return getAngleStep();
    }
    public float closeAngle() {        
        float currAngle = getAngle()-getAngleStep();        
        if (!(currAngle<=getMaxAngle() && currAngle>=getMinAngle())){
            if (currAngle<getMinAngle()){//The last step
                float res = this.currAngle-getMinAngle();
                checkHingeEvent(this.currAngle, getMinAngle(), -1);
                this.currAngle=getMinAngle();
                updateCollidedEdges(-1);
                return res;
            }
            return 0;
        }
        checkHingeEvent(this.currAngle, currAngle, -1);
        this.currAngle  = currAngle;
        updateCollidedEdges(-1);
        return getAngleStep();
    }
    AssetManager assetManager;
    Geometry leftHinge;
    Geometry rightHinge;
    Node hingeNode;
    public void attachHingeNodes(AssetManager assetManager, Node root){
        this.assetManager = assetManager;
/*        String mirrirEdgeId = "Hinge|"+surfRight.getId()+"#"+surfLeft.getId();
        if (root.getChild(mirrirEdgeId)!=null){
            return;
        }
*/
        // Creating a geometry, and apply a single color material to it
        leftHinge = createGeometry(fromPoint, surfLeft.getSurfaceColor()/* ColorRGBA.White*/, surfLeft.getId()+"#"+ surfRight.getId());
        rightHinge = createGeometry(toPoint, surfRight.getSurfaceColor()/*ColorRGBA.Blue*/, surfRight.getId()+"#"+ surfLeft.getId());
        hingeNode = new Node("Hinge|"+this.getId());
        
        hingeNode.attachChild(leftHinge);
        hingeNode.attachChild(rightHinge);
        leftHinge.setLocalTranslation(0, 0.05f, 0);
        rightHinge.setLocalTranslation(0, -0.05f, 0);
        hingeNode.setCullHint(Spatial.CullHint.Always);
        root.attachChild(hingeNode);        
//        System.out.println("Hinge node is created ");
    }
    public void setHingeVisible(BoundingBox boundingBox, boolean visible){
        //System.out.println("Set hinge "+ this.getId()+" "+visible+" where node is "+((hingeNode==null)?"NULL":"fine"));
        if (hingeNode==null)
            return;
        if (!visible)
            hingeNode.setCullHint(Spatial.CullHint.Always);
        else{
            hingeNode.setCullHint(Spatial.CullHint.Never);
            Vector3f dir = fromPoint.subtract(toPoint).normalize();
            Ray ray = new Ray(fromPoint, dir);

            CollisionResults results = new CollisionResults();

            boundingBox.collideWith(ray, results);
            if (results.size() > 0) {
                Vector3f contactPoint = results.getClosestCollision().getContactPoint();            
                contactPoint = dir.scaleAdd(0.14f, contactPoint);
                hingeNode.setLocalTranslation(contactPoint);//fromPoint.subtract(toPoint).normalizeLocal().scaleAdd(bound, toPoint ));
            }
        }
    }
    private Geometry createGeometry(Vector3f point, ColorRGBA color, String name){
        Box b = new Box(0.05f, 0.05f, 0.05f);
        Geometry geom = new Geometry(name, b);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", color);
        geom.setMaterial(mat2);        
        geom.setLocalTranslation(point.x, point.y, point.z);
        return geom;
    }

    boolean edgeIsActive = true;
    public boolean isActive() {
        if (collideEdgeMap.size()==0)
            return true;
        if (!collideEdgesAreMerged()){
            return false;
        }
        if (unknownSurfList.size()==0)
            return true;
        //From this point, collided edges are merged ones. 
        //Check unkown surface list is resolved by merged lists: a merged edge contains the unknown surface on one of its sides
        return checkUnknowSurfacesResolved();
         
    }
    public void setActive(boolean isActive){
        edgeIsActive = isActive;
    }

    TreeMap<String, OrigamiEdge3D> collideEdgeMap = new TreeMap<>();
    void addCollideEdge(OrigamiEdge3D edge2) {
        collideEdgeMap.put(edge2.getId(), edge2);
    }

    void resetCollideList() {
        collideEdgeMap = new TreeMap<>();
    }

    protected boolean collideEdgesAreMerged() {
        Vector3f dir = getNormalizedAxis();
        for (OrigamiEdge3D edge: collideEdgeMap.values()){
            Vector3f dir2 = edge.getNormalizedAxis();
            if (dir.angleBetween(dir2)>getAngleStep())
                return false;
        }
        return true;
    }
    
    public Vector3f getAxis(){
        return this.getToPoint().subtract(this.getFromPoint());
    }
    public Vector3f getNormalizedAxis(){
        return this.getToPoint().subtract(this.getFromPoint()).normalize();
    }

    private boolean checkUnknowSurfacesResolved() {
        for (String s: unknownSurfList.keySet()){
            boolean resolved=false;
            for (OrigamiEdge3D edge: collideEdgeMap.values()){
                if (edge.surfaceIsLeft(s)||edge.surfaceIsRight(s)){
                    resolved=true;
                    break;
                }
            }
            if (!resolved)
                return false;
        }
        return true;
    }
    public String getInternalId(){
        return surfLeft.getInternalId()+"#"+surfRight.getInternalId();
    }

    OrigamiEdgeAttributes edgeAttribute = null;
    void setEdgeAttributes(OrigamiEdgeAttributes edgeAttribute) {
        this.edgeAttribute = null;
        if (edgeAttribute==null)
            return;
        if (edgeAttribute.eventList.size()==0)
            return;
        this.edgeAttribute = edgeAttribute;
    }

    private ArrayList<OrigamiHingeEventListener> origamiHingeEventListener = null;

    public void setOrigamiHingeEventListener(ArrayList<OrigamiHingeEventListener> origamiHingeEventListener) {
        this.origamiHingeEventListener = origamiHingeEventListener;
    }

    private void checkHingeEvent(float oldAngle, float newAngle, int direction) {  
        //System.out.println("checkHingeEvent");
        if (this.edgeAttribute==null || origamiHingeEventListener==null)
            return;
        //System.out.println("Start");
        float min = Math.min(oldAngle, newAngle);
        float max = Math.max(oldAngle, newAngle);
        ArrayList<OrigamiEdgeAttributes.OrigamEdgeEvent> x = edgeAttribute.eventList;
        for (OrigamiEdgeAttributes.OrigamEdgeEvent e: edgeAttribute.eventList){
            //System.out.println("check "+e);
            if (e.direction==direction || e.direction==0){
                float angleInRad = e.angle*FastMath.DEG_TO_RAD;
                //System.out.println("check "+e + " that has angle: "+angleInRad+" between "+min+","+max);
                if ((angleInRad>=min) && (angleInRad<=max)){
                    ;//Trigger this event to all listeners
                    //System.out.println("needs triggering");
                    for (OrigamiHingeEventListener listener: origamiHingeEventListener){
                        //System.out.println("first listner is triggered");
                        listener.onHingeEvent(e);
                    }
                }
            }
        }
        //System.out.println("end checkHingeEvent");
    }
}