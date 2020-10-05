/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import javax.xml.transform.TransformerConfigurationException;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.GraphMLImporter;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.IntegerEdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.StringEdgeNameProvider;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.xml.sax.SAXException;

public class JVertex {

    static class GraphNode {

        String name;
        String label;
        String type;

        public GraphNode(String name, String label, String type) {
            this.name = name;
            this.label = label;
            this.type = type;
        }

        @Override
        public String toString() {
            return "GraphNode{" + "name=" + name + ", label=" + label + ", type = " + type + '}';
        }
    }

    public static void main(String[] args) throws IOException, SAXException, TransformerConfigurationException, ExportException, ImportException {
        DefaultDirectedGraph<GraphNode, DefaultEdge> g
                = new DefaultDirectedGraph<>(DefaultEdge.class);
        GraphNode node1 = new GraphNode("node1", "label1", "type1");
        GraphNode node2 = new GraphNode("node2", "label2", "type1");

        g.addVertex(node1);
        g.addVertex(node2);
        g.addEdge(node1, node2);

        VertexNameProvider<GraphNode> vLabelNameProvider = new StringNameProvider<>();
        VertexNameProvider<GraphNode> vIdNameProvider = new IntegerNameProvider<>();

        EdgeNameProvider<DefaultEdge> eLabelNameProvider = new StringEdgeNameProvider<>();
        EdgeNameProvider<DefaultEdge> eIdNameProvider = new IntegerEdgeNameProvider<>();

        GraphMLExporter<GraphNode, DefaultEdge> exporter
                = new GraphMLExporter<>(vIdNameProvider,
                        vLabelNameProvider, eIdNameProvider, eLabelNameProvider);

        FileWriter w = new FileWriter("output2.txt");
        exporter.exportGraph(g, w);
        System.out.println(g);

        //import 
        VertexProvider<GraphNode> vp = new VertexProvider<GraphNode>() {
            @Override
            public GraphNode buildVertex(String label, Map<String, String> map) {
                GraphNode v = new GraphNode(map.get("name"), label, map.get("type"));
                return v;
            }
            
            /*@Override
            public String buildVertex(String label, Map<String, String> attributes) {
                vertexAttributes.put(label, attributes);
                return label;
            }*/
        };

        EdgeProvider<GraphNode, DefaultEdge> ep = new EdgeProvider<GraphNode, DefaultEdge>() {
            @Override
            public DefaultEdge buildEdge(GraphNode v, GraphNode v1, String string, Map<String, String> map) {
                DefaultEdge e = g.getEdgeFactory().createEdge(v, v1);
                //edgeAttributes.put(e, attributes);
                return e;
            }

            
            /*@Override
            public E buildEdge(String from, String to, String label, Map<String, String> attributes) {
                E e = g.getEdgeFactory().createEdge(from, to);
                edgeAttributes.put(e, attributes);
                return e;
            }*/

        };
        DefaultDirectedGraph<GraphNode, DefaultEdge> g2
                = new DefaultDirectedGraph<>(DefaultEdge.class);
        GraphMLImporter<GraphNode, DefaultEdge> importer = new GraphMLImporter<GraphNode, DefaultEdge>(vp, ep);

        FileReader file = new FileReader("output.txt");
		BufferedReader reader = new BufferedReader(file);
                
String input = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"<key id=\"vertex_label_key\" for=\"node\" attr.name=\"Vertex Label\" attr.type=\"string\"/>\n" +
"<key id=\"edge_label_key\" for=\"edge\" attr.name=\"Edge Label\" attr.type=\"string\"/>\n" +
"<graph edgedefault=\"directed\">\n" +
"<node id=\"1\">\n" +
"<data key=\"vertex_label_key\">GraphNode{name=node1, label=label1, type = type1}</data>\n" +
"</node>\n" +
"<node id=\"2\">\n" +
"<data key=\"vertex_label_key\">GraphNode{name=node2, label=label2, type = type1}</data>\n" +
"</node>\n" +
"<edge id=\"1\" source=\"1\" target=\"2\">\n" +
"<data key=\"edge_label_key\">(GraphNode{name=node1, label=label1, type = type1} : GraphNode{name=node2, label=label2, type = type1})</data>\n" +
"</edge>\n" +
"</graph>\n" +
"</graphml>";                
        importer.importGraph(g2, new StringReader(input));
        System.out.println(g2);
        
    }

}
