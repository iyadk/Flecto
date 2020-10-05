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
public interface OrigamiHingeEventListener {
    void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event);

    public void onMouseClickedEvent(Point2D point, String surfaceName);
    
}
