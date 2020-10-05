/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.controls;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.digiroots.foldale.appStates.SimulatorAppState;

/**
 *
 * @author khaddam
 */
public class MountainFold extends Fold{
     public MountainFold(SimulatorAppState appState){
        super(appState);        
    }
    @Override
    public float getMinDegree() {
        return 0;
    }
    @Override
    public float getMaxDegree() {
        return FastMath.PI;
    }     
    @Override
    public void rotateClose() {
        if (currentMoment<TotalMoments)
            currentMoment ++;
        doRotate();
    }
    @Override
    public void rotateOpen() {
        if (currentMoment>0)
            currentMoment --;
        doRotate();       
    }
}
