/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

/**
 *
 * @author khaddam
 */
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import com.digiroots.foldale.controls.HingeControl;
import com.digiroots.foldale.gui.mapping.Mapper2D;
import com.digiroots.foldale.gui.mapping.TextureInfo;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.Point2D;

public class OrigamiSimulator extends SimpleApplication implements OrigamiHingeEventListener{

    /**
     * @param args
     */
    /*public static void main(String[] args) {
        OrigamiSimulator t = new OrigamiSimulator();
        t.start();
    }*/
    OrigamiShape3D origamiShape3d;
    Mapper2D gui2DMapper = null;
    Node origamiNode = new Node();
  private ChaseCamera chaseCam;

    
    
    public OrigamiSimulator(OrigamiShape3D origamiShape3d) {
        super();
        this.origamiShape3d = origamiShape3d;
        rootNode.attachChild(origamiNode);
        //this.setShowSettings(false);

    }

    public void refresh() {
        //origamiNode = new Node();
        //System.out.println("Add 3d shapes:");
        origamiShape3d.create3DSurfaces();
        origamiShape3d.prepareGeometryAndBones(assetManager);
        origamiShape3d.computeEdges();
        origamiShape3d.loadIfNecessary();
        origamiShape3d.setActiveEdge();

        displayNode();         
    }
    
    public void displayNode(){
        origamiNode.removeFromParent();
        origamiNode = origamiShape3d.getNode(assetManager);
        rootNode.attachChild(origamiNode);
        
        
        
        //origamiShape3d.skeletonControl.getSkeleton().updateWorldVectors();
        //origamiShape3d.skeletonControl.getSkeleton().setBindingPose();

    }

    public void refresh(OrigamiShape3D origamiShape3d, Mapper2D gui2DMapper) {
        if (this.gui2DMapper!=null){
            this.gui2DMapper.exitGUI();
            origamiShape3d.shape.removeOrigamiHingeEventListener(this.gui2DMapper.getHingeEventListener());
        }
        this.gui2DMapper = gui2DMapper;
        origamiShape3d.shape.addOrigamiHingeEventListener(gui2DMapper.getHingeEventListener());
        origamiShape3d.shape.removeOrigamiHingeEventListener(this);//Ensure this is the last event listener to refresh GUI after everything is completed
        origamiShape3d.shape.addOrigamiHingeEventListener(this);
        
        refresh(origamiShape3d);
    }

    public void refresh(OrigamiShape3D origamiShape3d) {
        this.origamiShape3d = origamiShape3d;
        refresh();
    }

