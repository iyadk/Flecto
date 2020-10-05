/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

import quickhull3d.Point3d;
import quickhull3d.gdx.EarClippingTriangulator;
import quickhull3d.gdx.ShortArray;

/**
 *
 * @author khaddam
 */
public class Test {
    public static void main (String[] args)
	 {
           // x y z coordinates of 6 points
	   Point3d[] points = new Point3d[]
                { new Point3d (0.0,  0.0,  0.0),
		new Point3d (1.0,  0.0,  0.0),
		new Point3d (1.0,  1.0,  0.0),
		new Point3d (0.0,  1.0,  0.0)};
           
           float[] p = new float[8];
           for (int i=0; i<4; i++){
               p[2*i]=(float)points[i].x;
               p[2*i+1]=(float)points[i].y;
           }
           EarClippingTriangulator triang = new EarClippingTriangulator();
        ShortArray res = triang.computeTriangles(p);
        
        System.out.println(res);
           
         }
}
