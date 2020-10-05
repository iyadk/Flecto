/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training;

import java.util.ArrayList;

/**
 *
 * @author khaddam
 */
public class NormalizedSurface {
public static void main(String [] args){
    /* Un sommet(1,1,0) est representé comme un vecteur de longueur 3. Ex: {1,1,0}.
    Pour stocker l'ensemble des sommets, il faut prévoir un tableau. Alors, l'ensemble des sommets peut être représenté comme une matrice pour laquelle
    chaque ligne contient un sommet, et les colonnes contiennent les composantes x,y,z du sommet.
    */
    int[][] vertex = {{-1,0,0},{0,0,1},{1,0,0},{0,1,0}};
    /* Un triangle est un vecteur composé de 3 sommets: a,b,c. Puisque l'on dispose déjà d'un tableau de sommets, on stock seulement l'indice du sommet dans les données d'un triangle. 
    Example: le triangle {2,3,1} est le triangle définit par les sommets:{1,0,0},{0,1,0},{0,0,1}. L'ordre des sommets dans le vecteur triangle est important!
    Pour stocker l'ensemble des triangles, il faut prévoir un tableau. Alors, l'ensemble des triangles peut être représenté comme une matrice pour laquelle
    chaque ligne contient un triangle, et les colonnes contiennent les sommet a,b,c du triangle.
    */
    int [][] triangle = {{2,3,1},{1,3,0},{0,3,2},{2,1,0}};
    System.out.println("Les sommets:");
    for (int i=0; i<vertex.length; i++){
        System.out.println(" Sommet numéro "+i+": x="+vertex[i][0]+",y="+vertex[i][1]+",z="+vertex[i][2]);
    }

    for (int i=0; i<triangle.length; i++){
        int a = triangle[i][0];
        int b = triangle[i][1];
        int c = triangle[i][2];            
        System.out.println("Triangle numéro "+i+" contient les indices des vertex suivantes: {"+a+","+b+ ","+c+ "}");
        System.out.println(" et contient les vertex suivantes: {"+triangle[i][0]+","+triangle[i][1]+ ","+triangle[i][2]+ "}");
        System.out.println(" vertex a: x="+vertex[a][0]+",y="+vertex[a][1]+",z="+vertex[a][2]);
        System.out.println(" vertex b: x="+vertex[b][0]+",y="+vertex[b][1]+",z="+vertex[b][2]);
        System.out.println(" vertex c: x="+vertex[c][0]+",y="+vertex[c][1]+",z="+vertex[c][2]);
    }

    ArrayList<Integer> triangleVisible = surfaceVisible(vertex, triangle);

    System.out.println("Les triangle visibles sont les suivants");
    for (int i=0; i<triangleVisible.size(); i++){
        System.out.println("Triangle numéro "+ triangleVisible.get(i));
    }       
}

    public static int[] produitVectoriel(int[] u, int[] v ){ //#1 points       
        int[] c = new int[3];  
        c[0] = u[1]*v[2]-u[2]*v[1];
        c[1] = u[2]*v[0]-u[0]*v[2];
        c[2] = u[0]*v[1]-u[1]*v[0];        
        return c;
    }
    private static ArrayList<Integer> surfaceVisible(int[][] vertex, int[][] triangle) {
        // init ArrayList to return: 0.5 points
        ArrayList<Integer> result = new ArrayList<>(); 
        // For loop: 0.5 points
        for (int i=0; i<triangle.length; i++){
            /*Get the three verteces: 1 point */
            int a = triangle[i][0];
            int b = triangle[i][1];
            int c = triangle[i][2];
            int[] p0 = vertex[a];
            int[] p1 = vertex[b];
            int[] p2 = vertex[c];
            /*Calc vectors: 2 points + prod. vect.*/
            int[] v01 = {p1[0]-p0[0],p1[1]-p0[1],p1[2]-p0[2]};
            int[] v12 = {p2[0]-p1[0],p2[1]-p1[1],p2[2]-p1[2]};
            int[] norme = produitVectoriel(v01, v12);
            if (norme[2]>0){ /*Condition: 0.5 points */
                result.add(i);  // 0.5 point
            }
        }
        return result;
    }
    
    private static void updateMesh(int[][] vertex, int[][] triangle) {
        for (int i=0; i<triangle.length; i++){
            int[] p0 = vertex[triangle[i][0]];
            int[] p1 = vertex[triangle[i][1]];
            int[] p2 = vertex[triangle[i][2]];
            int[] v01 = {0,0,0};
            v01[0] = p1[0]-p0[0];
            v01[1] = p1[1]-p0[1];
            int[] v12 = {0,0,0};
            v12[0] = p2[0]-p1[0];
            v12[1] = p2[1]-p1[1];
            int[] norme = produitVectoriel(v01, v12);
            if (norme[2]<0){
                int aux = triangle[i][0];
                triangle[i][0] = triangle[i][1];
                triangle[i][1] = aux;
            }
        }
    
    }
}
