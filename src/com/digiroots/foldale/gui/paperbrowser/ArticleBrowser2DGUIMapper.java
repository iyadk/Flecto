/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
com.digiroots.foldale.gui.paperbrowser.ArticleBrowser2DGUIMapper
 */
package com.digiroots.foldale.gui.paperbrowser;

import com.digiroots.foldale.gui.mapping.Capturable;
import com.digiroots.foldale.gui.mapping.Mapper2D;
import com.digiroots.foldale.gui.mapping.TextureInfo;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.OrigamiShape;
import com.digiroots.training.graph.OrigamiSurface;
import com.digiroots.training.graph.Point2D;

/**
 *
 * @author khaddam
 */
public class ArticleBrowser2DGUIMapper implements Mapper2D, OrigamiHingeEventListener{

    TreeMap<String, Component> componentMap = new TreeMap<>();
    TreeMap<String, DefaultMapper> surfMap = new TreeMap<>();
/*    OrigamiHingeEventListener hingeEventListener = new OrigamiHingeEventListener() {//Default listener outsputs events only.
            @Override
            public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
                System.out.println("Event "+event+" is triggered");
            }

            @Override
            public void onMouseClickedEvent(Point2D point, String surfaceName) {
                //shape.clicked3DPoint = point;
            }
        };*/
    public ArticleBrowser2DGUIMapper() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
 
    @Override
    public TextureInfo getTextureImage(String surfaceName) {
        Component c = getComponent(surfaceName);
        if (c==null)
            return null;
        return surfMap.get(surfaceName).getScreenShot();
    }

