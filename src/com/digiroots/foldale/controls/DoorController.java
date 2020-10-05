/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.controls;

/**
 *
 * @author khaddam
 */
import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import java.util.logging.Logger;



import com.jme3.math.FastMath;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;


import com.jme3.scene.Node;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.digiroots.training.graph.model3d.OrigamiEdge3D;
import com.digiroots.training.graph.model3d.OrigamiSurface3D;





/**

 * a controller for door animation

 * @author galun

 * @version $Id: DoorController.java,v 1.2 2006/08/27 15:44:38 galun Exp $

 *

 */

public class DoorController extends AbstractControl {



   private static final long serialVersionUID = 872371512005036263L;


   public static final int CLOCKWISE = -1;

   public static final int COUNTERCLOCKWISE = 1;

   //private Spatial spatialLeft;
   //private Spatial spatialRight;

   private Vector3f originalTranslation;
   private Vector3f originalTranslation2;
   String boneName;
   String boneName2;

   private Vector3f hinge;

   private Vector3f ref;
   private Matrix3f rot;


   private Vector3f lockAxis;

   //private float start;

   //private float stop;

   //private float curTime;

   private int direction;

   //private float radPerSec = 360 * FastMath.DEG_TO_RAD;

    Skeleton skeleton;

   private void init(OrigamiEdge3D edge) {
      this.edge = edge;
      Spatial spatial = getSpatialLeft();
      //this.spatialLeft = spatial;
      // save a deep copy of the original translation
      originalTranslation = new Vector3f(spatial.getLocalTranslation());
      this.boneName = edge.getSurfLeft().getId();
      
      Spatial spatial2 = getSpatialRight();//edge.getSurfRight().getGeometry();
      //this.spatialRight = spatial2;
      // save a deep copy of the original translation
      originalTranslation2 = new Vector3f(spatial2.getLocalTranslation());
      this.boneName2 = edge.getSurfRight().getId();
      
      //curTime = 0;
      
      //setActive(false);

   }
   OrigamiEdge3D edge; 
   public DoorController(OrigamiEdge3D edge, Skeleton skeleton) {
        init(edge);
        rot = new Matrix3f();
        this.skeleton = skeleton;
        setRotationAxe(edge.getFromPoint().subtract(edge.getToPoint()).normalize());

   }

   public OrigamiEdge3D getEdge(){
       return edge;
   }
/*    public DoorController(Node root1Node, Skeleton skeleton, String boneName) {
        this(root1Node);
        this.skeleton = skeleton;
        this.boneName = boneName;
    }
*/
   public float getSpeed(){
       return 0.002f;
   }

   @Override
   public void update(float time) {
   }

   public boolean contains(Spatial s){
       return s==getSpatialLeft() || s==getSpatialRight();
   }
   
   float currAngle = 0;//-FastMath.PI;
   float currAngle2 = 0;//FastMath.PI;
   private void rotate(Spatial s, float time){
       if (s!=getSpatialLeft() && s!=getSpatialRight())
           return;
//if (! isActive())

      //   return;
      
      /*curTime += time * getSpeed();

      float rad = start + curTime * radPerSec * direction;

      System.out.println("angle:" + rad);
      if (((stop - rad) * direction) <= 0) {

         rad = stop;
         System.out.println("Stopped");
      }*/
      //float rad = currAngle;//0.001f;
      
      //float newSurfAngle = Math.abs (currAngle-currAngle2)+time*getSpeed();
       System.out.println("angle: L,R" + currAngle+","+currAngle2);
       System.out.println(" new angle: " +  Math.abs (currAngle-currAngle2));

       if (s==getSpatialLeft()){
         currAngle+=time*getSpeed();
          float newSurfAngle = Math.abs (currAngle-currAngle2);
          //System.out.println(newSurfAngle);
          if (newSurfAngle<0|| newSurfAngle> 2*FastMath.PI){ //TODO: condition for low limit needs review. Maybe move with the rest to reach complete collapse between surfaces.
              currAngle-=time*getSpeed();
              return;
          }
          
          //currAngle+=time*getSpeed();
           updateSpatial1(currAngle);           
       }
       else {
          currAngle2+=time*getSpeed();
          float newSurfAngle = Math.abs (currAngle-currAngle2);
          //System.out.println(newSurfAngle);
          if (newSurfAngle<0 || newSurfAngle> 2*FastMath.PI){ 
              currAngle2-=time*getSpeed();
              return;
          }
           updateSpatial2(currAngle2);                      
       }
   }
   
