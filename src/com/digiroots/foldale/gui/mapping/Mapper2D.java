/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui.mapping;

import java.awt.Component;
import java.awt.image.BufferedImage;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.OrigamiShape;

/**
 *
 * @author khaddam
 */
public interface Mapper2D {
    TextureInfo getTextureImage(String surfaceName);
    void runGUI(OrigamiShape shape);
    void exitGUI();
    Component getComponent(String surfaceName);
    OrigamiHingeEventListener getHingeEventListener();    
}
