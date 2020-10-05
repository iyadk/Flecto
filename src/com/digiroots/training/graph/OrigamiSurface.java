/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.awt.Color;

/**
 *
 * @author khaddam
 */
public class OrigamiSurface {
    public Point2D[] points;
    private String id;
    public String internalId;
    public OrigamiSurface(String id){
        points = new Point2D[0];
        this.id = id;
        this.internalId = id;
    }
    public String getId(){
        return id;//"S"+getCenter().x+"#"+getCenter().y;
    }
    public int getPointIndex(Point2D point){
        for (int i=0; i<points.length; i++){
            Point2D p = points[i];
            if (p==point)
                return i;
        }
        return -1;            
    }
    public void ensureClockwise(){
        Point2D v01 = new Point2D(points[0].x-points[1].x, points[0].y-points[1].y);
        Point2D v12 = new Point2D(points[1].x-points[2].x, points[1].y-points[2].y);
        int crossProd = v01.x*v12.y-v01.y*v12.x;
        //System.out.println("2d cross prod = "+crossProd);
        if (crossProd>0){ //in 2d, positive crossProd means points are arranged clock-wise. This is translated as counter--clock-wise in 3d.
            for (int i=0; i<points.length/2; i++){
                Point2D aux = points[i];
                points[i]=points[points.length-i-1];
                points[points.length-i-1] = aux;
            }
        }
    }
    public int[] getXVector(){
        int[] res = new int[points.length];
        for (int i=0; i<points.length; i++){
            res[i] = points[i].x;
        }
        return res;
    }
    public int[] getYVector(){
        int[] res = new int[points.length];
        for (int i=0; i<points.length; i++){
            res[i] = points[i].y;
        }
        return res;
    }
    public int getLength(){
        return points.length;
    }

    @Override
    public String toString() {
        String s = "Surface::";
        for (Point2D p: points){
            s+=p;
        }
        return s+"\n";
    }

    public void setColor(Color color) {
        this.color = color;
    }
    Color color;
    public Color getColor() {
        return color;
    }
    
    public Point2D getCenter(){       
        return new Point2D((points[0].x+points[1].x)/4+points[2].x/2, (points[0].y+points[1].y)/4+points[2].y/2);
    }
    public boolean contains(int x, int y) {
      int i;
      int j;
      boolean result = false;
      for (i = 0, j = points.length - 1; i < points.length; j = i++) {
        if ((points[i].y > y) != (points[j].y > y) &&
            (x < (points[j].x - points[i].x) * (y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
          result = !result;
         }
      }
      return result;
    }    

    public boolean hasCommonEdge(OrigamiSurface s){
        if (s==null)
            return false;
        int countMatchPoints = 0;
        for (Point2D p: s.points){
            if (getPointIndex(p)>=0)
                countMatchPoints++;
            if (countMatchPoints>=2)
                return true;
        }
        return false;
    }

    void setId(String id) {
        this.id= id;
    }
    public Point2D[] getBoundingBox(){
        int minX = points[0].x;
        int minY = points[0].y;
        int maxX = points[0].x;
        int maxY = points[0].y;
        for (int i=1; i<points.length; i++){
            if (minX>points[i].x)
                minX = points[i].x;
            if (minY>points[i].y)
                minY = points[i].y;
            if (maxX<points[i].x)
                maxX = points[i].x;
            if (maxY<points[i].y)
                maxY = points[i].y;
            
        }
        Point2D[] res = {new Point2D(minX, minY), new Point2D(maxX, maxY)};
        return res;
    }
}
