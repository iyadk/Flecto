/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiShape;
import com.digiroots.training.graph.OrigamiSurfaceAttributes;

/**
 *
 * @author khaddam
 */
public class OrigamiHingeEditor extends javax.swing.JPanel {

    OrigamiShape shape;
    OrigamiSurfaceAttributes surfAtt1 = null;
    OrigamiSurfaceAttributes surfAtt2 = null;
    /**
     * Creates new form OrigamiHingeEditor
     */
    public OrigamiHingeEditor() {
        initComponents();
    }
    public void init(OrigamiShape shape) {
        this.shape = shape;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.add(lblNoHingeSelected);
        this.add(btnCustomizeEdge);
        btnCustomizeEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                attr = new OrigamiEdgeAttributes(shape.selectedSurface1,shape.selectedSurface2);
                shape.edgeAttributeMap.put(shape.selectedSurface1+"#"+shape.selectedSurface2, attr);
                shape.edgeAttributeMap.put(shape.selectedSurface2+"#"+shape.selectedSurface1, attr);
                refresh();
            }
        });
        btnCustomizeEdge.setVisible(false);
        this.add(btnAddEvent);
        btnAddEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) { 
                String hingeDefaultName = shape.selectedSurface1+"#"+shape.selectedSurface2;
                //OrigamiEdgeAttributes attr = shape.edgeAttributeMap.get(attrKey);
                attr.addEvent(hingeDefaultName);
                refresh();
            }
        });
        btnAddEvent.setVisible(false);   
        this.add(eventPanel);
        eventPanel.setVisible(false);
    }

    void refresh() {
        lblNoHingeSelected.setVisible(false);
        btnCustomizeEdge.setVisible(false);
        btnAddEvent.setVisible(false);        
        eventPanel.setVisible(false);

        if (shape.selectedSurface1!=null)
            surfAtt1 = shape.surfaceAttributeMap.get(shape.selectedSurface1);
        if (shape.selectedSurface2!=null)
            surfAtt2 = shape.surfaceAttributeMap.get(shape.selectedSurface2);
        if (surfAtt1==null || surfAtt2==null){
            lblNoHingeSelected.setVisible(true);
            return;
        }
        String attrKey = shape.selectedSurface1+"#"+shape.selectedSurface2;
        if (surfAtt1.surface.hasCommonEdge(surfAtt2.surface)){
            String s = "";
            for (String key: shape.edgeAttributeMap.keySet()){
                s +=key+",";
            }
            System.out.println("Keys in edgeAttrMap are:"+s);
            if (!shape.edgeAttributeMap.containsKey(attrKey)){
                        btnCustomizeEdge.setVisible(true);
                        System.out.println("Hinge is not customized");
                        return;
            }
        }
        else{
            System.out.println("No hinge is defined");
            return;
        }
        //Now we are ready to enable adding new events..
        btnAddEvent.setVisible(true);        

        attr = shape.edgeAttributeMap.get(attrKey);
        eventPanel.setVisible(true);
        eventPanel.removeAll();
        
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.add(createPanelForTitles());
        for (OrigamiEdgeAttributes.OrigamEdgeEvent e: attr.eventList){
            eventPanel.add(createPanelForEvent(e));
        }
    }
    private OrigamiEdgeAttributes attr;
    private JPanel createPanelForTitles() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        JLabel lblName = new JLabel("Name");
        p.add(lblName);
        p.add(new JLabel("     "));
        JLabel lblAngle = new JLabel("angle(0°-180°)");
        p.add(lblAngle);
        JLabel lblDir = new JLabel("Direction");
        p.add(lblDir);
        p.add(new JLabel("                   "));


        return p;
    }
    private JPanel createPanelForEvent(OrigamiEdgeAttributes.OrigamEdgeEvent e) {
        final OrigamiEdgeAttributes.OrigamEdgeEvent evnt = e;
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        /*JLabel lblAngle = new JLabel("angle(0°-180°):");
        p.add(lblAngle);
*/
        JTextField txtName = new JTextField();
        txtName.setText(""+evnt.name);
        txtName.getDocument().addDocumentListener(new DocumentListener() {
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
              try{
                  if (txtName.getText().isEmpty())
                      return;
                  evnt.name = txtName.getText();
              }
              catch(NumberFormatException e){;}
              
          }
        }); 
        txtName.setColumns(5);
        p.add(txtName);

        
        JTextField txtAngle = new JTextField();
        txtAngle.setText(""+evnt.angle);
        txtAngle.getDocument().addDocumentListener(new DocumentListener() {
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
              try{
                  if (txtAngle.getText().isEmpty())
                      return;
                  evnt.angle = Integer.parseInt(txtAngle.getText());
              }
              catch(NumberFormatException e){;}
              
          }
        }); 
        txtAngle.setColumns(5);
        p.add(txtAngle);
        /*JLabel lblDir = new JLabel("Direction");
        p.add(lblDir);
        */
        JComboBox compDirection = new JComboBox(new String[]{"open", "close", "both"});
        compDirection.setSelectedItem(evnt.getDirectionName());
        compDirection.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JComboBox combo = (JComboBox)e.getSource();
                        String directionStr = (String)combo.getSelectedItem();
                        evnt.setDirection(directionStr);
                    }
                }            
        );
        p.add(compDirection);
        
        JButton btnRemove = new JButton("remove");
        p.add(btnRemove);
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) { 
                attr.removeEvent(evnt);
                refresh();
            }
        });
        
        
        return p;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNoHingeSelected = new javax.swing.JLabel();
        btnCustomizeEdge = new javax.swing.JButton();
        btnAddEvent = new javax.swing.JButton();
        eventPanel = new javax.swing.JPanel();

        lblNoHingeSelected.setText("No hinge is selected");

        btnCustomizeEdge.setText("Customize hinge");

        btnAddEvent.setText("Add a hinge avent");

        eventPanel.setAutoscrolls(true);

        javax.swing.GroupLayout eventPanelLayout = new javax.swing.GroupLayout(eventPanel);
        eventPanel.setLayout(eventPanelLayout);
        eventPanelLayout.setHorizontalGroup(
            eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        eventPanelLayout.setVerticalGroup(
            eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eventPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoHingeSelected)
                            .addComponent(btnCustomizeEdge)
                            .addComponent(btnAddEvent))
                        .addGap(0, 34, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoHingeSelected)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCustomizeEdge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddEvent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eventPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEvent;
    private javax.swing.JButton btnCustomizeEdge;
    private javax.swing.JPanel eventPanel;
    private javax.swing.JLabel lblNoHingeSelected;
    // End of variables declaration//GEN-END:variables
}
