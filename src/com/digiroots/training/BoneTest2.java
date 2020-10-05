/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

/**
 *
 * @author khaddam
 */
import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.scene.shape.Box;
import com.digiroots.foldale.controls.DoorController;

public class BoneTest2 extends SimpleApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BoneTest2 t = new BoneTest2();
        t.start();
    }

    @Override
    public void simpleInitApp() {
        Bone[] bones = new Bone[4];

        bones[0] = new Bone("root1");
        bones[1] = new Bone("child1");
        bones[2] = new Bone("child2");
        bones[3] = new Bone("child3");
        //bones[4] = new Bone("root2");
/*        bones[3] = new Bone("root2");
        bones[4] = new Bone("child1.1");
*/
        bones[0].addChild(bones[1]);
        bones[1].addChild(bones[2]);
        bones[2].addChild(bones[3]);
        //bones[3].addChild(bones[1]);

        bones[0].setBindTransforms(new Vector3f(-1, 0, 0), new Quaternion(),
                new Vector3f(1, 1, 1));
        bones[1].setBindTransforms(new Vector3f(2, 0, 0), new Quaternion(),
                new Vector3f(1, 1, 1));
        bones[2].setBindTransforms(new Vector3f(-2, 0, 0), new Quaternion(),
                new Vector3f(1, 1, 1));
        bones[3].setBindTransforms(new Vector3f(0, 0, 2), new Quaternion(),
                new Vector3f(1, 1, 1));
        
        bones[0].setUserControl(true);
        bones[1].setUserControl(true);
        bones[2].setUserControl(true);
        bones[3].setUserControl(true);
       

        Skeleton skeleton = new Skeleton(bones);
        SkeletonControl sc = new SkeletonControl(skeleton);
        SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", skeleton);
        skeletonDebug.addControl(sc);

        attachGeomToBone("root1", sc);//, true);        
        attachGeomToBone("child1", sc, true);
        attachGeomToBone("child2", sc, false);
        attachGeomToBone("child3", sc, false);
        /*bones[1].setUserTransforms(new Vector3f(1, 1, 0), new Quaternion(),
                new Vector3f(1, 1, 1));
        bones[3].setUserTransforms(new Vector3f(2, 2, 0), new Quaternion(),
                new Vector3f(1, 1, 1));

        bones[4].setUserTransforms(new Vector3f(1, 0, 0), new Quaternion(0f, 0f, 0.2f, 0.5f),
                new Vector3f(1, 1, 1));
*/
        skeleton.updateWorldVectors();
        skeleton.setBindingPose();

//bones[1].setUserTransforms(new Vector3f(20, 0, 0), new Quaternion(), new Vector3f(1,1,1));
//skeleton.updateWorldVectors();
//skeleton.setBindingPose();
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        mat.getAdditionalRenderState().setDepthTest(false);
        skeletonDebug.setMaterial(mat);

        Node node = new Node();
        node.attachChild(skeletonDebug);
        rootNode.attachChild(node);
        rootNode.attachChild(createGeometry(1,0,0));
        rootNode.attachChild(createGeometry(2,0,0));
        rootNode.attachChild(createGeometry(3,0,0));
        rootNode.attachChild(createGeometry(0,1,0));
        rootNode.attachChild(createGeometry(0,2,0));
        rootNode.attachChild(createGeometry(0,3,0));
        rootNode.attachChild(createGeometry(0,0,1));
        rootNode.attachChild(createGeometry(0,0,2));
        rootNode.attachChild(createGeometry(0,0,3));

//SkeletonControl sc = skeletonDebug.getControl(SkeletonControl.class);
    }
    private Geometry createGeometry(float x, float y, float z){
        Box b = new Box(0.1f, 0.1f, 0.1f);
        Geometry geom = new Geometry("Box", b);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat2);
        geom.setLocalTranslation(x, y, z);
        return geom;
    }

    private void attachGeomToBone(String boneName, SkeletonControl sc, boolean withCotroller) {
        Bone bone = sc.getSkeleton().getBone(boneName);
        System.out.println(bone.getBindPosition()+","+bone.getLocalPosition()+","+bone.getModelSpacePosition());
        
        Box b = new Box(0.5f, 0.01f, 0.5f);
        Geometry geom = new Geometry("Box", b);
        geom.setLocalTranslation(0.5f, 0, 0);
               

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat2);
        Node root1Node = sc.getAttachmentsNode(boneName);
        if (withCotroller) {

            //DoorController dc = new DoorController(root1Node, sc.getSkeleton(), boneName);
            //dc.setRotation(new Vector3f(-0f, 0f, 0.0f), Vector3f.UNIT_Z/*new Vector3f(1, 1, 1)*/, 0, 60000f, 1);
            //dc.setRotation(new Vector3f(-0.0f, 0, 0), Vector3f.UNIT_Z, 0, 3000f, 1);
            //root1Node.addControl(dc);
            ;
        }
        root1Node.attachChild(geom);
    }

    private void attachGeomToBone(String boneName, SkeletonControl sc) {
        attachGeomToBone(boneName, sc, false);
    }
}
