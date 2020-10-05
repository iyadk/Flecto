/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

import com.jme3.app.SimpleApplication;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;
/**
 *
 * @author khaddam
 */
public class TestAnimation extends SimpleApplication {
   @Override
 public void simpleInitApp() {
		CreateAnimation2 ca = new CreateAnimation2(assetManager);
		Node node = ca.createModel();
		
		rootNode.attachChild(node);
		
		AnimControl ac = node.getControl(AnimControl.class);
		
		//SkeletonDebugger sd = new SkeletonDebugger("SkeletonDebuger", ac.getSkeleton());
		final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Magenta);
		mat.getAdditionalRenderState().setDepthTest(false);
		//sd.setMaterial(mat);
		//node.attachChild(sd);
		
		ac.createChannel().setAnim("Anim");;
	}
	public static void main(String[] args) {
		TestAnimation app = new TestAnimation();
		app.setShowSettings(false);
		app.start();
	} 
}
