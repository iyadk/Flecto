/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui.mapping;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.OrigamiShape;
import com.digiroots.training.graph.OrigamiSurface;
import com.digiroots.training.graph.Point2D;

/**
 *
 * @author khaddam
 */
public class Default2DGUIMapper implements Mapper2D, OrigamiHingeEventListener{

    TreeMap<String, Component> componentMap = new TreeMap<>();
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
    public Default2DGUIMapper() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
            
            //Logger.getLogger(Default2DGUIMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private BufferedImage getScreenShot(Component component) {
      return getScreenShot(component, component.getWidth(), component.getHeight());    
  }
   private BufferedImage getScreenShot(Component component, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);        
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint( g ); // alternately use .printAll(..)
        return image;
    }
   
    @Override
    public TextureInfo getTextureImage(String surfaceName) {
        Component c = getComponent(surfaceName);
        if (c==null)
            return null;
        return new TextureInfo(getScreenShot(c), false, false);
    }

    public Component getComponent(String surfaceName){
        return componentMap.get(surfaceName);
    }
    ArrayList<OrigamiSurface> surfaceList;
    TreeMap<String, JFrame> frameMap = new TreeMap<>();

    @Override
    public void runGUI(OrigamiShape shape) {
        this.shape = shape;
        surfaceList = shape.getColoredSurfaces();
        for(OrigamiSurface surface:surfaceList){
            JFrame frame = createFrame(surface);
            frame.setVisible(true);
            frameMap.put(surface.getId(), frame);
        }
    }
    
    public static class DefaultMapperPanel extends JPanel{
        OrigamiSurface surface;
        Point2D[] boundingPoints;//[0] is the min point, [1] is the max point
        float ratioX;
        float ratioY;
        public Image image;
        
        DefaultMapperPanel(OrigamiSurface surface, float ratioX, float ratioY){
            this.setLayout(null);
            this.surface = surface;
            this.ratioY = ratioY;
            this.ratioX = ratioX;
            boundingPoints = surface.getBoundingBox();
            this.setPreferredSize(new Dimension(toMyXCoord(boundingPoints[1].x),toMyYCoord(boundingPoints[1].y)));
            JButton button = new JButton("Click ");
            
            JLabel lblName = new JLabel(surface.getId());
            Point2D center = surface.getCenter();
            lblName.setBounds(toMyXCoord(center.x),toMyYCoord(center.y),50,25);
            button.setBounds(toMyXCoord(center.x),toMyYCoord(center.y)+30,100,25);
            this.add(lblName);            
            //this.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    button.setText(button.getText()+"1");

                }
        });
        }
        public final int toMyXCoord(int x){
            return Math.round((x-boundingPoints[0].x)*ratioX);
        }
        public final int toMyYCoord(int y){
            return Math.round((y-boundingPoints[0].y)*ratioY);
        }
        public void setImage(Image img){
            this.image = img;
            this.removeAll();
            this.revalidate();
            this.repaint();
        }
        @Override
        public void paintComponent(Graphics g) {
                        super.paintComponent(g);            

            if (image!=null){
                g.drawImage(image, 0, 0, this);
                return;
            }
            
            int[] x = surface.getXVector();
            int[] y = surface.getYVector();
            for (int i=0; i<x.length; i++){
                x[i] = Math.round((x[i]-boundingPoints[0].x)*ratioX);
                y[i] = Math.round((y[i]-boundingPoints[0].y)*ratioY);
            }
            g.setColor(surface.getColor());
            g.fillPolygon(x, y, x.length);

            /*int dim = 20;
            g.setColor(Color.ORANGE);
            g.fillOval(0, 0, dim, dim);
            g.setColor(Color.RED);
            g.fillOval(0, height-dim, dim, dim);
            g.setColor(Color.BLUE);
            g.fillOval(width-dim, 0, dim, dim);
            g.setColor(Color.GREEN);
            g.fillOval(width-dim, height-dim, dim, dim);
            */
        }
    }
    OrigamiShape shape;
    MenuItemListener menuItemListener = new MenuItemListener();
    private JFrame createFrame(OrigamiSurface surface) {
        JFrame frame = new JFrame(surface.getId());                
        DefaultMapperPanel panel = new DefaultMapperPanel(surface, shape.ratioX2D, shape.ratioY2D);        
        componentMap.put(surface.getId(), panel);

        frame.add(panel);
        
        //Menu for images
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Tools");
        //menu.setMnemonic(KeyEvent.VK_A);
        //menu.getAccessibleContext().setAccessibleDescription(
        //"The only menu in this program that has menu items");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Set front Image", KeyEvent.VK_F);
        menuItem.setName(surface.getId()+"#Face");
        menuItem.addActionListener(new MenuItemListener());
        menu.add(menuItem);
        /*menuItem = new JMenuItem("Set back Image", KeyEvent.VK_B);
        menuItem.setName(surface.getId()+"#Back");
        menuItem.addActionListener(new MenuItemListener());
        menu.add(menuItem);
        */
        frame.setJMenuBar(menuBar);
        frame.pack();
        return frame;
    }
    class MenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("Done");
            String name = ((JMenuItem)ae.getSource()).getName();
            String arr[] = name.split("#");
            DefaultMapperPanel mapper = (DefaultMapperPanel)componentMap.get(arr[0]);
            ImageFileSurfaceSelector imgFileSelector = new ImageFileSurfaceSelector(mapper);
            imgFileSelector.setVisible(true);            
        }        
    }

    @Override
    public void exitGUI() {
        for (JFrame f: frameMap.values())
            f.dispose();
    }

    @Override
    public OrigamiHingeEventListener getHingeEventListener() {
        return this;
    }

    @Override
    public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
        System.out.println("Event "+event+" is triggered from inside Default2DGUIMapper");        
    }

    Robot robot;
    @Override
    public void onMouseClickedEvent(Point2D point, String surfaceName) {
        OrigamiSurface surface = null;
        if (surfaceName==null)
            return;
        for(OrigamiSurface s:surfaceList){
            if (surfaceName.equalsIgnoreCase(s.getId()))
                surface = s;
        }        
        if (surface==null)
            return;
        //Do transformation of point to frame coordinates
        Point2D[] boundingPoints = surface.getBoundingBox();
        int x = Math.round((point.x-boundingPoints[0].x)*shape.ratioX2D);
        int y = Math.round((point.y-boundingPoints[0].y)*shape.ratioY2D/*/(boundingPoints[1].y-boundingPoints[0].y)*/);
        JFrame frame = frameMap.get(surface.getId());
        Component surfComponent = getComponent(surfaceName);
        //surfComponent.setBackground(Color.yellow);
        
        Container parent = surfComponent.getParent();
        while (!(parent instanceof JFrame)){
           // System.out.println(parent.getClass().getCanonicalName());
            parent = parent.getParent();
        }
        if (parent!=null){
            JFrame mainWindow = (JFrame)parent;
            mainWindow.toFront();
        }
        
        //surfComponent.getGraphics().drawRect(x,y,10,10);
        //Component clickedComponent = surfComponent.getComponentAt(x, y);
    //    System.out.println("Default2DGUIMapper:: The clicked component is "+clickedComponent.getClass().getCanonicalName()+ " and pos=["+clickedComponent.getLocationOnScreen().x+","+clickedComponent.getLocationOnScreen().y);
    //    System.out.println("Default2DGUIMapper:: Component [x,y]:["+surfComponent.getLocationOnScreen().x+","+surfComponent.getLocationOnScreen().y);        
    //    System.out.println("Default2DGUIMapper:: Component [w,h]:["+surfComponent.getWidth() +","+surfComponent.getHeight());        
    //    System.out.println("Default2DGUIMapper:: click [x,y]:["+x +","+y);
        x = x + surfComponent.getLocationOnScreen().x;
        y = y + surfComponent.getLocationOnScreen().y;
        System.out.println("Default2DGUIMapper:: mouseClick"+x+","+y);        

        Point oldPos = MouseInfo.getPointerInfo().getLocation();
    //    robot.mouseMove(0, 0);
    //  robot.delay(100);
        robot.mouseMove(x, y);
        leftClick();
        robot.mouseMove(oldPos.x, oldPos.y);
        rightClick();
    }
    private void leftClick()
    {
      robot.delay(200);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      robot.delay(200);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      robot.delay(200);
    }
    private void rightClick()
    {
      robot.delay(100);
      robot.mousePress(InputEvent.BUTTON3_MASK);
      robot.delay(100);
      robot.mouseRelease(InputEvent.BUTTON3_MASK);
      robot.delay(100);
    }
}
