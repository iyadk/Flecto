/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.digiroots.foldale.appStates.SimulatorAppState;

/**
 *
 * @author khaddam
 */
public abstract class Fold extends AbstractControl {
    protected SimulatorAppState appState;
    public static float STEP = 0.01f;
    public String leftNodeName;
    //public String rightNodeName;
    private Node left;
    //private Node right;
    private Node rootNode; 
    public float degree=0;
    
    protected int TotalMoments = 1000;
    protected int currentMoment  = 0;

    public Fold(SimulatorAppState appState){
        this.appState = appState;
    }    

    /*public Fold(Node left, Node right) {
        this.leftNodeName = left.getName();
        //this.rightNodeName = right.getName();
        this.left = left;
        //this.right = right;
    }*/

    /**
     *
     */
    public abstract void rotateClose();
    public abstract void rotateOpen();
    public abstract float getMinDegree();
    public abstract float getMaxDegree();
    
    protected void doRotate(){
        float step = (getMaxDegree()-getMinDegree())/TotalMoments;
        degree = getMinDegree() + currentMoment *step;
        Quaternion q= new Quaternion();
        q.fromAngleAxis( degree , Vector3f.UNIT_Z);
        getLeft().setLocalRotation( q );
    }
    public Node getLeft(){
        
        return (Node)spatial;//.getChild(leftNodeName);
    }
    /*public void attachToNode(Node rootNode) {
        this.rootNode = rootNode;

        rootNode.attachChild(left);
        //rootNode.attachChild(right);
    }*/
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
        @Override
    protected void controlUpdate(float tpf) {
    }
}
