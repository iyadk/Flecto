/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.training.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author khaddam
 */
public class OrigamiSurfaceEditor extends JPanel {

    int surfaceNumber = 1;
    OrigamiShape shape;
    OrigamiSurfaceAttributes surface = null;
    JTextField txtId;
    public OrigamiSurfaceEditor(OrigamiShape shape, String title, int surfaceNumber) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(250,60));
        this.setBackground(Color.WHITE);
        
        this.shape=shape;
        this.surfaceNumber = surfaceNumber;
        txtId = new JTextField();
        txtId.setColumns(10);
        //txtId.setPreferredSize(new Dimension(50, 25));
        //txtId.setLocation(20,50);
        txtId.setBounds(60,30,100,25);
        JLabel lblTitle = new JLabel(title);      
        lblTitle.setBounds(10, 10, 230, 25);
        //lblTitle.setLocation(10, 10);
        //lblTitle.setPreferredSize(new Dimension(100, 25));
        lblTitle.setFont(lblTitle.getFont().deriveFont(16f));
        this.add(lblTitle);
        
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel lblId = new JLabel("id");
        lblId.setBounds(10, 30, 50, 25);
        //lblId.setLocation(10, 30);
        //lblId.setPreferredSize(new Dimension(50, 25));

        this.add(lblId);
        
        this.add(txtId);
        
        txtId.getDocument().addDocumentListener(new DocumentListener() {
          public void changedUpdate(DocumentEvent e) {
            warn();
          }
          public void removeUpdate(DocumentEvent e) {
            warn();
          }
          public void insertUpdate(DocumentEvent e) {
            warn();
          }

          public void warn() {
              if (txtId.getText().isEmpty())
                  return;
              surface.id = txtId.getText();
              surface.applyAttributes();
          }
        });        
    }

    void refresh() {
        if (surfaceNumber == 1 && shape.selectedSurface1!=null)
            surface = shape.surfaceAttributeMap.get(shape.selectedSurface1);
        if (surfaceNumber == 2 && shape.selectedSurface2!=null)
            surface = shape.surfaceAttributeMap.get(shape.selectedSurface2);
        bindGUI();
    }

    private void bindGUI() {
        if (surface==null)
            return;
        txtId.setText(surface.id);
    }
    
}
