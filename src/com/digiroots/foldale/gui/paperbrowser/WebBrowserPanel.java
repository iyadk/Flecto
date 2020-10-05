/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui.paperbrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.concurrent.Worker.State.FAILED;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.digiroots.foldale.gui.mapping.Capturable;
import com.digiroots.foldale.gui.mapping.TextureInfo;

/**
 *
 * @author khaddam
 */
public class WebBrowserPanel extends JPanel implements Capturable{
    private JFXPanel jfxPanel;
    private WebEngine engine;

    //private JPanel panel = new JPanel();

    public WebBrowserPanel(){
        initComponents();
    }
    public WebBrowserPanel(String page){
        this.pageUrl = page;
        initComponents();
        //setPage(page);
    }
    String pageUrl = "";
    public void setPage(String url){
        pageUrl = url;
        loadURL(pageUrl);
    }
    public void initComponents() {
        this.setLayout(new BorderLayout());
        jfxPanel = new JFXPanel();
        createScene();
        this.add(jfxPanel, BorderLayout.CENTER);
        loadURL(pageUrl);
    }

    private boolean engineLoaded = false;
    private void createScene() {
        engineLoaded = false;
        Platform.runLater(new Runnable() {
            @Override public void run() {

                WebView view = new WebView();
                engine = view.getEngine();
                engine.getLoadWorker();

                jfxPanel.setScene(new Scene(view));
                engineLoaded = true;
            }
        });
        while(!engineLoaded){
              try {
                  System.out.println("wait for engine");
                  Thread.sleep(1000l);
              } catch (InterruptedException ex) {
                  Logger.getLogger(WebBrowserPanel.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
    }

    boolean pageLoaded = false;
    private String panelUrl;
    public void loadURL(final String url) {
        panelUrl = url;
        pageLoaded = false;
        Platform.runLater(new Runnable() {
            @Override public void run() {
                System.out.println("..start loading "+url);
                String tmp = toURL(url);
                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }
                engine.load(tmp);
                bufferedScreenShot = null;
                pageLoaded = true;
                System.out.println("page load thread done for url:"+url);
            }
        });
        /*while(!pageLoaded){
              try {
                  System.out.println("wait for page");
                  Thread.sleep(1000l);
              } catch (InterruptedException ex) {
                  Logger.getLogger(WebBrowserPanel.class.getName()).log(Level.SEVERE, null, ex);
              }
        }*/
        //System.out.println(url+" loaded ");    
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
                return null;
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    public void invalidate() {
        super.invalidate(); //To change body of generated methods, choose Tools | Templates.
        bufferedScreenShot = null;
        System.out.println("invalidate "+panelUrl );
    }
    

    
    BufferedImage bufferedScreenShot = null;
    private boolean isVisibleParent(){
        if (!isVisible())
            return false;
        Container p = this.getParent();
        while (p!=null){
            if (!p.isVisible())
                return false;
            p = p.getParent();
        }
        return true;
            
    }
    @Override
    public BufferedImage getScreenShot() {
        /*if (bufferedScreenShot!=null){
            return bufferedScreenShot;
        }*/
        if (!isVisibleParent())
            return null;
        /*if (this.getWidth()==0 || this.getHeight()==0)
                return null;
        System.out.println("recalculate screenshot for "+panelUrl);
*/
        while(!pageLoaded || !this.isValid()){
              try {
                  System.out.println("wait for page "+panelUrl);
                  Thread.sleep(200l);
              } catch (InterruptedException ex) {
                  Logger.getLogger(WebBrowserPanel.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        bufferedScreenShot = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);        
        Graphics g = bufferedScreenShot.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        // call the Component's paint method, using
        // the Graphics object of the image.
        this.paint( g ); 
        if (bufferedScreenShot==null){
            System.out.println("screenshot is null for page"+panelUrl);
            return null;
        }
        try{
            File outputfile = new File(panelUrl.replace("file:///C:/OrigamiProjects/", "")+"saved.png");
            ImageIO.write(bufferedScreenShot, "png", outputfile);
        }
        catch (IOException e) {
            e.printStackTrace();            
        }
        return bufferedScreenShot;
    }
    public void refreshScreenShort(){
        bufferedScreenShot = null;
    }
    
}
