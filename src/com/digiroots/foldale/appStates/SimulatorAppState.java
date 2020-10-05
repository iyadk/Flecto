/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Plane;
import com.jme3.math.Plane.Side;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import javax.swing.JFrame;
import com.digiroots.foldale.Simulator;
import com.digiroots.foldale.controls.Fold;
import com.digiroots.foldale.controls.MountainFold;
import com.digiroots.foldale.controls.VallyFold;
import com.digiroots.foldale.gui.ComponentImageCapture;
import com.digiroots.foldale.gui.FrameAnalyser;

/**
 *
 * @author khaddam
 */

public class SimulatorAppState extends AbstractAppState {
    private Node rootNode;
    private Simulator app;
    private AssetManager assetManager;  
    JFrame GUIFrame;
    TreeMap<String, String> surfaceComponentMap = new TreeMap<String, String>();
    FrameAnalyser frameAnalyser = new FrameAnalyser();
    public void addMapping(String surface, String componentName){
        surfaceComponentMap.put(surface, componentName);
    }

    public SimulatorAppState(JFrame GUIFrame) {
        this.GUIFrame = GUIFrame;
    }

    public void initialize(AppStateManager stateManager,
            Application app) {
        this.app = (Simulator) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();

        Box b = new Box(Vector3f.ZERO, 1, 0.1f, 1);
        Geometry geom0 = createBoxGeometry(b);
        Node node0 = new Node("node" + nodeCount++);
        addMapping(node0.getName(), "Main GUI");
        node0.attachChild(geom0);
        rootNode.attachChild(node0);

//        Fold fold1 = makeMountainFold(node0);
        Node node1 = makeVallyFold(node0);
        addMapping(node1.getName(), "Tree");

//        Fold fold2 = makeMountainFold((Node)rootNode.getChild("node1"));
        Node node2 = makeVallyFold((Node) rootNode.getChild("node1"));
        addMapping(node2.getName(), "Text Area");

//        Fold fold3 = makeMountainFold((Node)rootNode.getChild("node2"));
        Node node3 = makeVallyFold((Node) rootNode.getChild("node2"));
    }

    private static int nodeCount = 0;
    public Node makeVallyFold(Node node) {
        Spatial s = node.getChild("Box");
        Geometry g = (Geometry) s;
        Box b = (Box) g.getMesh();
        Box b1 = new Box(Vector3f.ZERO, b.getXExtent() / 2, b.getYExtent(), b.getZExtent());
        Box b2 = new Box(Vector3f.ZERO, b.getXExtent() / 2, b.getYExtent(), b.getZExtent());
        Geometry geom1 = createBoxGeometry(b1);
        Geometry geom2 = createBoxGeometry(b2);
        node.attachChild(geom1);
        Vector3f translation = g.getLocalTranslation();
        geom1.setLocalTranslation(translation.add(b1.getXExtent(), 0, 0));
        node.detachChild(g);
        Node left = new Node("node" + nodeCount++);
        left.attachChild(geom2);
        left.move(translation.add(0, b.getYExtent(), 0));
        geom2.setLocalTranslation(-1 * b1.getXExtent(), -1 * b1.getYExtent(), 0);
        Fold fold = new VallyFold(this);//new VallyFold(left, null);
        left.addControl(fold);
        node.attachChild(left);

        //fold.attachToNode(node);
        return left;
    }

    public Node makeMountainFold(Node node) {
        Spatial s = node.getChild("Box");
        Geometry g = (Geometry) s;
        Box b = (Box) g.getMesh();
        Box b1 = new Box(Vector3f.ZERO, b.getXExtent() / 2, b.getYExtent(), b.getZExtent());
        Box b2 = new Box(Vector3f.ZERO, b.getXExtent() / 2, b.getYExtent(), b.getZExtent());
        Geometry geom1 = createBoxGeometry(b1);
        Geometry geom2 = createBoxGeometry(b2);
        node.attachChild(geom1);
        Vector3f translation = g.getLocalTranslation();
        geom1.setLocalTranslation(translation.add(b1.getXExtent(), 0, 0));
        node.detachChild(g);
        Node left = new Node("node" + nodeCount++);
        left.attachChild(geom2);
        left.move(translation.add(0, -1 * b.getYExtent(), 0));
        geom2.setLocalTranslation(-1 * b1.getXExtent(), 1 * b1.getYExtent(), 0);

        Fold fold = new MountainFold(this);//left, null);
        left.addControl(fold);
        node.attachChild(left);

        //fold.attachToNode(node);
        return left;
    }

    private Geometry createBoxGeometry(Box b) {
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        Texture tex = createTexture(mat, GUIFrame.getContentPane());
        geom.setMaterial(mat);
        /**
         * An unshaded textured cube. Uses texture from jme3-test-data library!
         */
        return geom;
    }

    private Texture createTexture(Material mat,Component guiComponent) {
        //try {
            //Texture texture = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
            int width = GUIFrame.getContentPane().getWidth();
            int height = GUIFrame.getContentPane().getHeight();
            BufferedImage imageBuffer = ComponentImageCapture.getScreenShot(guiComponent, width, height);//ImageIO.read(new File("C:\\OldLaptop\\Academic\\Masters\\Masters Damascus 2015\\WesleyShillingford\\screenshot.png"));
            ComponentImageCapture.saveImage(guiComponent.getName().replaceAll(" ", "_"), imageBuffer);
            /*Image newImg = imageBuffer.getScaledInstance(
                      imageBuffer.getWidth(null)/2,
                      imageBuffer.getHeight(null)/2,
                      Image.SCALE_SMOOTH );*/
            AWTLoader awtL = new AWTLoader();

            com.jme3.texture.Image imgJME = awtL.load(imageBuffer, true);
            
            
            Texture2D imgTex = new Texture2D(imgJME);
            mat.setTexture("ColorMap", imgTex);
            return imgTex;
        /*} catch (IOException ex) {
            Logger.getLogger(SimulatorAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;*/
    }
     
    
    
    @Override
    public void cleanup() {
        rootNode.detachChildNamed("playerNode");
        rootNode.detachChildNamed("towerNode");
        rootNode.detachChildNamed("creepNode");
    }    
    public AssetManager getAssetManager(){
        return assetManager;
    }
    
    public void refresh(){
        for (int i=0; i<nodeCount; i++){
            Node node = (Node)rootNode.getChild("node"+i);
            if (node!=null){
                Spatial s = node.getChild("Box");
                Geometry g = (Geometry) s;
                refresh(g);
            }
        }
    }

    public void refresh(Geometry g) {
        Box b = (Box) g.getMesh();

        Node node = g.getParent();
        String surfaceName = node.getName();
        if (surfaceName==null)
            return;
        
        //System.out.println("Surface dim [x,z]:["+b.getXExtent()+","+b.getZExtent()+"]");
        String componentName = surfaceComponentMap.get(surfaceName);
        if (componentName==null)
            return;
        frameAnalyser.analyse(GUIFrame);
        Component comp = frameAnalyser.getAnalysisResult().get(componentName);
        if (comp==null){
            System.out.println("component name ["+componentName+"] is not found in the GUI");
            return;
        }
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = createTexture(mat, comp);
        g.setMaterial(mat);
        //FrameAnalyser.analyse(GUIFrame);
    }
}
