/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.foldale.gui.paperbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author khaddam
 */
public class ScientificPaperBrowser extends javax.swing.JFrame {

    /**
     * Creates new form ScientificPaperBrowser
     */
    int currentState =0;
    int currentPage = 2;
    int FrameWidth = 700;//567;
    int FrameHeight = (int)Math.round(FrameWidth/0.707) ;//848;//801;
    /*
    State desc and trans:
    State 0: initial. The title is only visible
    0->1
    State 1: Abstract and toc are visible in the same panel? References is visible in a separate panel
    1->2
    State 2: browse pages. Stay in the same state when navigating among pages.
    2->1
    1->0
    
    */
    com.digiroots.foldale.gui.paperbrowser.WebBrowserPanel headerPanel;
    com.digiroots.foldale.gui.paperbrowser.WebBrowserPanel abstractPanel;
    com.digiroots.foldale.gui.paperbrowser.WebBrowserPanel referencePanel;
    com.digiroots.foldale.gui.paperbrowser.WebBrowserPanel tocPanel;
    com.digiroots.foldale.gui.paperbrowser.WebBrowserPanel pagePanel;
    JPanel abstractAndTocPanel = new JPanel(new BorderLayout());
    JPanel dummyPanel = new JPanel(new BorderLayout());

    
    public ScientificPaperBrowser() {
        initComponentsUpdated();
        initPanels();
        renderState(0);
    }
    private void initPanels() {
        headerPanel = new WebBrowserPanel("file:///C:/OrigamiProjects/header.html");
        headerPanel.setPreferredSize(new Dimension(FrameWidth/2, FrameHeight/2));
        abstractPanel = new WebBrowserPanel("file:///C:/OrigamiProjects/Abstract.xhtml");
        abstractPanel.setPreferredSize(new Dimension(FrameWidth/2, FrameHeight/2));
        referencePanel = new WebBrowserPanel("file:///C:/OrigamiProjects/References.html");
        referencePanel.setPreferredSize(new Dimension(FrameWidth/2, FrameHeight/2));
        tocPanel = new WebBrowserPanel("file:///C:/OrigamiProjects/TOC.html");
        tocPanel.setPreferredSize(new Dimension(FrameWidth/2, FrameHeight/2));
        pagePanel = new WebBrowserPanel("file:///C:/OrigamiProjects/p2.html");
        pagePanel.setPreferredSize(new Dimension(FrameWidth, FrameHeight));
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setPreferredSize(new Dimension(FrameWidth, FrameHeight));
        abstractAndTocPanel.add(abstractPanel, BorderLayout.NORTH);        
        abstractAndTocPanel.add(tocPanel, BorderLayout.SOUTH); 
        dummyPanel.setPreferredSize(new Dimension(FrameWidth/2, FrameHeight/2));
    }    
    public void renderState(int state){
        switch(state){
            case 0: renderHeader(); 
                    btnPrevPage.setVisible(false);
                    btnNextPage.setVisible(false);
                    btnMore.setVisible(true);
                    btnReadArticle.setVisible(false);
                    btnBack.setVisible(false);
                break;
            case 1: renderAbstractAndTocAndRef(); 
                    btnPrevPage.setVisible(false);
                    btnNextPage.setVisible(false);
                    btnMore.setVisible(false);
                    btnReadArticle.setVisible(true);
                    btnBack.setVisible(true);
                break;
            case 2: renderPage(); 
                    btnPrevPage.setVisible(true);
                    btnNextPage.setVisible(true);
                    btnMore.setVisible(false);
                    btnReadArticle.setVisible(false);
                    btnBack.setVisible(true);
                break;
        }
    }
    private void initAllPanels(boolean value){
        headerPanel.setVisible(value);
        abstractAndTocPanel.setVisible(value);
        referencePanel.setVisible(value);
        pagePanel.setVisible(value);     
        dummyPanel.setVisible(value);
        
    }
    private void renderHeader() {
        initAllPanels(false);
        headerPanel.setVisible(true);
        dummyPanel.setVisible(true);
        //jPanel1.removeAll();
        //jPanel1.setLayout(new BorderLayout());
        jPanel1.add(headerPanel, BorderLayout.CENTER);        
        jPanel1.add(dummyPanel, BorderLayout.EAST);        
    }

