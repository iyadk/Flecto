/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.awt.Color;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.digiroots.main.project.ProjectDesc;
import static com.digiroots.main.project.ProjectDesc.SETTING_FILE;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.ImportException;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
//import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author khaddam
 */
public class OrigamiShape {

    DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> shapeGraph = new DefaultDirectedWeightedGraph<Point2D, OrigamiEdge>(
                        new ClassBasedEdgeFactory<Point2D, OrigamiEdge>(OrigamiEdge.class));
    public int paperWidth = 400;
    public int paperHeight = 400;
    public float ratioX3D = 0.01f;
    public float ratioY3D= 0.01f;
    public float ratioX2D = 1.5f;
    public float ratioY2D= 2f;
    
    public static final double EPSILON=0.0001;
    
    private ArrayList<OrigamiHingeEventListener> origamiHingeEventListener = new ArrayList<>();

    public void addOrigamiHingeEventListener(OrigamiHingeEventListener origamiHingeEventListener) {
        this.origamiHingeEventListener.add(origamiHingeEventListener);
    }
    public void removeOrigamiHingeEventListener(OrigamiHingeEventListener hingeEventListener) {
        this.origamiHingeEventListener.remove(hingeEventListener);
    }

    public ArrayList<OrigamiHingeEventListener> getOrigamiHingeEventListeners() {
        return origamiHingeEventListener;
    }
    

    public DefaultDirectedWeightedGraph<Point2D, OrigamiEdge> getShapeGraph(){
        return shapeGraph;
    }
    public int getPrecision() {
        return 5;
        //return Math.min(paperHeight, paperWidth)/100;
    }

    private ProjectDesc project;

