/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.digiroots.main.project.ProjectDesc;

/**
 *
 * @author khaddam
 */
public class OrigamiShapeEditor {

     public OrigamiHingeEditor hingeEditor;
     public OrigamiSurfaceEditor surface1Editor;
     public OrigamiSurfaceEditor surface2Editor;
     private ProjectDesc project;
     public OrigamiShapeEditor(ProjectDesc project){
         this.project = project;
     }


    public static class OrigamiPanel extends JPanel {

        Point pointStart = null;
        Point pointEnd = null;

        OrigamiShape shape;
        //OrigamiShapeEditor editor;
        public OrigamiPanel(OrigamiShape shape/*, OrigamiShapeEditor editor*/) {
            this.shape = shape;
            //this.editor = editor;
            addMouseListener(new MouseAdapter() {
                /*@Override
                public void mouseClicked(MouseEvent me) {
                    if (me.isControlDown()){
                        shape.selectClickedSurface(me.getPoint().x, me.getPoint().y);
                        *//*editor.surface1Editor.refresh();
                        editor.surface2Editor.refresh();
                        editor.hingeEditor.refresh();
                        */
                    /*}
                }*/
                
                public void mousePressed(MouseEvent e) {
                    pointStart = e.getPoint();
                }

                public void mouseReleased(MouseEvent e) {

                    System.out.println("shape.addHinge(new Point2D(" + pointStart.x + "," + pointStart.y + "), new Point2D(" + pointEnd.x + "," + pointEnd.y + "), \"vally\");");
                    if (SwingUtilities.isRightMouseButton(e)) {
                        shape.addHinge(new Point2D(pointStart.x, pointStart.y), new Point2D(pointEnd.x, pointEnd.y), OrigamiEdge.MOUNTAIN);
                    } else {
                        shape.addHinge(new Point2D(pointStart.x, pointStart.y), new Point2D(pointEnd.x, pointEnd.y), OrigamiEdge.VALLY);
                    }

                    pointStart = null;
                    //debugGraph(shape);
                    repaint();

                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    pointEnd = e.getPoint();
                    repaint();
                }

                public void mouseDragged(MouseEvent e) {
                    pointEnd = e.getPoint();
                    repaint();
                }
            });
        }

        public void paint(Graphics g) {
            super.paint(g);
            ArrayList<OrigamiSurface> surfaceList = shape.getColoredSurfaces();
            for (OrigamiSurface s : surfaceList) {
                int[] x = s.getXVector();
                int[] y = s.getYVector();
                g.setColor(s.getColor());
                g.fillPolygon(x, y, x.length);
                g.setColor(Color.BLACK);
                g.drawString(s.getId(), s.getCenter().x, s.getCenter().y);
            }
            g.setColor(Color.RED);
            Point2D clickedpoint = shape.getClicked3DPoint();
            if (clickedpoint!=null){
                g.fillOval(clickedpoint.x-1, clickedpoint.y-1, 2, 2);
            }
            g.setColor(Color.BLACK);

            if (pointEnd != null) {
                g.drawString("" + pointEnd.x + "," + pointEnd.y, pointEnd.x, pointEnd.y);
            }
            if (pointStart != null) {
                g.setColor(Color.RED);

                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
            }
            drawOrigamiShape(g);
        }

        private void drawOrigamiShape(Graphics g) {
            for (Point2D p : shape.shapeGraph.vertexSet()) {
                g.drawRect(p.x - 2, p.y - 2, 4, 4);
            }
            for (OrigamiEdge edge : shape.shapeGraph.edgeSet()) {
                Point2D startP = (Point2D) edge.getV1();
                Point2D endP = (Point2D) edge.getV2();
                g.setColor(Color.BLACK);
                if (edge.isPhysical()) {
                    g.setColor(Color.BLACK);
                } else if (edge.isVally()) {
                    g.setColor(Color.LIGHT_GRAY);
                    Graphics2D g2d = (Graphics2D) g.create();
                    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                    g2d.setStroke(dashed);
                    g2d.drawLine(startP.x, startP.y, endP.x, endP.y);
                    g2d.dispose();
                    continue;
                } else if (edge.isMountain()) {
                    g.setColor(Color.DARK_GRAY);
                }
                g.drawLine(startP.x, startP.y, endP.x, endP.y);
            }

        }
    }
/*    public static OrigamiShapeEditorTest debugGraph(OrigamiShape shape) {
        OrigamiShapeEditorTest applet = new OrigamiShapeEditorTest(shape.paperWidth, shape.paperHeight);
        applet.shape = shape;
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Origami Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return applet;
    }
*/
}
