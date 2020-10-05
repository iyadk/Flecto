/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

/**
 *
 * @author khaddam
 */
public class Point2D {
    public int x;
    public int y;
    public int visits = 0;
    public Point2D(){                
    }
    public Point2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "["+x+","+y+"]"; //To change body of generated methods, choose Tools | Templates.
    }
    public String getId(){
        return x+","+y;
    }
            
     
}
