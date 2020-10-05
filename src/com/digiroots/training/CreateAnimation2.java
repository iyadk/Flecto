/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

//https://github.com/jmecn/learnJME3/blob/master/jME3-Animation/src/com/ruanko/asset/CreateAnimation2.java
public class CreateAnimation2 {
	AssetManager assetManager;

	public CreateAnimation2(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public AnimControl createAnimControl() {
		
		Skeleton ske = buildSkeleton();
		
		AnimControl animControl = new AnimControl(ske);
		
		float length = 9f;
		
		Animation anim = new Animation("Anim", length);
		
		anim.addTrack(track0());
		anim.addTrack(track1());
		anim.addTrack(track2());
		
		animControl.addAnim(anim);
		
		return animControl;
	}
	
	public Skeleton buildSkeleton() {
		
		// create bones
		Bone bip01 = new Bone("Bip01");
		bip01.setBindTransforms(new Vector3f(0, 0, 1), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));

		Bone bip02 = new Bone("Bip02");
		bip02.setBindTransforms(new Vector3f(0.74f, 0.74f, 0), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));

		Bone bip03 = new Bone("Bip03");
		bip03.setBindTransforms(new Vector3f(0, 1, 0), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));
		
		bip01.addChild(bip02);
		bip02.addChild(bip03);
		
		// create skeleton
		Bone[] bones = new Bone[] {bip01, bip02, bip03};
		Skeleton ske = new Skeleton(bones);

		return ske;
	}
	
	private BoneTrack track0() {
		int size = 10;// numbers of keyframes
		
		float[] times = new float[size];
		Vector3f[] translations = new Vector3f[size];
		Quaternion[] rotations = new Quaternion[size];
		Vector3f[] scales = new Vector3f[size];
		
		// initBindTransform
		times[0] = 0;
		translations[0] = new Vector3f(0, 0, 0);
		rotations[0] = new Quaternion(0, 0, 0, 1);
		scales[0] = new Vector3f(1, 1, 1);
		
		Quaternion q = new Quaternion().fromAngleAxis(FastMath.PI/3, new Vector3f(0, 1, 0));
		for(int i=1; i<size; i++) {
			times[i] = i;
			translations[i] = translations[i-1];
			rotations[i] = rotations[i-1].mult(q);
			scales[i] = scales[i-1];
		}
		
		BoneTrack track = new BoneTrack(0, times, translations, rotations, scales);
		
		return track;
	}
	
	private BoneTrack track1() {
		int size = 10;// numbers of keyframes
		
		float[] times = new float[size];
		Vector3f[] translations = new Vector3f[size];
		Quaternion[] rotations = new Quaternion[size];
		Vector3f[] scales = new Vector3f[size];
		
		// initBindTransform
		times[0] = 0;
		translations[0] = new Vector3f(0f, 0f, 0f);
		rotations[0] = new Quaternion(0, 0, 0, 1);
		scales[0] = new Vector3f(1, 1, 1);
		
		for(int i=1; i<size; i++) {
			times[i] = i;
			translations[i] = translations[i-1].add(0, 0.2f, 0);
			rotations[i] = rotations[i-1];
			scales[i] = scales[i-1];
		}
		
		BoneTrack track = new BoneTrack(1, times, translations, rotations, scales);
		
		return track;
	}
	
	private BoneTrack track2() {
		int size = 10;// numbers of keyframes
		
		float[] times = new float[size];
		Vector3f[] translations = new Vector3f[size];
		Quaternion[] rotations = new Quaternion[size];
		Vector3f[] scales = new Vector3f[size];
		
		// initBindTransform
		times[0] = 0;
		translations[0] = new Vector3f(0, 0, 0);
		rotations[0] = new Quaternion(0, 0, 0, 1);
		scales[0] = new Vector3f(1, 1, 1);
		
		for(int i=1; i<size; i++) {
			times[i] = i;
			translations[i] = translations[i-1].add(new Vector3f(0.4f, 0, 0f));
			rotations[i] = rotations[i-1];
			scales[i] = scales[i-1].mult(1.1f);
		}
		
		BoneTrack track = new BoneTrack(2, times, translations, rotations, scales);
		
		return track;
	}
	
	public Node createModel() {
		Node model = new Node("model");
		
                
		Box box = new Box(0.1f, 0.1f, 0.1f);
		Geometry box01 = new Geometry("box01", box);
		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Green);
		box01.setMaterial(mat1);
		
		Box box2 = new Box(0.1f, 0.1f, 0.1f);
		Geometry box02 = new Geometry("box02", box2);
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Blue);
		box02.setMaterial(mat2);
		
		Box box3 = new Box(0.1f, 0.1f, 0.1f);
		Geometry box03 = new Geometry("box03", box3);
		Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", ColorRGBA.Red);
		box03.setMaterial(mat3);
		
		model.attachChild(box01);
		model.attachChild(box02);
		model.attachChild(box03);
		
		
		AnimControl ac = createAnimControl();
		model.addControl(ac);
		
		// Create a SkeletonControl
		SkeletonControl sc = new SkeletonControl(ac.getSkeleton());
		// add it to Node before call getAttachmentsNode(String) or it returns null
		model.addControl(sc);
		
		// attach geomertries to bones: post by prog
		sc.getAttachmentsNode("Bip01").attachChild(box01);
		sc.getAttachmentsNode("Bip02").attachChild(box02);
		sc.getAttachmentsNode("Bip03").attachChild(box03);
		
		
		return model;
	}
}
