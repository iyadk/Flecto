/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.io.Serializable;

/**
 *
 * @author khaddam
 */
public class OrigamiSurfaceAttributes implements Serializable{
    public String id;
    public transient OrigamiSurface surface;

    public OrigamiSurfaceAttributes(OrigamiSurface surf) {
        this.surface = surf;
        copyAttributes();
    }
    public void applyAttributes(){
        surface.setId(id);
    }

    private void copyAttributes() {
        id = surface.getId();
    }
}
