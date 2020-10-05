/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author khaddam
 */
public class TestArrayList {
    public static void main(String[] args){
        Vector3f v = new Vector3f(1,1,1);
        Vector3f v2 = new Vector3f(0,1,0);
        ArrayList<Vector3f> arr = new ArrayList<>();
        arr.add(v);
        arr.add(v2);
        arr.add(v);
        System.out.println(arr);
    }
    
}
