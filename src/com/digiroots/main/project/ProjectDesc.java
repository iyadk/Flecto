/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.main.project;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author khaddam
 */
public class ProjectDesc implements Serializable{
    private static final long serialVersionUID = 100000L;

    public int paperWidth = 400;
    public int paperHeight = 400;
    public float ratioX3D = 0.01f;
    public float ratioY3D= 0.01f;
    public float ratioX2D = 1.5f;
    public float ratioY2D= 2f;
    public int shiftX = 100;
    public int shiftY = 100;
    private boolean hasSavedShape = false;
    private boolean hasSavedShapeAttr = false;
    private boolean hasSavedInitialState = false;

    public void setHasSavedInitialState(boolean hasSavedInitialState) {
        this.hasSavedInitialState = hasSavedInitialState;
    }

    public boolean isHasSavedInitialState() {
        return hasSavedInitialState;
    }

    public void setSavedShapeAttr(boolean v) {
        hasSavedShapeAttr = v;
    }
    public String projectPath;
    public final static String SETTING_FILE="settings"; 
    public final static String SHAPE_FILENAME = "shape"; 
    public final static String SHAPE_ATTR_FILE = "attr";     
    public final static String SHAPE3D_INITIALSTATE_FILE = "initialState";     
    public String getInitialStateFile(){
        return this.projectPath+File.separator+SHAPE3D_INITIALSTATE_FILE;
    }
    
    public static ProjectDesc loadProject(String path){
        FileInputStream streamIn = null;
        ObjectInputStream objectinputstream = null;
        ProjectDesc readCase = null;
        try {
            streamIn = new FileInputStream(path+File.separator+SETTING_FILE);
            objectinputstream = new ObjectInputStream(streamIn);            
            readCase = (ProjectDesc) objectinputstream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(streamIn);
            closeStream(objectinputstream);
        }        
        return readCase;
    }
    private static void closeStream(Closeable s){
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }
    public void saveProject(){
        FileOutputStream fout=null;
        ObjectOutputStream oos=null;
        try {
            fout = new FileOutputStream(projectPath+ File.separator+SETTING_FILE);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
        } catch (IOException  ex) {
            System.out.println("Error saving project: ");
            ex.printStackTrace(System.out);
        } finally {
            closeStream(fout);
            closeStream(oos);            
        }   
    }

    @Override
    public String toString() {
        StringBuilder sr = new StringBuilder("Project info \n");
        sr.append("Path: "+this.projectPath+"\n");
        sr.append("Paper: "+paperWidth+"x"+paperHeight+"\n");
        sr.append("3D Ratio: "+this.ratioX3D+"x"+this.ratioY3D+"\n");
        sr.append("2D Ratio: "+this.ratioX2D+"x"+this.ratioY2D+"\n");
        return sr.toString();
    }
    public void setProjectPath(String projPath) {
        File f = new File(projPath);
        f.mkdirs();
        this.projectPath = f.getPath();
    }
    
    public static void main(String[] args){
        ProjectDesc proj = new ProjectDesc();
        String projPath = "c:\\OrigamiProj\\Test";
        proj.setProjectPath(projPath);
        System.out.println(proj.projectPath);
                
        proj.paperHeight=600;
        proj.paperWidth = 900;
        System.out.println("Saving project ...");
        proj.saveProject();
        System.out.println("Project saved");
        ProjectDesc proj2 = ProjectDesc.loadProject(projPath);
        System.out.println(proj);
        System.out.println(proj2);
    }

    public boolean hasSavedShape() {
        return hasSavedShape;
    }
    

    public String getShapeFilename() {
        return this.projectPath+File.separator+SHAPE_FILENAME;
    }

    public void setSavedShape(boolean b) {
        hasSavedShape = true;
    }

    public String getShapeAttributesFile() {
        return projectPath+ File.separator+SHAPE_ATTR_FILE;
    }

    public boolean hasSavedShapeAttr() {
        return hasSavedShapeAttr;
    }

    
    
}
