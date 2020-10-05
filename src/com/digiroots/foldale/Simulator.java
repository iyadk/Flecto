/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import com.digiroots.foldale.appStates.SimulatorAppState;
import com.digiroots.foldale.controls.Fold;
import com.digiroots.foldale.gui.ComponentImageCapture;

/**
 *
 * @author khaddam
 */
public class Simulator extends SimpleApplication {
 
    private final static Trigger TRIGGER_SELECT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static Trigger TRIGGER_ROTATE_UP = new KeyTrigger(KeyInput.KEY_UP);
    private final static Trigger TRIGGER_ROTATE_DOWN = new KeyTrigger(KeyInput.KEY_DOWN);
//    private final static String MAPPING_COLOR = "Toggle Color";
//    private final static String MAPPING_ROTATE = "Rotate";
    private final static String MAPPING_SELECT = "Select Surface";
    private final static String MAPPING_ROTATE_UP = "Rotate Up";
    private final static String MAPPING_ROTATE_DOWN = "Rotate Down";
    private JFrame GUIFrame;
    
    public Simulator(){
        GUIFrame = ComponentImageCapture.execute();
        while (!GUIFrame.isVisible()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public void simpleInitApp() {
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        inputManager.addMapping(MAPPING_SELECT, TRIGGER_SELECT);
        inputManager.addMapping(MAPPING_ROTATE_UP, TRIGGER_ROTATE_UP);
        inputManager.addMapping(MAPPING_ROTATE_DOWN, TRIGGER_ROTATE_DOWN);
        inputManager.addListener(actionListener, new String[]{MAPPING_SELECT});
        inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE_UP, MAPPING_ROTATE_DOWN});

        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 5, 5));
        cam.lookAt(Vector3f.ZERO, new Vector3f(0, 1, 0));
        //Quaternion ROLL045  = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
        //cam.setRotation(ROLL045);
        SimulatorAppState state = new SimulatorAppState(GUIFrame);
        stateManager.attach(state);
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    Fold selected=null;
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_SELECT) && !isPressed) {
                Geometry selectedTower = getClickedGeometry(inputManager.getCursorPosition(), cam, rootNode);
                if (selectedTower!=null){
                    Fold tc = selectedTower.getParent().getControl(Fold.class);//Get control from the parent node
                    selected = tc;
                    //if (selectedTower instanceof Box)
                    System.out.println("Selected "+selectedTower.getParent().getName());
                    SimulatorAppState appState = stateManager.getState(SimulatorAppState.class);
                    if (appState != null) {
                        appState.refresh(selectedTower);
                    }
                }

            }
            
        }
        
    
    };
    
   private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            System.out.println("intensity:"+intensity);

            if (name.equals(MAPPING_ROTATE_UP) ) {
                if (selected != null) {
                    selected.rotateClose();                   
                }
            }
            if (name.equals(MAPPING_ROTATE_DOWN) ) {
                    if (selected != null) {
                        selected.rotateOpen();
                    }
            }
            /*if (selected!=null){
                System.out.println("Selected:"+selected.leftNodeName);
            }
            else{
                System.out.println("Selected: nothing");
            } */           
            
        }
    };    
    private Geometry getClickedGeometry(Vector2f click2d, Camera cam, Node theRootNode) {
        CollisionResults results = new CollisionResults();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
        Ray ray = new Ray(click3d, dir);
        //Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        theRootNode.collideWith(ray, results);
        if (results.size() > 0) {
            System.out.println("Contact Point="+results.getClosestCollision().getContactPoint());
            return results.getClosestCollision().getGeometry();
        }
        return null;
    }
}