    private void renderAbstractAndTocAndRef() {
        initAllPanels(false);
        abstractAndTocPanel.setVisible(true);
        //jPanel1.removeAll();
        //jPanel1.setLayout(new BorderLayout());
        jPanel1.add(abstractAndTocPanel, BorderLayout.CENTER);        
        referencePanel.setVisible(true);
        jPanel1.add(referencePanel, BorderLayout.EAST);        
    }

    private void renderPage() {
        initAllPanels(false);
        //jPanel1.removeAll();
        //jPanel1.setLayout(new BorderLayout());
        pagePanel.setVisible(true);
        pagePanel.setPage("file:///C:/OrigamiProjects/p"+currentPage+".html");
        //pagePanel.setPreferredSize(new Dimension(FrameWidth, FrameHeight));
        jPanel1.add(pagePanel, BorderLayout.CENTER);        
    }
    
     void changeState(int newState) {
        if (acceptableTrans(currentState, newState))
            currentState = newState;
        renderState(currentState);
        jPanel1.repaint();
        jPanel1.revalidate();
    }
    
    private boolean acceptableTrans(int currentState, int newState) {
        switch(currentState){
            case 0: return newState==1;
            case 1: return newState==2 || newState==0;
            case 2: return newState==1 ;
        }
        return false;
    }

    private void initComponentsUpdated() {

        jPanel1 = new javax.swing.JPanel();
        btnMore = new javax.swing.JButton();
        btnReadArticle = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnNextPage = new javax.swing.JButton();
        btnPrevPage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(FrameWidth, FrameHeight));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );

        btnMore.setText("More");
        btnMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoreActionPerformed(evt);
            }
        });

        btnReadArticle.setText("Read article");
        btnReadArticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadArticleActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNextPage.setText("Next page");
        btnNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextPageActionPerformed(evt);
            }
        });

        btnPrevPage.setText("Previous page");
        btnPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevPageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMore)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)
                        .addGap(30, 30, 30)
                        .addComponent(btnReadArticle)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrevPage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNextPage))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, FrameWidth, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMore)
                    .addComponent(btnReadArticle)
                    .addComponent(btnBack)
                    .addComponent(btnNextPage)
                    .addComponent(btnPrevPage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, FrameHeight, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnMore = new javax.swing.JButton();
        btnReadArticle = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnNextPage = new javax.swing.JButton();
        btnPrevPage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(567, 801));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );

        btnMore.setText("More");
        btnMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoreActionPerformed(evt);
            }
        });

        btnReadArticle.setText("Read article");
        btnReadArticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadArticleActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNextPage.setText("Next page");
        btnNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextPageActionPerformed(evt);
            }
        });

        btnPrevPage.setText("Previous page");
        btnPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevPageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMore)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)
                        .addGap(30, 30, 30)
                        .addComponent(btnReadArticle)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrevPage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNextPage))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMore)
                    .addComponent(btnReadArticle)
                    .addComponent(btnBack)
                    .addComponent(btnNextPage)
                    .addComponent(btnPrevPage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoreActionPerformed
        changeState(1);
    }//GEN-LAST:event_btnMoreActionPerformed

    private void btnReadArticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadArticleActionPerformed
        changeState(2);
    }//GEN-LAST:event_btnReadArticleActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        
        changeState(currentState-1);

    }//GEN-LAST:event_btnBackActionPerformed

    private void btnPrevPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevPageActionPerformed
        prevPage();
    }//GEN-LAST:event_btnPrevPageActionPerformed

    private void btnNextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextPageActionPerformed
        nextPage();
    }//GEN-LAST:event_btnNextPageActionPerformed

    public void nextPage(){
        currentPage++;
        renderState(currentState);
    }
    public void prevPage(){
        currentPage--;
        renderState(currentState);        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScientificPaperBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScientificPaperBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScientificPaperBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScientificPaperBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScientificPaperBrowser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnMore;
    private javax.swing.JButton btnNextPage;
    private javax.swing.JButton btnPrevPage;
    private javax.swing.JButton btnReadArticle;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables





}
