/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import org.jgrapht.graph.DefaultEdge;
import java.util.ArrayList;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author khaddam
 */
public class OrigamiEdgeTest {

    public String label;

    private static final String friend = "friend";
    private static final String enemy = "enemy";

    public static void main(String[] args) {
        DefaultDirectedGraph<String, OrigamiEdge> graph
                = new DefaultDirectedGraph<String, OrigamiEdge>(
                        new ClassBasedEdgeFactory<String, OrigamiEdge>(OrigamiEdge.class));

        ArrayList<String> people = new ArrayList<String>();
        people.add("John");
        people.add("James");
        people.add("Sarah");
        people.add("Jessica");

        graph.addVertex(people.get(0));

        // John is everyone's friend
        for (int i = 1; i < people.size(); i++) {
            String person = people.get(i);
            graph.addVertex(person);
            graph.addEdge(people.get(0), person, new OrigamiEdge<String>(people.get(0), person, friend));
        }

        // Apparently James doesn't really like John
        graph.addEdge("James", "John", new OrigamiEdge<String>("James", "John", enemy));

        // Jessica is Sarah and James's friend
        graph.addEdge("Jessica", "Sarah", new OrigamiEdge<String>("Jessica", "Sarah", friend));
        graph.addEdge("Jessica", "James", new OrigamiEdge<String>("Jessica", "James", friend));

        // But Sarah doesn't really like James
        graph.addEdge("Sarah", "James", new OrigamiEdge<String>("Sarah", "James", enemy));

        for (OrigamiEdge edge : graph.edgeSet()) {
            if (edge.toString().equals("enemy")) {
                System.out.printf(edge.getV1() + " is an enemy of " + edge.getV2() + "\n");
            } else if (edge.toString().equals("friend")) {
                System.out.printf(edge.getV1() + " is a friend of " + edge.getV2() + "\n");
            }
        }
        System.out.println(graph);
    }

    public static class RelationshipEdge<V> extends DefaultEdge {

        private V v1;
        private V v2;
        private String label;

        public RelationshipEdge(V v1, V v2, String label) {
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

        public String toString() {
            return label;
        }
    }
}
