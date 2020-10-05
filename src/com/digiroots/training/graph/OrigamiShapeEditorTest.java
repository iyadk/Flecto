/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

/**
 *
 * @author khaddam
 */
import java.awt.*;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import com.mxgraph.layout.*;
import com.mxgraph.swing.*;
import java.util.HashMap;
import java.util.Map;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;


public class OrigamiShapeEditorTest  extends JApplet{
    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private Dimension DEFAULT_SIZE = new Dimension(530, 320);

    //private JGraphXAdapter<Point2D, OrigamiEdge> jgxAdapter;
    private JGraphModelAdapter m_jgAdapter;


    OrigamiShape shape;
    public static void main(String[] args)
    {
        
        OrigamiShape shape = new OrigamiShape(null);
        int start = 100;
        int end = 300;
        Point2D p00 = shape.addPoint(new Point2D(start, start));
        Point2D p10 = shape.addPoint(new Point2D(start, end));
        Point2D p11 = shape.addPoint(new Point2D(end, end));
        Point2D p01 = shape.addPoint(new Point2D(end, start));
        shape.addEdge(p00, p01);
        shape.addEdge(p01, p11);
        shape.addEdge(p11, p10);
        shape.addEdge(p10, p00);
        shape.addHinge(new Point2D((start+end)/2, start),new  Point2D(start+(end-start)/4, end), "vally");
        shape.addHinge(new Point2D(start, (start+end)/2),new  Point2D(end, start+(end+start)/4), "mountain");
        System.out.println(shape.shapeGraph.toString());

        OrigamiShapeEditorTest applet = new OrigamiShapeEditorTest(shape.paperWidth,shape.paperHeight);
        applet.shape = shape;
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Origami Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void init()
    {
        // create a JGraphT graph
        //ListenableGraph<String, OrigamiEdge> g =
        //    new ListenableDirectedGraph<String, OrigamiEdge>(OrigamiEdge.class);

        // create a visualization using JGraph, via an adapter
        //jgxAdapter = new JGraphXAdapter<>(shape.shapeGraph);
        m_jgAdapter = new JGraphModelAdapter( shape.shapeGraph);

        JGraph jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( jgraph );
        getContentPane(  ).add( jgraph );
        resize( DEFAULT_SIZE );
        

        //getContentPane().add(new mxGraphComponent(jgxAdapter));
        //resize(DEFAULT_SIZE);

        /*String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add some sample data (graph manipulated via JGraphX)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v4, v3);
        */

        for (Point2D p: shape.shapeGraph.vertexSet()){
            positionVertexAt( p, p.x, p.y);
        }
        
        // positioning via jgraphx layouts
        //mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        //layout.execute(jgxAdapter.getDefaultParent());

        // that's all there is to it!...
    }
    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter( "bgcolor" );
        }
         catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );
    }


    private void positionVertexAt( Point2D vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle        b    = GraphConstants.getBounds( attr ).getBounds();

        GraphConstants.setBounds( attr, new Rectangle( x, y, 10, 10 ) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null );
    }    
    
    public OrigamiShapeEditorTest(int paperWidth, int paperHeight) {
        DEFAULT_SIZE = new Dimension(paperWidth+50, paperHeight+50);
    }
    

}
