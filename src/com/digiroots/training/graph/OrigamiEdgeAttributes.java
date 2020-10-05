/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author khaddam
 */
public class OrigamiEdgeAttributes  implements Serializable{
    public String id;
    String surf1; 
    String surf2;
    public ArrayList<OrigamEdgeEvent> eventList = new ArrayList<>();
    public OrigamiEdgeAttributes(String surf1, String surf2) {
        this.surf1 = surf1;
        this.surf2 = surf2;
    }
    public void addEvent(OrigamEdgeEvent event){
        eventList.add(event);
    }
    public void addEvent(String name){
        eventList.add(new OrigamEdgeEvent(name));
    }
    public void removeEvent(OrigamEdgeEvent event){
        eventList.remove(event);
    }

    public static class OrigamEdgeEvent  implements Serializable{
        public int angle;
        public int direction;
        public boolean active=false;
        public String name;
        public OrigamEdgeEvent(String name) {
            angle = 180;
            direction=1;
            this.name = name;
        }
        public String getDirectionName(){
            switch (direction){
                case 1: return "open";
                case -1: return "close";
                case 0: return "both";
            }
            return "";
        }
        public void setDirection(String name){
            switch(name){
                case "open": direction = 1;break;
                case "close": direction = -1;break;
                case "both": direction = 0;break;
            }
        }

        @Override
        public String toString() {
            return "Event: "+name+ getDirectionName()+" at "+angle;
        }
        
    }
}
