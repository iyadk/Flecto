/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph.model3d;

import com.jme3.math.Vector3f;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author khaddam
 */
class Point3D implements Serializable{
    Vector3f vertex;
    Vector3f origin;
    transient ArrayList<String> surfaceList = new ArrayList<>();
    String id;

    Point3D(String pointId, Vector3f vertex, String surfaceId) {
        this.id = pointId;
        this.vertex = vertex;
        this.origin = new Vector3f(vertex);
        this.surfaceList.add(surfaceId);        
    }

    public void setVertex(Vector3f vertex) {
        this.vertex = vertex;
    }

    public Vector3f getVertex() {
        return vertex;
    }

    public void addSurface(String surfaceId) {
        if (surfaceList==null){
            surfaceList = new ArrayList<>();
        }
        this.surfaceList.add(surfaceId);
    }

    public ArrayList<String> getSurfaceList() {
        return surfaceList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public Point3D(String id){
        this.id = id;
    }

    Vector3f getOrigin() {
        return origin;
    }

    void setVertexInternal(Vector3f vertex) {
        this.vertex.x = vertex.x;
        this.vertex.y = vertex.y;
        this.vertex.z = vertex.z;
    }
}
