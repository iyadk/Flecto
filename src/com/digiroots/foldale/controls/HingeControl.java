/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.controls;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import com.digiroots.training.graph.model3d.OrigamiEdge3D;
import com.digiroots.training.graph.model3d.OrigamiShape3D;
import com.digiroots.training.graph.model3d.OrigamiSurface3D;

/**
 *
 * @author khaddam
 */
public class HingeControl extends AbstractControl {

   OrigamiEdge3D edge;
   OrigamiShape3D shape;
   public HingeControl(OrigamiShape3D shape) {
       this.shape = shape;
   }

   private void resetRelatedEdges(TreeMap<String, OrigamiSurface3D> relatedSurfaces){
        for (OrigamiSurface3D su: relatedSurfaces.values()){
            for (OrigamiEdge3D e: su.getRelatedEdges()){
                if (e==edge)
                    continue;
                e.setCoordUpdated(false);
            }
        }
   }
   private void rotate(Spatial s, float angle, int direction){
       if (angle==0)
           return;
       if (s!=edge.getSurfLeft().getGeometry()&& s!=edge.getSurfRight().getGeometry())
           return;
       TreeMap<String, OrigamiSurface3D> relatedSurfaces;
       
       /* Rotation: 
                    L               R
           Close    Rot+(angle)  Rot-(-1*angle)
           Open     Rot-(-1*angle)  Rot+(angle)
       *direction convention: open(1), close(-1)
       */
       int hingeTypeFactor =1;
       if (edge.isMountain())
           hingeTypeFactor=-1;
       OrigamiSurface3D selectedSurface;
       if (s==edge.getSurfLeft().getGeometry()){
          relatedSurfaces = edge.getLeftSurfaceMap();
          selectedSurface = edge.getSurfLeft();
          angle= -1*hingeTypeFactor*direction*angle;

       }
       else {    
          relatedSurfaces = edge.getRightSurfaceMap();
          selectedSurface = edge.getSurfRight();  
          angle= hingeTypeFactor*direction*angle;
       }
       
       resetRelatedEdges(relatedSurfaces);
       updateSpatial(edge, angle, selectedSurface, relatedSurfaces.values());

   }
    private void updateSpatial(OrigamiEdge3D edge, float radStep, OrigamiSurface3D s, Collection<OrigamiSurface3D> relatedSurfaces){
        shape.rotateShape(edge, radStep, s, relatedSurfaces);        
   }

    public void rotateClose(Geometry selectedSecond) {
        float angle = edge.closeAngle();
        rotate(selectedSecond, angle, -1);

/*        for (int i=0; i<1; i++){
            if (edge.closeAngle())
                rotate(selectedSecond, edge.getAngleStep(), -1);
        }*/
    }

    public void rotateOpen(Geometry selectedSecond) {
        float angle = edge.openAngle();
        rotate(selectedSecond, angle, 1);
/*        for (int i=0; i<1; i++){
            if (edge.openAngle())
                rotate(selectedSecond, edge.getAngleStep(), 1);
        }*/
    }

    public void setEdge(OrigamiEdge3D edge2) {
        this.edge = edge2;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }    

    public OrigamiEdge3D getEdge() {
        return edge;    
    }
}