   private void updateSpatial1(float rad){
      
      rot.fromAngleNormalAxis(rad, lockAxis);

      ref.set(hinge);

      rot.mult(ref, ref);

      Quaternion q = new Quaternion();
      q.apply(rot);

      Bone bone = skeleton.getBone(boneName);
      bone.setUserTransforms(originalTranslation.subtract(ref.subtractLocal(hinge)), q,
	new Vector3f(1,1,1));
      /*
      getSpatialLeft().setLocalRotation(rot);
      //System.out.println("rot="+rot);

      getSpatialLeft().setLocalTranslation(originalTranslation.subtract(ref.subtractLocal(hinge)));
      Bone bone = skeleton.getBone(boneName);
      
      bone.setUserTransforms(getSpatialLeft().getLocalTranslation(), getSpatialLeft().getLocalRotation(),
	new Vector3f(1,1,1));
      */
      skeleton.updateWorldVectors();
      //skeleton.setBindingPose();

   }

      private void updateSpatial2(float rad){
 
      rot.fromAngleNormalAxis(rad, lockAxis);

      ref.set(hinge);

      rot.mult(ref, ref);

      Bone bone = skeleton.getBone(boneName2);
      Quaternion q = new Quaternion();
      q.apply(rot);
      bone.setUserTransforms(originalTranslation2.subtract(ref.subtractLocal(hinge)), q,
	new Vector3f(1,1,1));

      
      //getSpatialRight().setLocalRotation(rot);
      /*//System.out.println("rot="+rot);

      getSpatialRight().setLocalTranslation(originalTranslation2.subtract(ref.subtractLocal(hinge)));
      Bone bone = skeleton.getBone(boneName2);
      
      bone.setUserTransforms(getSpatialRight().getLocalTranslation(), getSpatialRight().getLocalRotation(),
	new Vector3f(1,1,1));
      */
      skeleton.updateWorldVectors();
   }


   /**

    * set the animation (rotation) this controller should do

    * @param hinge the pivot point for the rotation in the model space

    * @param axis the x, y or z axis to rotate around (not the hinge axis!)

    * @param start the starting angle in degrees

    * @param stop the stop angle in degrees

    */


   private void setRotationHinge(Vector3f hinge) {
      this.hinge = new Vector3f(hinge); // better use a deep copy
      //this.start = 0 * FastMath.DEG_TO_RAD;
      //this.stop = 6000 * FastMath.DEG_TO_RAD;
      this.direction = 1;
      ref = new Vector3f(hinge);
   }   
   private void setRotationAxe(Vector3f axis) {
      if (axis != null)
         this.lockAxis = axis;
      //this.start = 0 * FastMath.DEG_TO_RAD;
      //this.stop = 6000 * FastMath.DEG_TO_RAD;
      this.direction = 1;
   }


   public Spatial getSpatialLeft() {

      return edge.getSurfLeft().getGeometry();
   }
   
   public Spatial getSpatialRight(){
       return edge.getSurfRight().getGeometry();
   }

    @Override
    protected void controlUpdate(float tpf) {
        //update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void rotateClose(Geometry selectedSecond) {
        prepareRotation(selectedSecond);
        rotate(selectedSecond, -1f);
    }

    public void rotateOpen(Geometry selectedSecond) {
        prepareRotation(selectedSecond);
        rotate(selectedSecond, 1f);
    }
    private OrigamiSurface3D getSurface3DFromGeometry(Geometry geom){
        if (!contains(geom))
            return null;
        if (edge.getSurfLeft().getGeometry()==geom)
            return edge.getSurfLeft();
        return edge.getSurfRight();
    }

    private void prepareRotation(Geometry selectedSecond) {
        OrigamiSurface3D surf = getSurface3DFromGeometry(selectedSecond);
        if (surf==null)
           return;
        
        //setRotationHinge(edge.getCenter().subtract(surf.getCenter()));//, fromPoint.subtract(toPoint).normalize(), 0, 60000f);
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
        init(edge);
        
        /*rot = new Matrix3f();
        setRotationAxe(edge.getFromPoint().subtract(edge.getToPoint()).normalize());*/
        rotateOpen(edge.getSurfLeft().getGeometry());
        rotateClose(edge.getSurfLeft().getGeometry());
        rotateClose(edge.getSurfRight().getGeometry());
        rotateOpen(edge.getSurfRight().getGeometry());

    }

}
