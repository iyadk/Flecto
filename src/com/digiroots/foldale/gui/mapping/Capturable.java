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
public interface Capturable {
    BufferedImage getScreenShot();
    void refreshScreenShort();
}