    public void bindProject(){
        paperWidth = project.paperWidth;
        paperHeight = project.paperHeight;
        this.ratioX2D = project.ratioX2D;
        this.ratioY2D = project.ratioY2D;
        this.ratioX3D = project.ratioX3D;
        this.ratioY3D = project.ratioY3D;
        shiftX = project.shiftX;
        shiftY = project.shiftY;        
    }
    public OrigamiShape(ProjectDesc project) {
        this.project = project;
        bindProject();
        if (project.hasSavedShape()){
            loadFromSaved(project.getShapeFilename());
        }
        else{
            int endX = shiftX + paperWidth;
            int endY = shiftY + paperHeight;

            Point2D p00 = addPoint(new Point2D(shiftX, shiftY));
            Point2D p10 = addPoint(new Point2D(shiftX, endY));
            Point2D p11 = addPoint(new Point2D(endX, endY));
            Point2D p01 = addPoint(new Point2D(endX, shiftY));
            addEdge(p00, p01);
            addEdge(p01, p11);
            addEdge(p11, p10);
            addEdge(p10, p00);            
        }
        
    }
    private void loadFromSaved(String fileName){
        OrigamiExporterImporter importer = new OrigamiExporterImporter();
        try {
            shapeGraph = importer.importGraph(fileName);
        } catch (IOException ex) {
            Logger.getLogger(OrigamiShapeEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImportException ex) {
            Logger.getLogger(OrigamiShapeEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExportException ex) {
            Logger.getLogger(OrigamiShapeEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (project.hasSavedShapeAttr())
            loadsaveAttributeMap();
    }

    public void saveShape(){
        OrigamiExporterImporter exporter = new OrigamiExporterImporter();
        try {
            exporter.exportGraph(shapeGraph, project.getShapeFilename());
            project.setSavedShape(true);
        } catch (ExportException ex) {
            Logger.getLogger(OrigamiShapeEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OrigamiShapeEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        saveAttributeMap();
    }
    public void loadsaveAttributeMap(){
        FileInputStream streamIn = null;
        ObjectInputStream objectinputstream = null;
        ProjectDesc readCase = null;
        try {
            streamIn = new FileInputStream(project.getShapeAttributesFile());
            objectinputstream = new ObjectInputStream(streamIn);            
            this.surfaceAttributeMap = (TreeMap<String, OrigamiSurfaceAttributes>) objectinputstream.readObject();
            this.edgeAttributeMap = (TreeMap<String, OrigamiEdgeAttributes>) objectinputstream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(streamIn);
            closeStream(objectinputstream);
        }        
    }    
    private static void closeStream(Closeable s){
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }
    public void saveAttributeMap(){
        FileOutputStream fout=null;
        ObjectOutputStream oos=null;
        try {
            fout = new FileOutputStream(project.getShapeAttributesFile());
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.surfaceAttributeMap);
            oos.writeObject(this.edgeAttributeMap);
            project.setSavedShapeAttr(true);
        } catch (IOException  ex) {
            System.out.println("Error saving project: ");
            ex.printStackTrace(System.out);
        } finally {
            closeStream(fout);
            closeStream(oos);            
        }   
    }    
    
/*    public void saveEdgeAttributeMap(){
        this.edgeAttributeMap;
        TreeMap<String, OrigamiEdgeAttributes>
        
        FileOutputStream fout=null;
        ObjectOutputStream oos=null;
        try {
            fout = new FileOutputStream(project.getShapeAttributesFile());
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.surfaceAttributeMap);
            project.setSavedShapeAttr(true);
        } catch (IOException  ex) {
            System.out.println("Error saving project: ");
            ex.printStackTrace(System.out);
        } finally {
            closeStream(fout);
            closeStream(oos);            
        }   
    }*/        
    
    public Point2D addPoint(Point2D point) {
        Point2D corrected = correctedVertx(point);//check the point already exists
        if (corrected == null) {//Don't add the vertix if it already exists
            //ArrayList<OrigamiEdge> edges = getContainingEdge(point);
            shapeGraph.addVertex(point);
            /*if (edges!=null)
                for (OrigamiEdge edge : edges) {
                    Point2D p1 = shapeGraph.getEdgeSource(edge);
                    Point2D p2 = shapeGraph.getEdgeTarget(edge);
                    addEdge(p1, point, edge.getType());//TODO: copy properties of edge into this edge
                    addEdge(point, p2, edge.getType());//TODO: copy properties of edge into this edge
                    shapeGraph.removeEdge(edge);
                }*/
            corrected = point;
        }
        //if the new vertex is on an edge, cut the edge into 2.
        //TODO
        return corrected;
    }

    private Point2D correctedVertx(Point2D point) {
        for (Point2D p : shapeGraph.vertexSet()) {
            if (Math.abs(p.x - point.x) < getPrecision() && Math.abs(p.y - point.y) < getPrecision()) {
                return p;
            }
        }
        return null;
    }

    private ArrayList<OrigamiEdge> getContainingEdge(Point2D point) {
        ArrayList<OrigamiEdge> edges = new ArrayList<>();
        shapeGraph.edgeSet().stream().filter((edge) -> (isPointOnEdge(edge, point))).forEach((edge) -> {
            edges.add(edge);
        });
        if (edges.isEmpty()) {
            return null;
        }
        return edges;
    }

    private Point2D correctPointOnEdge(OrigamiEdge edge, Point2D point) {
        //if (!isPointOnEdge(edge, point))
        //    return point;
        //System.out.println(p1+","+p2)
        Point2D p1 = shapeGraph.getEdgeSource(edge);
        Point2D p2 = shapeGraph.getEdgeTarget(edge);
        int dxc = roundDimension(point.x - p1.x);
        int dyc = roundDimension(point.y - p1.y);

        int dxl = roundDimension(p2.x - p1.x);
        int dyl = roundDimension(p2.y - p1.y);
        int cross = dxc * dyl - dyc * dxl;
        if (cross == 0)//The point is on the edge. Do nothing
        {
            return point;
        }

        //fix x and calculate y
        Point2D px = new Point2D();
        px.x = point.x;
        if (dyl == 0) {
            px.y = p1.y;
        } else if (dxl == 0) {
            px.y = point.y;
        } else {
            px.y = (px.x - p1.x) * (p2.x - p1.x) / (p2.y - p1.y) + p1.y;
        }
        //fix y and calculate x
        Point2D py = new Point2D();
        py.y = point.y;
        if (dxl == 0) {
            py.x = p1.x;
        } else if (dyl == 0) {
            py.x = point.x;
        } else {
            py.x = (py.y - p1.y) * (p2.y - p1.y) / (p2.x - p1.x) + p1.x;
        }

        //return the point in the middle
        Point2D corected = new Point2D();
        corected.x = (px.x + py.x) / 2;
        corected.y = (px.y + py.y) / 2;
        return corected;
    }

    private int roundDimension(int val){
        if (Math.abs(val)>getPrecision())
            return val;
        return 0;
    }
    private boolean isPointOnEdge(OrigamiEdge edge, Point2D point) {
        Point2D p1 = shapeGraph.getEdgeSource(edge);
        Point2D p2 = shapeGraph.getEdgeTarget(edge);
        int dxc = roundDimension(point.x - p1.x);
        int dyc = roundDimension(point.y - p1.y);

        int dxl = roundDimension(p2.x - p1.x);
        int dyl = roundDimension(p2.y - p1.y);
        int cross = dxc * dyl - dyc * dxl;
        if (!(Math.abs(cross) < getPrecision())) {
            return false;
        }
        //Now correct point to be exact on the line. 
        Point2D corrected = correctPointOnEdge(edge, point);

        //Check point is inside the interval
        if (corrected.x >= Math.min(p1.x, p2.x) && corrected.x <= Math.max(p1.x, p2.x)
                && corrected.y >= Math.min(p1.y, p2.y) && corrected.y <= Math.max(p1.y, p2.y)) {
            point.x = corrected.x;
            point.y = corrected.y;
            return true;
        }
        return false;
    }

    private Point2D[] intersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        Point2D[] res;
        if (x1 == x2 || x3 == x4) {//vertical segments
            if (x1 == x2 && x3 == x4) {
                if (x1 != x3)//parallel, no intersection                
                    return null;
                if (Math.min(y3, y4) > Math.max(y1, y2) || Math.min(y1, y2) > Math.max(y3, y4))//ys do not overlap                
                    return null;
                //they overlap. return two points in worst case
                int ymax = Math.min(Math.max(y1, y2), Math.max(y3, y4));
                int ymin = Math.max(Math.min(y1, y2), Math.min(y3, y4));
                if (ymax == ymin)//overlap on one point only. Consider no intersection
                    return null;

                //return new Point2D[] {new Point2D(x1,ymin)};                    
                return new Point2D[]{new Point2D(x1, ymin), new Point2D(x1, ymax)};
            }
            if (x1==x2){
                //First check that x1 belongs to second segment
                if (x1<Math.min(x3,x4) || x1>Math.max(x3, x4))//Check belongs to first segment
                    return null;                
                int yinter = (y4-y3)*(x1-x3)/(x4-x3)+y3;
                if (yinter<Math.min(y1,y2) || yinter>Math.max(y1, y2))//Check belongs to the first segment
                    return null;
                if (yinter<Math.min(y3,y4) || yinter>Math.max(y3, y4))//Check belongs to the second segment
                    return null;
                return new Point2D[]{new Point2D(x1, yinter)};                
            }
            //x3==x4
            //First check that x3 belongs to the first segment
            if (x3<Math.min(x1,x2) || x3>Math.max(x1, x2))//Check belongs to first segment
                return null;                
            int yinter = (y2-y1)*(x3-x1)/(x2-x1)+y1;
            if (yinter<Math.min(y3,y4) || yinter>Math.max(y3, y4))//Check belongs to the second segment
                return null;
            if (yinter<Math.min(y1,y2) || yinter>Math.max(y1, y2))//Check belongs to the first segment
                return null;
            return new Point2D[]{new Point2D(x3, yinter)};                
        }
        double a1 = (y2-y1)/(float)(x2-x1);
        double b1 = y1-a1*x1;
        double a2= (y4-y3)/(float)(x4-x3);
        double b2 = y3-a2*x3;
        if((y2-y1)*(x4-x3) == (x2-x1)*(y4-y3)){//parallel lines. 
            if (Math.abs(b1-b2)<EPSILON){ //parallel and identical. Check overlaping
                if (Math.min(y3, y4) > Math.max(y1, y2) || Math.min(y1, y2) > Math.max(y3, y4))//ys do not overlap                
                    return null;
                //they overlap. return two points in worst case
                int ymax = Math.min(Math.max(y1, y2), Math.max(y3, y4));
                int ymin = Math.max(Math.min(y1, y2), Math.min(y3, y4));
                if (ymax == ymin)//overlap on one point only. Consider no intersection
                    return null;
                int xmax = x1;
                int xmin = x1;
                if (Math.abs(a1)<EPSILON){
                    xmax = (int)Math.round((ymax-b1)/a1);
                    xmin = (int)Math.round((ymin-b1)/a1);
                }
                //return new Point2D[] {new Point2D(x1,ymin)};                    
                return new Point2D[]{new Point2D(xmin, ymin), new Point2D(xmax, ymax)};
            }
            return null;            
        }
        //Not parallel. calc intersection
        int xinter = (int)Math.round(-(b1-b2)/(a1-a2));        
        int yinter = (int)Math.round(a1*xinter+b1);
        if (Math.abs(a1)>Math.abs(a2))//Calc y from the equation that gives more precise values for y
            yinter = (int)Math.round(a2*xinter+b2);
        if (xinter<Math.min(x1,x2) || xinter>Math.max(x1, x2))//Check belongs to first segment
            return null;                
        if (xinter<Math.min(x3,x4) || xinter>Math.max(x3, x4))//Check belongs to second segment
            return null;                
        return new Point2D[]{new Point2D(xinter, yinter)};
    }

    public void addEdge(Point2D p1, Point2D p2) {
        addEdge(p1,p2,OrigamiEdge.PHYSICAL);
    }
    public void addEdge(Point2D p1, Point2D p2, String type) {
        if (p1!=p2){
            OrigamiEdge<Point2D> edge = new OrigamiEdge<Point2D>(p1, p2, type);
            shapeGraph.addEdge(p1, p2, edge);
            shapeGraph.setEdgeWeight(edge, 1);
        }
    }

    private PointHingeMap[] getSortedList(boolean checkOnXAxis, ArrayList<PointHingeMap> intersections) {
        PointHingeMap[] list = new PointHingeMap[intersections.size()];
        int n = intersections.size();
        for (int i=0; i<n; i++){
            PointHingeMap ph = findMin(intersections, checkOnXAxis);
            list[i]=ph;
            intersections.remove(ph);
        }
        return list;
    }

    private PointHingeMap findMin(ArrayList<PointHingeMap> intersections, boolean checkOnXAxis) {
        PointHingeMap min = intersections.get(0);
        for (int i=1; i<intersections.size(); i++){
            int minDim = min.point.x;
            int phDim = intersections.get(i).point.x;
            if (!checkOnXAxis){
                minDim = min.point.y;
                phDim = intersections.get(i).point.y;
            }
            if (minDim>phDim)
                min = intersections.get(i);
        }
        return min;
    }

    /*Coloring part*/
    public static Color toNaturalColor(Color mix) {
        Random random = new Random();
        int red = random.nextInt(127);
        int green = random.nextInt(127);
        int blue = random.nextInt(127);

        // mix the color
        if (mix != null) {
            red = (red + mix.getRed()) / 2;
            green = (green + mix.getGreen()) / 2;
            blue = (blue + mix.getBlue()) / 2;
        }

        Color color = new Color(red, green, blue, 128);
        return color;
    }

    public static Color[] generateColorPallette(int number) {

        Color[] pallette = new Color[number];
        int j = 0;
        Random rand = new Random();

        for (int i = 0; i < number; i++) {
            HSLColor c = new HSLColor(i * 360 / number, /*90 + rand.nextFloat() * 10*/ 10, /*50 + rand.nextFloat() * 10*/ 70);
            /*c.hue = i;
            c.saturation = 90 + randf() * 10;
            c.lightness = 50 + randf() * 10;
             */
            pallette[(j + 0) % number] = toNaturalColor(c.getRGB());
            j++;
        }
        return pallette;
    }    
    public static Color[] pallette = generateColorPallette(50);
    /*End Coloring part*/
    public ArrayList<OrigamiSurface> getColoredSurfaces(){
        ArrayList<OrigamiSurface> surfaceList = getSurfaces();
        for (OrigamiSurface s : surfaceList) {                
            s.setColor(pallette[surfaceList.indexOf(s) % 50]);
        }
        return surfaceList;
    }
    
    public ArrayList<OrigamiSurface> getSurfaces() {
        ArrayList<OrigamiSurface> res  = new ArrayList<>();
        //System.out.println("###########Debug surfaces");
        //reset weights and visits to vertises and edges
        for (Point2D p:shapeGraph.vertexSet()){
             p.visits=0;
        }
        for (OrigamiEdge edge: shapeGraph.edgeSet()){
            shapeGraph.setEdgeWeight(edge, 1);
            edge.reset();
        }
        int MAX_WEIGHT = Integer.MAX_VALUE;
        //repeat until no path is returned.
        int surfaceCounter = 0;
        while (true){
            //1-find v1 : a most external vertex (number of outs-visits is minimal)
            Point2D v1 = null;
            int extValue = MAX_WEIGHT;
            //System.out.println("Ext value for ");
            for (Point2D p:shapeGraph.vertexSet()){
                int pExtVal = shapeGraph.outDegreeOf(p)-p.visits;
                //System.out.print(p+":"+pExtVal+", ");
                if (pExtVal>0 && pExtVal<extValue){
                    extValue = pExtVal;
                    v1=p;
                }                
            }
            //System.out.println();
            if (v1==null)
                return res;
            v1.visits ++;
            //2-get v2: the next point
            OrigamiEdge v1v2 = null;
            OrigamiEdge v2v1 = null;
            Point2D v2 = null;
            Set<OrigamiEdge> outEdges = shapeGraph.outgoingEdgesOf(v1);
            if (outEdges==null )//should continue the loop
                continue;
            for (OrigamiEdge edge: outEdges){
                //System.out.println("..Test edge:"+edge);
                if (shapeGraph.getEdgeWeight(edge)!=MAX_WEIGHT){
                    Point2D endEdge = (Point2D)edge.getV2();

                    int endEdgeExtVal = shapeGraph.outDegreeOf(endEdge)-endEdge.visits;
                    if (endEdgeExtVal>0){
                        v1v2 = edge;
                        //System.out.println("Selected edge:"+v1v2);
                        break;
                    }
                }

            }
            if (v1v2==null)//should continue the loop
                continue;
            v2 = (Point2D)v1v2.getV2();
            v2v1 = shapeGraph.getEdge(v2, v1);                

            //3-save the weight of v2->v1, if the edge exists. Set the weight of v2->v1 to max value.
            double Wv2v1 = MAX_WEIGHT;
            if (v2v1!=null){
                Wv2v1 = shapeGraph.getEdgeWeight(v2v1);
                shapeGraph.setEdgeWeight(v2v1, MAX_WEIGHT);
            }
            //4- find shortest path.            
            List<OrigamiEdge> path = DijkstraShortestPath.findPathBetween(shapeGraph, v2, v1);
            if (path!=null){
                //System.out.println("2D::Path from "+v2+" to "+ v1+" is:\n "+path);
                OrigamiSurface surf = new OrigamiSurface(""+surfaceCounter++);
                if (this.surfaceAttributeMap.get(surf.internalId)==null)
                    this.surfaceAttributeMap.put(surf.internalId, new OrigamiSurfaceAttributes(surf));
                else
                    this.surfaceAttributeMap.get(surf.internalId).surface = surf;
                this.surfaceAttributeMap.get(surf.internalId).applyAttributes();
                surf.points = new Point2D[path.size()+1];
                surf.points[0] = v1;
                int i=1;
                for (OrigamiEdge edge: path){
                    shapeGraph.setEdgeWeight(edge, MAX_WEIGHT);
                    surf.points[i] = (Point2D)edge.getV1();
                    //5-increase the number of visits per vertex on the path
                    surf.points[i].visits++;
                    i++;
                }
                surf.ensureClockwise();
                v1v2.addSurface(surf);
                for (OrigamiEdge edge: path){
                    //System.out.println("2D::add surf "+surf.getId()+" to edge "+ edge.toString());
                    edge.addSurface(surf);
                }
                res.add(surf);
            }            
            //6-restore the old weight of v2->v1, if exisits
            if (v2v1!=null){
                shapeGraph.setEdgeWeight(v2v1, Wv2v1);
            }

            //7-set the weight of v1->v2 to a maximum value
            shapeGraph.setEdgeWeight(v1v2, MAX_WEIGHT);
        }
        //return res;
    }

    public int shiftX = 100;
    public int shiftY = 100;

    Point2D clicked3DPoint = null;
    public Point2D getClicked3DPoint() {
        return clicked3DPoint;
    }
    public void setClicked3DPoint(Point2D point, String surfaceName){
        if (this.origamiHingeEventListener!=null)
            for (OrigamiHingeEventListener listener: origamiHingeEventListener){
                listener.onMouseClickedEvent(point, surfaceName);
            }
        //clicked3DPoint = point;
    }

    public TreeMap<String, OrigamiSurfaceAttributes> surfaceAttributeMap = new TreeMap<>();
    public TreeMap<String, OrigamiEdgeAttributes> edgeAttributeMap = new TreeMap<>();
    public String selectedSurface1;
    public String selectedSurface2;
    public void selectClickedSurface(int x, int y) {
       OrigamiSurface surf = null;
       for (OrigamiSurface s: getSurfaces()){
           if (s.contains(x,y)){
               surf = s;
               break;
           }
       }
       if (surf!=null){
           if (selectedSurface1==null){
               selectedSurface1 = surf.internalId;
               selectedSurface2 = null;
               return;
           }
           selectedSurface2 = selectedSurface1;
           selectedSurface1 = surf.internalId;
       }
    }

    private void updateEdgeAttributesMap() {
    }

    public OrigamiEdgeAttributes getEdgeAttribute(String internalId) {
        return edgeAttributeMap.get(internalId);
    }

    public void setClicked3DPoint(Point2D point) {
        this.clicked3DPoint = point;
    }

    static class PointHingeMap{
        public Point2D point;
        public OrigamiEdge hinge;
        public PointHingeMap(Point2D point, OrigamiEdge edge){
            this.point = point;
            this.hinge = edge;
        }

        @Override
        public String toString() {
            return point.toString();
        }
        
    }
    public void addHinge(Point2D p1, Point2D p2, String hingeType) {
    
        //Check lines intersect:
        ArrayList<PointHingeMap> intersections = new ArrayList<>();
        for (OrigamiEdge edge : shapeGraph.edgeSet()) {
            Point2D p3 = shapeGraph.getEdgeSource(edge);
            Point2D p4 = shapeGraph.getEdgeTarget(edge);
            Point2D [] intersectArr = intersect(p1.x,p1.y, p2.x,p2.y,p3.x,p3.y,p4.x,p4.y);
            if (intersectArr!=null){
                if (intersectArr.length==1){
                    //System.out.println("CROSS: edge "+p3+"->"+p4+" on "+intersectArr[0]);
                    intersections.add(new PointHingeMap(intersectArr[0], edge));
                }
            }
        }
        //System.out.println("intersection:"+intersections);

        int dx = Math.abs(p1.x-p2.x);
        int dy = Math.abs(p1.y-p2.y);
        PointHingeMap[] sortedIntersect = getSortedList(dx>dy, intersections);
        if (sortedIntersect!=null)
            for (int i=0; i<sortedIntersect.length; i++){
                //System.out.print(i+":"+sortedIntersect[i].point);
                sortedIntersect[i].point = addPoint(sortedIntersect[i].point);
                OrigamiEdge crossedEdge = sortedIntersect[i].hinge;
                Point2D edgeP1 = shapeGraph.getEdgeSource(crossedEdge);
                Point2D edgeP2 = shapeGraph.getEdgeTarget(crossedEdge);
                if (sortedIntersect[i].point!=edgeP1 &&sortedIntersect[i].point!=edgeP2){
                    addEdge(edgeP1, sortedIntersect[i].point, crossedEdge.getType());//TODO: copy properties of edge into this edge
                    addEdge(sortedIntersect[i].point, edgeP2, crossedEdge.getType());//TODO: copy properties of edge into this edge
                    shapeGraph.removeEdge(crossedEdge);
                }

                
                //System.out.println(" corrected as:"+sortedIntersect[i].point);
            }
            for (int i=0; i<sortedIntersect.length-1; i++){
                addEdge(sortedIntersect[i].point,sortedIntersect[i+1].point, hingeType);//TODO: Add a bi-directional edge of the same type of the hinge   
                addEdge(sortedIntersect[i+1].point,sortedIntersect[i].point, hingeType);
                //new OrigamiEdge<String>("James", "John", enemy)
            }
    }

/*    public static void main(String[] args) {
        OrigamiShape shape = new OrigamiShape();
        int start=100;
        int end = 300;
        Point2D p00 = shape.addPoint(new Point2D(start, start));
        Point2D p10 = shape.addPoint(new Point2D(start, end));
        Point2D p11 = shape.addPoint(new Point2D(end, end));
        Point2D p01 = shape.addPoint(new Point2D(end, start));
        shape.addEdge(p00, p01);
        shape.addEdge(p01, p11);
        shape.addEdge(p11, p10);
        shape.addEdge(p10, p00);
        shape.addHinge(new Point2D(100,100), new Point2D(300,300), "vally");
        //shape.addHinge(new Point2D(100,120), new Point2D(300,120), "vally");
        
        //shape.addHinge(new Point2D(50, 0),new  Point2D(25, 100), "vally");
        //shape.addHinge(new Point2D(62,133), new Point2D(484,279), "vally");
        System.out.println(shape.shapeGraph.toString());
    }
*/
}
