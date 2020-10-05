/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author khaddam
 */
public class OrigamiEdge<V> extends DefaultWeightedEdge {

    private V v1;
    private V v2;
    private String label;
    public static final String MOUNTAIN="mountain";
    public static final String VALLY="vally";
    public static final String PHYSICAL="physical";
    
    public static String decode(String val){
        if ("p".equalsIgnoreCase(val))
            return PHYSICAL;
        if ("v".equalsIgnoreCase(val))
            return VALLY;
        return MOUNTAIN;
    }
    public OrigamiEdge(){
        label=PHYSICAL;        
    }

    public OrigamiEdge(V v1, V v2, String label) {
        this.v1 = v1;
        this.v2 = v2;
        this.label = label;
    }

    public V getV1() {
        return v1;
    }

    public V getV2() {
        return v2;
    }
    public String getType() {
        return label;
    }

    public String toString() {
        return ""+v1+"->"+v2+label.charAt(0);
    }

    public boolean isPhysical() {
        return PHYSICAL.equalsIgnoreCase(label);
    }

    public boolean isVally() {
        return VALLY.equalsIgnoreCase(label);
    }

    public boolean isMountain() {
        return MOUNTAIN.equalsIgnoreCase(label);
    }

    TreeMap<String, OrigamiSurface> surfaceList = new TreeMap<>();
    //ArrayList<OrigamiSurface> surfaceList =  new ArrayList<>();
    void addSurface(OrigamiSurface surf) {
        surfaceList.put(surf.getId(),surf);
    }

    void reset() {
        surfaceList =  new TreeMap<>();
    }

    public OrigamiSurface[] getSurfaceList() {
        return surfaceList.values().toArray(new OrigamiSurface[0]);
    }

/*    private boolean visited = false;

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
*/    
}
