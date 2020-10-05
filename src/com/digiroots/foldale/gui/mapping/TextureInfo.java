/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui.mapping;

import java.awt.image.BufferedImage;

/**
 *
 * @author khaddam
 */
public class TextureInfo {
    public BufferedImage textureImg;
    public boolean hideFront;
    public boolean hideBack;
    public TextureInfo(BufferedImage textureImg, boolean hideUpperFace, boolean hideLowerFace){
        this.hideBack = hideLowerFace;
        this.hideFront = hideUpperFace;
        this.textureImg = textureImg;
    }
}
