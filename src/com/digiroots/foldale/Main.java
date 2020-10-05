package com.digiroots.foldale;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

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
        Simulator app = new Simulator();
        app.setSettings(settings);
        app.setShowSettings(false);
        
        //app.setDisplayFps(false); //Hide StatView HUDs. Both lines.
        //app.setDisplayStatView(false);
        
        app.start();
/*        Main app = new Main();
        app.start();
*/
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
