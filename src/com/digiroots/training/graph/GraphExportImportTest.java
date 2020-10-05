/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.GraphMLImporter;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.IntegerEdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.WeightedPseudograph;

/**
 *
 * @author khaddam
 */
public class GraphExportImportTest {

    private static DOTImporter<Point2D, OrigamiEdge> buildImporter()
    {
        return new DOTImporter<Point2D, OrigamiEdge>(new VertexProvider<Point2D>()
        {
            @Override
            public Point2D buildVertex(String label, Map<String, String> attributes)
            {
                String [] xy = label.split("\\.");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                //System.out.println("attributes for "+label+":"+ x+" "+y);
                return new Point2D(x, y);
            }
        }, new EdgeProvider<Point2D, OrigamiEdge>()
        {
            @Override
            public OrigamiEdge buildEdge(
                Point2D from, Point2D to, String label, Map<String, String> attributes)
            {
                return new OrigamiEdge(from, to, OrigamiEdge.decode(label));
            }
        });
    }
    public static void  addEdge(DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph , Point2D p1, Point2D p2) {
        addEdge(shapeGraph, p1,p2,OrigamiEdge.PHYSICAL);
    }
    public static void addEdge(DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph, Point2D p1, Point2D p2, String type) {
        if (p1!=p2){
            OrigamiEdge<Point2D> edge = new OrigamiEdge<Point2D>(p1, p2, type);
            shapeGraph.addEdge(p1, p2, edge);
            shapeGraph.setEdgeWeight(edge, 1);
        }
    }
    
    public static DefaultDirectedWeightedGraph buildGraph(){
    DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph = new DefaultDirectedWeightedGraph<Point2D, OrigamiEdge>(
                        new ClassBasedEdgeFactory<Point2D, OrigamiEdge>(OrigamiEdge.class));
        int start=100;
        int end = 300;
        Point2D p00 = new Point2D(start, start); shapeGraph.addVertex(p00);
        Point2D p10 = new Point2D(start, end); shapeGraph.addVertex(p10);
        Point2D p11 = new Point2D(end, end); shapeGraph.addVertex(p11);
        Point2D p01 = new Point2D(end, start); shapeGraph.addVertex(p01);
        addEdge(shapeGraph, p00, p01);
        addEdge(shapeGraph, p01, p11);
        addEdge(shapeGraph, p11, p10);
        addEdge(shapeGraph, p10, p00);
        //shapeGraph.addHinge(new Point2D(100,100), new Point2D(300,300), "vally");
        return shapeGraph;
                
    }

    public static void main(String[] args) throws ExportException, UnsupportedEncodingException, ImportException {
        DirectedMultigraph<Point2D, OrigamiEdge> start =
            new DirectedMultigraph<Point2D, OrigamiEdge>(OrigamiEdge.class);
        Point2D p1 = new Point2D(100, 100);
        Point2D p2 = new Point2D(200, 200);
        
        start.addVertex(p1);
        start.addVertex(p2);
        start.addEdge(p1, p2, new OrigamiEdge<Point2D>(p1, p2, OrigamiEdge.VALLY));
        /*start.addVertex("c");
        start.addVertex("d");
        start.addEdge("a", "b");
        start.addEdge("b", "c");
        start.addEdge("b", "d");*/
        

        DOTExporter<Point2D, OrigamiEdge> exporter
            = new DOTExporter<Point2D, OrigamiEdge>(new VertexNameProvider<Point2D>() {
                @Override
                public String getVertexName(Point2D vertex) {
                    return "" + vertex.x + "." + vertex.y;
                }
            }, null, new EdgeNameProvider<OrigamiEdge>() {
                @Override
                public String getEdgeName(OrigamiEdge e) {
                    return "" + e.getType().charAt(0);
                }

            }/*IntegerEdgeNameProvider<OrigamiEdge>()*/);
    
        

        DOTImporter<Point2D, OrigamiEdge> importer = buildImporter();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(start, os);
        String output = new String(os.toByteArray(), "UTF-8");
        System.out.println(output);
        DirectedMultigraph<Point2D, OrigamiEdge> result =
            new DirectedMultigraph<Point2D, OrigamiEdge>(OrigamiEdge.class);

        importer.importGraph(result, new StringReader(output));
        System.out.println(start);
        System.out.println(result);
    }
    /*
            DOTImporter<TestVertex, TestEdge> importer =
            new DOTImporter<TestVertex, TestEdge>(new VertexProvider<TestVertex>()
            {
                @Override
                public TestVertex buildVertex(String label, Map<String, String> attributes)
                {
                    return new TestVertex(label, attributes);
                }
            }, new EdgeProvider<TestVertex, TestEdge>()
            {
                @Override
                public TestEdge buildEdge(
                    TestVertex from, TestVertex to, String label, Map<String, String> attributes)
                {
                    return new TestEdge(label, attributes);
                }
            });
    */
}
