/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 *
 * @author khaddam
 */
public class FrameAnalyser {
  TreeMap<String, Component> componentMap = new TreeMap<String, Component>();
  public TreeMap<String, Component> getAnalysisResult(){
      return componentMap;
  }
  public  void analyse(JFrame f){
    Component[] components = f.getComponents();
    addToMap(f.getContentPane());
    for (int i = 0; i < components.length; i++) {
        Component c = components[i];
        analyseComponent(c);
    }
  }
  public  void analyse(JRootPane f){
      for (Component c: f.getComponents()){
            analyseComponent(c);
     }
  }
  public  void analyse(JPanel f){
      for (Component c: f.getComponents()){
            analyseComponent(c);
     }
  }
  
  public  void analyse(JLayeredPane f){
      for (Component c: f.getComponents()){
            analyseComponent(c);
     }
  }
  public  void analyse(Component c){
      //System.out.println("NOT IMPLEMENTED:: Component ["+c.getClass().getSimpleName()+"] "+c.getName()+ ", Rectangle="+c.getBounds()); 
  }
  
  
  public  void analyseComponent(Component c){
      Rectangle bounds = c.getBounds();
      addToMap(c);
      if (c instanceof JRootPane)
          analyse((JRootPane)c);
      else
      if (c instanceof JPanel)
          analyse((JPanel)c);
      else
      if (c instanceof JLayeredPane)
          analyse((JLayeredPane)c);
      else
          analyse(c);
  }

    private void addToMap(Component c) {
        String id = c.getName();        
        if (id!=null && !"".equals(id.trim())){
            componentMap.put(id, c);
            System.out.println("Component ["+c.getClass().getSimpleName()+"] "+c.getName()+ ", Rectangle="+c.getBounds()); 
            
            return;
        }        
    }
  
}
