/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

/**
 *
 * @author khaddam
 */
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class BoneTest extends SimpleApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Flecto Simulator");
        settings.setSettingsDialogImage("Interface/splashscreen.png");
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        settings.setResolution(modes[0].getWidth(), modes[0].getHeight());
        settings.setFrequency(modes[0].getRefreshRate());
        //settings.setDepthBits(modes[0].getBitDepth());
        settings.setBitsPerPixel(modes[0].getBitDepth());
        //settings.setFullscreen(device.isFullScreenSupported());
        BoneTest t = new BoneTest();
        t.setSettings(settings);
        t.setShowSettings(false);
        t.start();
    }

    
    Geometry surface;
    Vector3f from;
    Vector3f to;
    Vector3f originalTranslation;
    Node surfaceNode;
    @Override
    public void simpleInitApp() {
        createScene(rootNode);
        Vector3f center = new Vector3f(0,0,0);
        Vector3f hinge1From = (new Vector3f(2,0,0)).add(center);
        Vector3f hinge1To = (new Vector3f(2,2,0)).add(center);
        from = hinge1From;
        to = hinge1To;
       /* Vector3f hinge2From = (new Vector3f(-2,-1,0)).add(center);
        Vector3f hinge2To = (new Vector3f(2,-1,0)).add(center);*/
        rootNode.attachChild(createSphere(64,25,1));
        rootNode.attachChild(createLine("h1",hinge1From, hinge1To));
                
        surface = createGeometry("nameofgeom");
        rootNode.attachChild(createGeometry(1,1,0));
        surface.setLocalTranslation(new Vector3f(1,1,0));
        rootNode.attachChild(surface);
        originalTranslation = new Vector3f(surface.getLocalTranslation());
        angle = FastMath.HALF_PI;
        rotateOnAxe(from.subtract(to), from.add(to).divide(2), angle, surface);        
        Quaternion quat = new Quaternion( surface.getLocalRotation());
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        surface.setLocalRotation(quat);
        //surface.setLocalTranslation(Vector3f.ZERO);
        //Vector3f original2D = quat.mult(contactPoint.subtract(geom.getLocalTranslation()));
        //rotateOnAxe(to.subtract(from), from.add(to).divide(2), FastMath.HALF_PI, surface);        

        //rotateOnAxe(hinge1From.subtract(hinge1To), hinge1From.add(hinge1To).divide(2), FastMath.HALF_PI, surface);              
    }

    float angle=0;
    float step = FastMath.HALF_PI/20000;
    @Override
    public void simpleUpdate(float tpf){
        angle += step;
        //rotateOnAxe(from.subtract(to), from.add(to).divide(2), angle, surface);        
        
    }
    private void rotateOnAxe(Vector3f axis, Vector3f theHinge, float rad, Spatial s){
        Matrix3f absRot = new Matrix3f();
        Spatial geom = s;        

        Vector3f lockAxis = axis.normalize();
        Vector3f hinge = new Vector3f(theHinge.subtract(originalTranslation));
        Vector3f absRef = new Vector3f(hinge);
        
        absRot.fromAngleNormalAxis(rad, lockAxis);
        absRef.set(hinge);
        absRot.mult(absRef, absRef);
        Quaternion absQuat = new Quaternion();
        absQuat.apply(absRot);
        geom.setLocalRotation(absRot);

        //Vector3f originalTranslation = new Vector3f(geom.getLocalTranslation());//.subtract(s.getOriginalTranslation());
        Matrix3f relRot = new Matrix3f();
        relRot.fromAngleNormalAxis(step, lockAxis);
        Quaternion relQuat = new Quaternion();
        relQuat.apply(relRot);
        //hinge = new Vector3f(hinge.subtract(originalTranslation));
        //absRef = new Vector3f(absRef.subtract(originalTranslation));
        
        System.out.println(geom.getLocalTranslation()+","+absRef+","+hinge);
        //geom.rotate(relQuat);
        
        geom.setLocalTranslation(originalTranslation.subtract(absRef.subtract(hinge)));                
    }     
    private void createScene(Node rootNode) {
        rootNode.attachChild(createArrow(ColorRGBA.Blue, Vector3f.UNIT_Z));
        rootNode.attachChild(createArrow(ColorRGBA.Green, Vector3f.UNIT_Y));
        rootNode.attachChild(createArrow(ColorRGBA.Red, Vector3f.UNIT_X));   
        rootNode.attachChild(createGeometry(1,0,0));
        rootNode.attachChild(createGeometry(2,0,0));
        rootNode.attachChild(createGeometry(3,0,0));
        rootNode.attachChild(createGeometry(0,1,0));
        rootNode.attachChild(createGeometry(0,2,0));
        rootNode.attachChild(createGeometry(0,3,0));
        rootNode.attachChild(createGeometry(0,0,1));
        rootNode.attachChild(createGeometry(0,0,2));
        rootNode.attachChild(createGeometry(0,0,3));        
    }

    private Geometry createArrow(ColorRGBA color, Vector3f direction) {
        Arrow arrow = new Arrow(direction.mult(4));
        //arrow.setLineWidth(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Geometry g = new Geometry("arrow", arrow);
        g.setMaterial(mat);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setLineWidth(3);
        return g;
    }
    private Node createSurface(Vector3f center, Vector3f hinge1From, Vector3f hinge1To, Vector3f hinge2From, Vector3f hinge2To){
        Node node = new Node("s1");
        Geometry geom = createGeometry("surface");
        node.setLocalTranslation(center);
        node.attachChild(geom);
        Geometry hinge1 = createLine("h1", hinge1From.subtract(center), hinge1To.subtract(center));
        node.attachChild(hinge1);
        /*Geometry hinge2 = createLine("h2", hinge2From.subtract(center), hinge2To.subtract(center));
        node.attachChild(hinge2);
        */
        return node;
    }
    boolean alternateColor = false;
    private Geometry createLine(String name, Vector3f from, Vector3f to){
        Line l = new Line(from, to);
        Geometry geom = new Geometry(name, l);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        if (alternateColor)
            mat2.setColor("Color", ColorRGBA.Orange);
        else
            mat2.setColor("Color", ColorRGBA.Green);
        alternateColor = !alternateColor;

                    
        mat2.getAdditionalRenderState().setLineWidth(3);
        
        geom.setMaterial(mat2);
        
        return geom;

    }
    private Geometry createGeometry(String name){
        Box b = new Box(1f, 1f, 0);
        Geometry geom = new Geometry(name, b);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Pink);
        geom.setMaterial(mat2);
        //geom.setLocalTranslation(center.x, center.y, center.z);
        return geom;
    }   
    
        private Geometry createGeometry(float x, float y, float z){
        Box b = new Box(0.05f, 0.05f, 0.05f);
        Geometry geom = new Geometry("Box", b);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat2);
        geom.setLocalTranslation(x, y, z);
        return geom;
    }

    private Geometry createSphere(int samples, int radiusSamles, float angle) {
        Sphere s = new Sphere(samples, radiusSamles, angle);
        Geometry geom = new Geometry("Sphere", s);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.getAdditionalRenderState().setWireframe(true);
        mat2.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat2);
        //geom.setLocalTranslation(x, y, z);
        return geom;

    }
 

}
