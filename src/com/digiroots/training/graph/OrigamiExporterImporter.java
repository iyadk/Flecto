/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

/**
 *
 * @author khaddam
 */
public class OrigamiExporterImporter {

    private DOTImporter<Point2D, OrigamiEdge> buildImporter() {
        return new DOTImporter<Point2D, OrigamiEdge>(new VertexProvider<Point2D>() {
            @Override
            public Point2D buildVertex(String label, Map<String, String> attributes) {
                String[] xy = label.split("\\.");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                //System.out.println("attributes for "+label+":"+ x+" "+y);
                return new Point2D(x, y);
            }
        }, new EdgeProvider<Point2D, OrigamiEdge>() {
            @Override
            public OrigamiEdge buildEdge(
                    Point2D from, Point2D to, String label, Map<String, String> attributes) {
                return new OrigamiEdge(from, to, OrigamiEdge.decode(label));
            }
        });
    }
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

    public void exportGraph(DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph, String fileName) throws ExportException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileWriter w = new FileWriter(fileName);

        exporter.exportGraph(shapeGraph, w);
        //String output = new String(os.toByteArray(), "UTF-8");
    }
    
    public DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> importGraph(String fileName) throws ExportException, IOException, ImportException {
        DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph;
        shapeGraph = new DefaultDirectedWeightedGraph<>(new ClassBasedEdgeFactory<Point2D, OrigamiEdge>(OrigamiEdge.class));
        FileReader fr = new FileReader(fileName);
        DOTImporter<Point2D, OrigamiEdge> importer = buildImporter();
        importer.importGraph(shapeGraph, fr);

        return shapeGraph;
        
        
    }


}
