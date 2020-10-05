/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.Skeleton;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 *
 * @author khaddam
 */
public class SkeletonUtil {
    public static Skeleton buildValySkeleton(Geometry g1, Geometry g2){
        Bone leftBone = new Bone("leftBone");
        leftBone.setBindTransforms(new Vector3f(0, 0, 1), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));

        Bone rightBone = new Bone("rightBone");
        rightBone.setBindTransforms(new Vector3f(0, 0, 1), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));

        Bone[] bones = new Bone[] {leftBone, rightBone};
        Skeleton ske = new Skeleton(bones);

        return ske;
    }
    public static Skeleton buildMountainSkeleton(){
        return null;
    }
    
    	public static Skeleton buildSkeleton() {
		
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
        
}