    public Component getComponent(String surfaceName){
        if (!surfMap.containsKey(surfaceName))
            return null;
        return surfMap.get(surfaceName).getComponent();
    }
    ScientificPaperBrowser swingApp;
    @Override
    public void runGUI(OrigamiShape shape) {
        this.shape = shape;
        swingApp= new ScientificPaperBrowser();
        try {
            Thread.sleep(1l);
        } catch (InterruptedException ex) {
            Logger.getLogger(ArticleBrowser2DGUIMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        swingApp.setVisible(true);
        for (OrigamiSurface surf: shape.getColoredSurfaces()){
            DefaultMapper sMap = new DefaultMapper(surf, shape.ratioX2D, shape.ratioY2D);
            surfMap.put(surf.getId(), sMap);
        }
        DefaultMapper sMap0 = surfMap.get("Surf0");
        sMap0.addComponentAtState(0, swingApp.headerPanel, 0,0, true, false, true, true);
        sMap0.addComponentAtState(1, swingApp.abstractPanel, 0,0, false, true, true, false);
        sMap0.addComponentAtState(2, swingApp.pagePanel,0,0, false, false, false, false);
        DefaultMapper sMapPrev = surfMap.get("BackSurf");
        sMapPrev.addComponentAtState(0, swingApp.headerPanel, 0,0, true, false, true, true);
        sMapPrev.addComponentAtState(1, swingApp.abstractPanel, swingApp.FrameWidth*1/4,0, false, true, true, false);
        sMapPrev.addComponentAtState(2, swingApp.pagePanel,0,0, false, false, false, false);
        DefaultMapper sMap1 = surfMap.get("Surf1");
        sMap1.addComponentAtState(0, swingApp.tocPanel, 0,0, true, false, true, true);
        sMap1.addComponentAtState(1, swingApp.tocPanel, 0,0, false, true, true, false);
        sMap1.addComponentAtState(2, swingApp.pagePanel,0,swingApp.FrameHeight/2, false, false, false, false);
        DefaultMapper sMap2 = surfMap.get("Surf2");
        sMap2.addComponentAtState(0, swingApp.abstractPanel, 0,0, false, false, true, false);//no inverse and no turn 
        sMap2.addComponentAtState(1, swingApp.referencePanel, 0,swingApp.FrameHeight/2, false, true, true, false);//no inverse and  turn
        sMap2.addComponentAtState(2, swingApp.pagePanel,swingApp.FrameWidth/2,swingApp.FrameHeight/2, false, false, false, false);
        DefaultMapper sMap3 = surfMap.get("Surf3");
        sMap3.addComponentAtState(0, swingApp.headerPanel, 0,0, true, false, true, false);
        sMap3.addComponentAtState(1, swingApp.referencePanel, 0,0, false,true, true, false);
        sMap3.addComponentAtState(2, swingApp.pagePanel,swingApp.FrameWidth/2,0, false, false, false, false);
        DefaultMapper sMapNext = surfMap.get("PrevSurf");
        sMapNext.addComponentAtState(0, swingApp.headerPanel, swingApp.FrameWidth*1/4,swingApp.FrameHeight*2/4, true, false, true, false);
        sMapNext.addComponentAtState(1, swingApp.referencePanel, swingApp.FrameWidth*0/4,0, false,true, true, false);
        sMapNext.addComponentAtState(2, swingApp.pagePanel,swingApp.FrameWidth*3/4,0, false, false, false, false);
    }
    
    public class DefaultMapper{
        OrigamiSurface surface;
        Point2D[] boundingPoints;//[0] is the min point, [1] is the max point
        float ratioX;
        float ratioY;
        Dimension dim;  
        
        DefaultMapper(OrigamiSurface surface, float ratioX, float ratioY){
            this.surface = surface;
            this.ratioY = ratioY;
            this.ratioX = ratioX;
            boundingPoints = surface.getBoundingBox();
            dim = new Dimension(Math.round((boundingPoints[1].x-boundingPoints[0].x)*ratioX),
                    Math.round((boundingPoints[1].y-boundingPoints[0].y)*ratioY));
            
            Point2D center = surface.getCenter();
        }
        public Component getComponent(){
            StateInfo info = getStateInfo(swingApp.currentState);            
            return info.panel;
        }        
        public TextureInfo getScreenShot(){
            StateInfo info = getStateInfo(swingApp.currentState);  
            if (info.panel==null)
                return null;
            if (info.panel instanceof Capturable){
                BufferedImage image = ((Capturable)info.panel).getScreenShot();
                if (image==null)
                    return null;
                return new TextureInfo(info.processImage(image), info.hideFace, info.hideBack);
            }
            if (info.panel.getWidth()==0 || info.panel.getHeight()==0)
                return null;
            BufferedImage image = new BufferedImage(info.panel.getWidth(), info.panel.getHeight(), BufferedImage.TYPE_INT_RGB);        
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, info.panel.getWidth(), info.panel.getHeight());
            // call the Component's paint method, using
            // the Graphics object of the image.
            info.panel.paint( g ); // alternately use .printAll(..)
            
            
            return new TextureInfo(info.processImage(image), info.hideFace, info.hideBack);            
        }
        public final int toMyXCoord(int x){
            StateInfo info = getStateInfo(swingApp.currentState);            
            return info.x0 + Math.round((x-boundingPoints[0].x)*ratioX);            
        }
        public final int toMyYCoord(int y){
            StateInfo info = getStateInfo(swingApp.currentState);
            return info.y0 + Math.round((y-boundingPoints[0].y)*ratioY);
        }
        public void paintComponent(Graphics g) {
            int[] x = surface.getXVector();
            int[] y = surface.getYVector();
            for (int i=0; i<x.length; i++){
                x[i] = Math.round((x[i]-boundingPoints[0].x)*ratioX);
                y[i] = Math.round((y[i]-boundingPoints[0].y)*ratioY);
            }
        }
        public StateInfo getStateInfo(int state){
            return stateMap.get(state);
        }
        TreeMap<Integer, StateInfo> stateMap = new TreeMap<>();
        public void addComponentAtState(int state, Component panel, int x0, int y0, boolean flipX, boolean flipY, boolean hideFace, boolean hideBack) {
               stateMap.put(state, new StateInfo(panel, x0, y0, flipX, flipY, hideFace, hideBack));
        }
        public int getXLocationOnScreen(int x){
            return toMyXCoord(x)+getComponent().getLocationOnScreen().x;
        }
        public int getYLocationOnScreen(int y){
            return toMyYCoord(y)+getComponent().getLocationOnScreen().y;
        }
        public class StateInfo{
            Component panel;
            int x0;
            int y0;
            boolean flipX;
            boolean flipY;
            boolean hideFace;
            boolean hideBack;
            public StateInfo(Component panel, int x0, int y0, boolean flipX, boolean flipY,  boolean hideFace, boolean hideBack){
                this.panel = panel;
                this.x0 = x0;
                this.y0 = y0;
                this.flipX = flipX;
                this.flipY = flipY;
                this.hideBack = hideBack;
                this.hideFace = hideFace;
            }
            public BufferedImage processImage(BufferedImage image){
                int w = dim.width;
                if (x0+w>image.getWidth())
                    w = image.getWidth() -x0;
                int h = dim.height;
                if (y0+h>image.getHeight())
                    h = image.getHeight()-y0;
                //System.out.println("Crop image:"+surface.getId()+", x="+image.getWidth()+":"+w+", h="+image.getHeight()+":"+h);
                BufferedImage cropedImage = image.getSubimage(x0, y0, w, h);
                if (flipX){
                    cropedImage = createFlipped(cropedImage);
                }
                if (flipY){
                    cropedImage = createRotated(createFlipped(cropedImage));
                    
                }
                return cropedImage;
                
            }
            private BufferedImage createFlipped(BufferedImage image)
            {
                AffineTransform at = new AffineTransform();
                at.concatenate(AffineTransform.getScaleInstance(1, -1));
                at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
                return createTransformed(image, at);
            }

            private BufferedImage createRotated(BufferedImage image)
            {
                AffineTransform at = AffineTransform.getRotateInstance(
                    Math.PI, image.getWidth()/2, image.getHeight()/2.0);
                return createTransformed(image, at);
            }

            private BufferedImage createTransformed(
                BufferedImage image, AffineTransform at)
            {
                BufferedImage newImage = new BufferedImage(
                    image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = newImage.createGraphics();
                g.transform(at);
                g.drawImage(image, 0, 0, null);
                g.dispose();
                return newImage;
            }

            /*private BufferedImage createInverted(BufferedImage image)
            {
                if (image.getType() != BufferedImage.TYPE_INT_ARGB)
                {
                    image = convertToARGB(image);
                }
                LookupTable lookup = new LookupTable(0, 4)
                {
                    @Override
                    public int[] lookupPixel(int[] src, int[] dest)
                    {
                        dest[0] = (int)(255-src[0]);
                        dest[1] = (int)(255-src[1]);
                        dest[2] = (int)(255-src[2]);
                        return dest;
                    }
                };
                LookupOp op = new LookupOp(lookup, new RenderingHints(null));
                return op.filter(image, null);
            }            
            */
        }
    }
    OrigamiShape shape;

    @Override
    public void exitGUI() {
        swingApp.dispose();
    }

    @Override
    public OrigamiHingeEventListener getHingeEventListener() {
        return this;
    }

    @Override
    public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
        if ("0To1".equalsIgnoreCase(event.name)){
            swingApp.changeState(1);
        }
        else if ("1To0".equalsIgnoreCase(event.name)){
            swingApp.changeState(0);
        }
        else if ("1To2".equalsIgnoreCase(event.name)){
            swingApp.changeState(2);
        }
        else if ("2To1".equalsIgnoreCase(event.name)){
            swingApp.changeState(1);
        }
        else if ("NextPage".equalsIgnoreCase(event.name)){
            swingApp.nextPage();
        }
        else if ("PrevPage".equalsIgnoreCase(event.name)){
            swingApp.prevPage();
        }        //System.out.println("Event "+event+" is triggered from inside Default2DGUIMapper");        
    }

    Robot robot;
    @Override
    public void onMouseClickedEvent(Point2D point, String surfaceName) {
        if (surfaceName==null)
            return;
        if (!surfMap.containsKey(surfaceName))
            return;
        DefaultMapper surfaceMap = surfMap.get(surfaceName);
        OrigamiSurface surface = surfMap.get(surfaceName).surface;        
        if (surface==null)
            return;
        int x = surfMap.get(surfaceName).getXLocationOnScreen(point.x);
        int y = surfMap.get(surfaceName).getYLocationOnScreen(point.y);
        System.out.println("ArticleBrowser2D:: clickedPoint is"+ x+","+y );
        //Do transformation of point to frame coordinates
        //Point2D[] boundingPoints = surface.getBoundingBox();
        //int x = Math.round((point.x-boundingPoints[0].x)*shape.ratioX2D);
        //int y = Math.round((point.y-boundingPoints[0].y)*shape.ratioY2D/*/(boundingPoints[1].y-boundingPoints[0].y)*/);
        //JFrame frame = frameMap.get(surface.getId());
        //Component surfComponent = getComponent(surfaceName);
        //x = x + surfComponent.getLocationOnScreen().x;
        //y = y + surfComponent.getLocationOnScreen().y;
        Point oldPos = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(x, y);
        leftClick();
        //robot.mouseMove(oldPos.x, oldPos.y);
        //rightClick();
        for (DefaultMapper.StateInfo si:surfaceMap.stateMap.values()){
            if (si.panel instanceof Capturable){
                ((Capturable)si.panel).refreshScreenShort();
            }
        }
    }
    private void leftClick()
    {
      robot.mousePress(InputEvent.BUTTON1_MASK);
      robot.delay(200);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      robot.delay(200);
    }
    private void rightClick()
    {
      robot.mousePress(InputEvent.BUTTON3_MASK);
      robot.delay(100);
      robot.mouseRelease(InputEvent.BUTTON3_MASK);
      robot.delay(100);
    }
}