    @Override
    public void gainFocus() {
        //System.out.println("gain focus");
        refreshGUI();
        super.gainFocus(); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshGUI(){
        if (gui2DMapper==null || origamiNode==null){
            return;
        }
        for (Spatial surface: origamiNode.getChildren()){
            String surfName = surface.getName();
            if (!origamiShape3d.surfaceLookupById.containsKey(surfName))
                continue;
            TextureInfo textureInfo = gui2DMapper.getTextureImage(surfName);
            if (textureInfo!=null && textureInfo.textureImg!=null){
                BufferedImage textureImg = textureInfo.textureImg;
                //surface.setCullHint(Spatial.CullHint.Never);
                AWTLoader awtL = new AWTLoader();
                com.jme3.texture.Image imgJME = awtL.load(textureImg, true);
                Texture2D imgTex = new Texture2D(imgJME);
                
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setTexture("ColorMap", imgTex);
                
                mat.getAdditionalRenderState().setFaceCullMode(
                        getSurfaceRenderState(textureInfo.hideFront, textureInfo.hideBack)
                        /*RenderState.FaceCullMode.Off*/);
                
                surface.setMaterial(mat);
            }
            if (textureInfo==null){
                //surface.setCullHint(Spatial.CullHint.Always);
            }
            
        }
    }
    private RenderState.FaceCullMode getSurfaceRenderState(boolean hideFront, boolean hideBack) {
        if (hideFront && hideBack)
            return RenderState.FaceCullMode.FrontAndBack;
        if (hideFront)
            return RenderState.FaceCullMode.Front;
        if (hideBack)
            return RenderState.FaceCullMode.Back;
        return RenderState.FaceCullMode.Off;
    }    

    private void setupChaseCamera(){
        flyCam.setEnabled(false);
        rootNode.attachChild(createChasedObject(CHASEOBJ_NAME));

        chaseCam = new ChaseCamera(cam, rootNode.getChild(CHASEOBJ_NAME), inputManager);
        chaseCam.setUpVector(Vector3f.UNIT_Y);
        chaseCam.setMaxVerticalRotation(FastMath.PI);
        chaseCam.setDefaultHorizontalRotation(FastMath.HALF_PI);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(0f));
        chaseCam.setMinDistance(0.01f);
        chaseCam.setZoomSensitivity(0.5f);
        chaseCam.setDragToRotate(true);
        chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addMapping(MAPPING_S, TRIGGER_S);
        inputManager.addMapping(MAPPING_Z, TRIGGER_Z);
        inputManager.addMapping(MAPPING_A, TRIGGER_A);
        inputManager.addMapping(MAPPING_E, TRIGGER_E);
        inputManager.addMapping(MAPPING_Q, TRIGGER_Q);
        inputManager.addMapping(MAPPING_D, TRIGGER_D);
    }
    private void setupFlyCam(){
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10f);
    }
    private final String CHASEOBJ_NAME = "ChasingObject";
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        //setupFlyCam();
        setupChaseCamera();
        inputManager.setCursorVisible(true);
        createScene();
        inputManager.setCursorVisible(true);
        inputManager.addMapping(MAPPING_SELECT, TRIGGER_SELECT);
        inputManager.addMapping(MAPPING_ALT, TRIGGER_ALT);
        inputManager.addMapping(MAPPING_TAB, TRIGGER_TAB);
        inputManager.addMapping(MAPPING_0, TRIGGER_0);
        inputManager.addMapping(MAPPING_ROTATE_CLOSE, TRIGGER_ROTATE_CLOSE);
        inputManager.addMapping(MAPPING_ROTATE_OPEN, TRIGGER_ROTATE_OPEN);
        inputManager.addMapping(MAPPING_F1, TRIGGER_F1);
        inputManager.addMapping(MAPPING_CLICK, TRIGGER_CLICK);
        
        inputManager.addListener(actionListener, new String[]{MAPPING_SELECT, 
            MAPPING_ALT, MAPPING_TAB, MAPPING_0, MAPPING_F1, MAPPING_S, 
            MAPPING_Z, MAPPING_A, MAPPING_E, MAPPING_Q, MAPPING_D, MAPPING_CLICK});
        inputManager.addListener(analogListener, new String[]{ MAPPING_ROTATE_CLOSE, MAPPING_ROTATE_OPEN});
        

    }

    private final  String X_ARROW_NAME = "ArrowX" ;
    private final  String Y_ARROW_NAME = "ArrowY" ;
    private final  String Z_ARROW_NAME = "ArrowZ" ;
    private void createScene() {
        rootNode.attachChild(createArrow(ColorRGBA.Blue, Vector3f.UNIT_Z, Z_ARROW_NAME));
        rootNode.attachChild(createArrow(ColorRGBA.Green, Vector3f.UNIT_Y, Y_ARROW_NAME));
        rootNode.attachChild(createArrow(ColorRGBA.Red, Vector3f.UNIT_X, X_ARROW_NAME));
    }

    private Geometry createArrow(ColorRGBA color, Vector3f direction, String name) {
        Arrow arrow = new Arrow(direction.mult(4));
        //arrow.setLineWidth(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Geometry g = new Geometry(name, arrow);
        g.setMaterial(mat);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setLineWidth(1);
        return g;
    }   
    private Geometry createChasedObject(String name) {
        Box b = new Box(0.001f, 0.001f, 0.001f);
        //arrow.setLineWidth(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Geometry g = new Geometry(name, b);
        g.setCullHint(Spatial.CullHint.Always);
        g.setMaterial(mat);
        return g;
    }   
        
    
    private final static Trigger TRIGGER_SELECT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static Trigger TRIGGER_CLICK = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    private final static Trigger TRIGGER_ALT = new KeyTrigger(KeyInput.KEY_LMENU);
    private final static Trigger TRIGGER_TAB = new KeyTrigger(KeyInput.KEY_TAB);
    private final static Trigger TRIGGER_0 = new KeyTrigger(KeyInput.KEY_0);
    private final static Trigger TRIGGER_F1 = new KeyTrigger(KeyInput.KEY_F1);
    private final static Trigger TRIGGER_ROTATE_CLOSE = new KeyTrigger(KeyInput.KEY_DOWN);
    private final static Trigger TRIGGER_ROTATE_OPEN = new KeyTrigger(KeyInput.KEY_UP);
    private final static Trigger TRIGGER_Z = new KeyTrigger(KeyInput.KEY_Z);
    private final static Trigger TRIGGER_S = new KeyTrigger(KeyInput.KEY_S);
    private final static Trigger TRIGGER_Q = new KeyTrigger(KeyInput.KEY_Q);
    private final static Trigger TRIGGER_D = new KeyTrigger(KeyInput.KEY_D);
    private final static Trigger TRIGGER_A = new KeyTrigger(KeyInput.KEY_A);
    private final static Trigger TRIGGER_E = new KeyTrigger(KeyInput.KEY_E);

    private final static String MAPPING_SELECT = "Select Surface";
    private final static String MAPPING_CLICK = "Click Surface";
    private final static String MAPPING_ALT = "ALT";
    private final static String MAPPING_TAB = "TAB";
    private final static String MAPPING_0 = "0";
    private final static String MAPPING_F1 = "F1";
    private final static String MAPPING_ROTATE_CLOSE = "Rotate Close";
    private final static String MAPPING_ROTATE_OPEN = "Rotate Open";    
    private final static String MAPPING_S = "ChaseDown";
    private final static String MAPPING_Z = "ChaseUp";
    private final static String MAPPING_Q = "ChaseLeft";
    private final static String MAPPING_D = "ChaseRight";
    private final static String MAPPING_A = "MoveCamUp";
    private final static String MAPPING_E = "MoveCamDown";
    Geometry selectedFirst=null;
    Geometry selectedSecond=null;
    HingeControl selectedHingeController = null;
    boolean ctrlIsPressed = false;
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            //System.out.println(name);
            if (name.equals(MAPPING_ALT) ){//&& isPressed) {
               ctrlIsPressed = isPressed; 
               /*if (isPressed){
                    chaseCam.setDragToRotate(false);
               }
               else
                    chaseCam.setDragToRotate(true);*/
            } 
            if (name.equals(MAPPING_TAB) && isPressed){
                origamiShape3d.showActiveEdges=!origamiShape3d.showActiveEdges;
                origamiShape3d.computeEdges();
               ;//toggle show/hide hinge selector shortcuts
            } 
            if (name.equals(MAPPING_0) && isPressed){
                if (rootNode.getChild(X_ARROW_NAME).getCullHint()==Spatial.CullHint.Always){
                    rootNode.getChild(X_ARROW_NAME).setCullHint(Spatial.CullHint.Never);
                    rootNode.getChild(Y_ARROW_NAME).setCullHint(Spatial.CullHint.Never);
                    rootNode.getChild(Z_ARROW_NAME).setCullHint(Spatial.CullHint.Never);
                }
                else{
                    rootNode.getChild(X_ARROW_NAME).setCullHint(Spatial.CullHint.Always);
                    rootNode.getChild(Y_ARROW_NAME).setCullHint(Spatial.CullHint.Always);
                    rootNode.getChild(Z_ARROW_NAME).setCullHint(Spatial.CullHint.Always);
                    
                }
               ;//toggle hide/show coordinate system
            } 
            
            if (name.equals(MAPPING_F1) && isPressed){
                dumpInfo();
            }
            if (name.equals(MAPPING_E) && isPressed){
                Vector3f v = chaseCam.getLookAtOffset();
                chaseCam.setLookAtOffset(v.subtract(Vector3f.UNIT_Y));
            }
            if (name.equals(MAPPING_A) && isPressed){
                Vector3f v = chaseCam.getLookAtOffset();
                chaseCam.setLookAtOffset(v.add(Vector3f.UNIT_Y));
            }
            if (name.equals(MAPPING_S) && isPressed){
                rootNode.getChild(CHASEOBJ_NAME).move(0, -0.5f, 0);
            }
            if (name.equals(MAPPING_Z) && isPressed){
                rootNode.getChild(CHASEOBJ_NAME).move(0, 0.5f, 0);
            }
            if (name.equals(MAPPING_Q) && isPressed){
                rootNode.getChild(CHASEOBJ_NAME).move(-0.5f, 0, 0);
            }
            if (name.equals(MAPPING_D) && isPressed){
                rootNode.getChild(CHASEOBJ_NAME).move(0.5f, 0, 0);
            }
            if ((name.equals(MAPPING_SELECT)||name.equals(MAPPING_CLICK)) && !isPressed) {
                Geometry selectedSurface = getClickedGeometry(inputManager.getCursorPosition(), cam, rootNode);
                if (selectedSurface!=null){
                    selectedFirst = selectedSecond;
                    selectedSecond = selectedSurface;
                    if (selectedFirst!=null && selectedSecond!=null ){
                        //find selected hinge
                        Node origamiNode = selectedSurface.getParent();
                        //Node origamiNode = rootNode.getChild("shapeNode");
                        HingeControl hinge = origamiNode.getControl(HingeControl.class);

                        if (hinge!=null){
                            OrigamiEdge3D selectedEdge = origamiShape3d.getEdge(selectedFirst.getName(), selectedSecond.getName());                            
                            selectedHingeController = null;
                            if (selectedEdge!=null){
                                    boolean couldSelectEdge = origamiShape3d.setActiveEdge(selectedEdge);
                                    if (couldSelectEdge){
                                        selectedHingeController = hinge;
                                        System.out.println("Hinge "+selectedEdge.getId()+" is selected with angle"+ selectedEdge.currAngle);
                                    }
                                }
                        }                        
                    }                    
                }
            }
        }
    };
    
   private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            //System.out.println("intensity:"+intensity);                     
            if (name.equals(MAPPING_ROTATE_CLOSE) ) {
                if (selectedHingeController != null) {
                    selectedHingeController.rotateClose(selectedSecond);
                    origamiShape3d.computeEdges();
                }
            }
            if (name.equals(MAPPING_ROTATE_OPEN) ) {
                    if (selectedHingeController != null) {
                        selectedHingeController.rotateOpen(selectedSecond);
                        origamiShape3d.computeEdges();
                    }
            }
        }
    };
    Vector3f contactPoint;
    private Geometry getClickedGeometry(Vector2f click2d, Camera cam, Node theRootNode) {
        CollisionResults results = new CollisionResults();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
        Ray ray = new Ray(click3d, dir);
        theRootNode.collideWith(ray, results);
        if (results.size() > 0) {
            contactPoint = results.getClosestCollision().getContactPoint();
            Geometry geom = results.getClosestCollision().getGeometry();
            System.out.println(geom.getName()+" is clicked");
            String geomName = geom.getName();
            if (geomName.contains("#")){// This is a hinge selection shortcut
                String[] surfs = geomName.split("#");
                selectedSecond = (Geometry)origamiNode.getChild(surfs[1]);
                return (Geometry)origamiNode.getChild(surfs[0]);
            }
            OrigamiSurface3D surf = origamiShape3d.surfaceLookupById.get(geom.getName());
            if (surf==null){
                System.out.println("Not a surface");
                return null;
            }
            //get any point from the surface
            Point3D surfPoint = surf.vertices3D[0];
            Point3D surfPoint2 = surf.vertices3D[1];
            Vector3f v1 = surfPoint2.getVertex().subtract(surfPoint.getVertex());
            Vector3f v2 = contactPoint.subtract(surfPoint.getVertex());
            float angle = v1.normalize().angleBetween(v2.normalize());
            //System.out.println("p1="+surfPoint.getVertex()+", p2="+surfPoint2.getVertex()+",c="+contactPoint+"angle="+angle);
            //System.out.println("p1 orig="+surfPoint.getOrigin()+",p2 orig"+surfPoint2.getOrigin());
            Vector3f vOrig = surfPoint2.getOrigin().subtract(surfPoint.getOrigin()).normalize();
            Matrix3f relRot = new Matrix3f();
            relRot.fromAngleNormalAxis(angle, Vector3f.UNIT_Z);
            relRot.mult(vOrig, vOrig);
            //System.out.println("Rotated="+vOrig);
            Vector3f original2D = vOrig.scaleAdd(v2.length(), surfPoint.getOrigin());

            /*Vector3f orig = surfPoint.getOrigin();
            Vector3f original2D = orig.subtract(surfPoint.getVertex()).add(contactPoint);
            */
            //Vector3f original2D = contactPoint.subtract(surfPoint.getVertex()).add(orig);
            Point2D point2D = origamiShape3d.transformer.transform3DCoordinates(original2D);       
            if (ctrlIsPressed){
                origamiShape3d.setClicked3DPoint(point2D, geom.getName());
                ctrlIsPressed = false;
            }
            System.out.println("Contact Point="+contactPoint+", tranformed 3d:"+original2D+"in 2d:"+point2D);
            return geom;
        }
        else{
            refreshGUI();
        }
        return null;
    }    

    @Override
    public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
        refreshGUI();
    }

    @Override
    public void onMouseClickedEvent(Point2D point, String surfaceName) {
        refreshGUI();
    }
    public void dumpInfo(){
        System.out.println("Camera location: "+cam.getLocation());
        if (selectedHingeController!=null){
            OrigamiEdge3D edge = selectedHingeController.getEdge();
            edge.getSurfLeft().dumpSurface();
            System.out.println();
            edge.getSurfRight().dumpSurface();
            System.out.println();
            //edge.getSu
        }
    }


}
