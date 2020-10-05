/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiroots.main;

import com.jme3.system.AppSettings;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.digiroots.foldale.gui.mapping.Default2DGUIMapper;
import com.digiroots.foldale.gui.mapping.Mapper2D;
import com.digiroots.main.project.ProjectDesc;
import com.digiroots.training.graph.OrigamiEdgeAttributes;
import com.digiroots.training.graph.OrigamiHingeEditor;
import com.digiroots.training.graph.OrigamiHingeEventListener;
import com.digiroots.training.graph.OrigamiShape;
import com.digiroots.training.graph.OrigamiShapeEditor;
import com.digiroots.training.graph.OrigamiSurfaceEditor;
import com.digiroots.training.graph.Point2D;
import com.digiroots.training.graph.model3d.OrigamiShape3D;
import com.digiroots.training.graph.model3d.OrigamiSimulator;

/**
 *
 * @author khaddam
 */
public class ProjectFrame extends javax.swing.JFrame {

    /**
     * Creates new form ProjectFrame
     */
    Main mainProjectFrame;
    ProjectDesc project;
    OrigamiShape shape;
    OrigamiShape3D shape3D;
    //JFrame shapeFrame;
    OrigamiSimulator simulater = new OrigamiSimulator(null);
    
    public ProjectFrame(Main mainProjectFrame, ProjectDesc project) {
        initComponents();
        this.mainProjectFrame = mainProjectFrame;
        this.project = project;
        //shapeFrame = openOrigamiShapeEditor();
        shape = new OrigamiShape(project);
        //shape = editor.createOrigamiShape();
        shape.addOrigamiHingeEventListener(new OrigamiHingeEventListener() {//Default listener outsputs events only.
            @Override
            public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
                System.out.println("Event "+event+" is triggered");
            }

            @Override
            public void onMouseClickedEvent(Point2D point, String surfaceName) {
                shape.setClicked3DPoint(point);
            }
        });
        JPanel p = new OrigamiShapeEditor.OrigamiPanel(shape);
        this.origamiEditorPanel.setLayout(new BorderLayout());
        this.origamiEditorPanel.add(p, BorderLayout.CENTER);
        p.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    if (me.isControlDown()){
                        shape.selectClickedSurface(me.getPoint().x, me.getPoint().y);
                        surface1Editor.refresh();
                        surface2Editor.refresh();
                        origamiHingeEditor.refresh();
                    }
                }
        });
        
        surface1Editor.init(shape, "Selected Surface", 1);
        surface2Editor.init(shape, "Previously selected surface",  2);
        
        origamiHingeEditor.init(shape);
        //surface2Editor.setEnabled(false);
        txt2dXRatio.setText(""+project.ratioX2D);
        txt2dYRatio.setText(""+project.ratioY2D);
        txt3dXRatio.setText(""+project.ratioX3D);
        txt3dYRatio.setText(""+project.ratioY3D);
        lblProjectName.setText(project.projectPath);

        RatioDocumentListener listener = new RatioDocumentListener();
        txt2dXRatio.getDocument().addDocumentListener(listener);            
        txt2dYRatio.getDocument().addDocumentListener(listener);            
        txt3dXRatio.getDocument().addDocumentListener(listener);            
        txt3dYRatio.getDocument().addDocumentListener(listener);            
        setupSimulator();
        simulater.start();    
        
        
    }
    private void setupSimulator(){
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Flecto Simulator");
        settings.setSettingsDialogImage("Interface/splashscreen.png");
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        settings.setResolution(600,800/*modes[0].getWidth(), modes[0].getHeight()*/);
        settings.setFrequency(modes[0].getRefreshRate());
        //settings.setDepthBits(modes[0].getBitDepth());
        settings.setBitsPerPixel(modes[0].getBitDepth());

        //settings.setFullscreen(device.isFullScreenSupported());

        simulater.setSettings(settings);
        simulater.setShowSettings(false);
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
        jLabel1 = new javax.swing.JLabel();
        lblProjectName = new javax.swing.JLabel();
        btnSaveProject = new javax.swing.JButton();
        btnCloseProject = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt3dXRatio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt3dYRatio = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btnSimulate3D = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt2dXRatio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt2dYRatio = new javax.swing.JTextField();
        btnSimulate3Dand2D = new javax.swing.JButton();
        surface1Editor = new com.digiroots.main.SurfaceEditor();
        surface2Editor = new com.digiroots.main.SurfaceEditor();
        origamiHingeEditor = new com.digiroots.main.OrigamiHingeEditor();
        jLabel8 = new javax.swing.JLabel();
        txtMapperClassPath = new javax.swing.JTextField();
        origamiEditorPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(400, 1000));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Project");

        lblProjectName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProjectName.setText("c:\\OrigamiProjects\\projecct1");

        btnSaveProject.setText("Save");
        btnSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveProjectActionPerformed(evt);
            }
        });

        btnCloseProject.setText("Close");
        btnCloseProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseProjectActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("3DRendering settings");

        jLabel3.setText("X Ratio");

        txt3dXRatio.setText("jTextField1");

        jLabel4.setText("Y Ratio");
        jLabel4.setToolTipText("");

        txt3dYRatio.setText("jTextField2");

        jButton2.setText("Save as initial state");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnSimulate3D.setText("Simulate 3D");
        btnSimulate3D.setToolTipText("");
        btnSimulate3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimulate3DActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("2D settings");

        jLabel6.setText("X Ratio");

        txt2dXRatio.setText("jTextField1");

        jLabel7.setText("Y Ratio");
        jLabel7.setToolTipText("");

        txt2dYRatio.setText("jTextField2");

        btnSimulate3Dand2D.setText("Simulate 3D with 2D");
        btnSimulate3Dand2D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimulate3Dand2DActionPerformed(evt);
            }
        });

        jLabel8.setText("Give the class path to the GUI then click simulate below");
        jLabel8.setToolTipText("");

        txtMapperClassPath.setText("com.digiroots.foldale.gui.mapping.Default2DGUIMapper");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtMapperClassPath)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnSaveProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnCloseProject, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(lblProjectName))
                        .addComponent(jLabel2)
                        .addComponent(jLabel5)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt2dXRatio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt2dYRatio, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnSimulate3Dand2D)
                        .addComponent(origamiHingeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt3dXRatio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt3dYRatio))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSimulate3D)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))))
                    .addComponent(surface2Editor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(surface1Editor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblProjectName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveProject)
                    .addComponent(btnCloseProject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt3dXRatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3dYRatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimulate3D)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt2dXRatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txt2dYRatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jLabel8)
                .addGap(4, 4, 4)
                .addComponent(txtMapperClassPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimulate3Dand2D)
                .addGap(13, 13, 13)
                .addComponent(surface1Editor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(surface2Editor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(origamiHingeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        origamiEditorPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout origamiEditorPanelLayout = new javax.swing.GroupLayout(origamiEditorPanel);
        origamiEditorPanel.setLayout(origamiEditorPanelLayout);
        origamiEditorPanelLayout.setHorizontalGroup(
            origamiEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        origamiEditorPanelLayout.setVerticalGroup(
            origamiEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(origamiEditorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(origamiEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseProjectActionPerformed
        mainProjectFrame.setVisible(true);
        //shapeFrame.dispose();
        dispose();
    }//GEN-LAST:event_btnCloseProjectActionPerformed

    private void btnSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveProjectActionPerformed
        bindProject();
        shape.saveShape();
        project.saveProject();
    }//GEN-LAST:event_btnSaveProjectActionPerformed

    private void btnSimulate3Dand2DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimulate3Dand2DActionPerformed
        Mapper2D mapper=null;
        try {
           String className = txtMapperClassPath.getText();
           Class c = Class.forName(className);
           mapper = (Mapper2D)c.newInstance();
        } catch (Exception e) {
           e.printStackTrace();
           return;
        }        
        mapper.runGUI(shape);
        shape3D = new OrigamiShape3D(shape, project);
        if (project.isHasSavedInitialState()){
            shape3D.loadInitialState();
        }
        simulater.refresh(shape3D, mapper);
                    
    }//GEN-LAST:event_btnSimulate3Dand2DActionPerformed

    private void btnSimulate3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimulate3DActionPerformed
        Default2DGUIMapper mapper = new Default2DGUIMapper();
        mapper.runGUI(shape);
        shape3D = new OrigamiShape3D(shape, project);
        simulater.refresh(shape3D, mapper);
    }//GEN-LAST:event_btnSimulate3DActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (shape3D!=null){
            shape3D.saveInitialState();
            shape.saveShape();
            project.saveProject();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    
    public JFrame openOrigamiShapeEditor() {
        JFrame f = new JFrame("Draw Origami shape");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000, 600);
        f.setLocation(300, 300);
        f.setResizable(true);
        Container contentPane = f.getContentPane();
        contentPane.setLayout(new BorderLayout());

        OrigamiShapeEditor editor = new OrigamiShapeEditor(project);
        shape = new OrigamiShape(project);
        //shape = editor.createOrigamiShape();
        shape.addOrigamiHingeEventListener(new OrigamiHingeEventListener() {//Default listener outsputs events only.
            @Override
            public void onHingeEvent(OrigamiEdgeAttributes.OrigamEdgeEvent event) {
                System.out.println("Event "+event+" is triggered");
            }

            @Override
            public void onMouseClickedEvent(Point2D point, String surfaceName) {
                shape.setClicked3DPoint(point);
            }
        });
        JPanel p = new OrigamiShapeEditor.OrigamiPanel(shape/*, editor*/);
        //JPanel pCmd = new OrigamiShapeEditor.OrigamiCommandsPanel(shape);
        editor.hingeEditor = new OrigamiHingeEditor(shape);
        editor.surface1Editor = new OrigamiSurfaceEditor(shape, "Last clicked Surface", 1);
        editor.surface2Editor = new OrigamiSurfaceEditor(shape, "Before last clicked surface",  2);
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.add(editor.surface1Editor);
        westPanel.add(editor.surface2Editor);
        westPanel.add(editor.hingeEditor);

        //contentPane.add(pCmd, BorderLayout.SOUTH);
        contentPane.add(p, BorderLayout.CENTER);
        contentPane.add(westPanel, BorderLayout.EAST);
        //contentPane.add(ratioPanel, BorderLayout.NORTH);
        f.setVisible(true);
        return f;
    }    
    private void bindProject(){
        project.ratioX2D = Float.parseFloat(txt2dXRatio.getText());
        project.ratioY2D = Float.parseFloat(txt2dYRatio.getText());
        project.ratioX3D = Float.parseFloat(txt3dXRatio.getText());
        project.ratioY3D = Float.parseFloat(txt3dYRatio.getText());
        shape.bindProject();
    }
    class RatioDocumentListener implements DocumentListener {
        
        public RatioDocumentListener(){
           
        }
        public void changedUpdate(DocumentEvent e) {warn();}
        public void removeUpdate(DocumentEvent e) {warn();}
        public void insertUpdate(DocumentEvent e) {warn();}
        public void warn() {
            try{
                bindProject();
                /*if (txtW.getText().isEmpty())
                    return;
                shape.ratioX3D = Float.parseFloat(txtW.getText());
                */
            } catch(NumberFormatException e){;}
        }
                
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseProject;
    private javax.swing.JButton btnSaveProject;
    private javax.swing.JButton btnSimulate3D;
    private javax.swing.JButton btnSimulate3Dand2D;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblProjectName;
    private javax.swing.JPanel origamiEditorPanel;
    private com.digiroots.main.OrigamiHingeEditor origamiHingeEditor;
    private com.digiroots.main.SurfaceEditor surface1Editor;
    private com.digiroots.main.SurfaceEditor surface2Editor;
    private javax.swing.JTextField txt2dXRatio;
    private javax.swing.JTextField txt2dYRatio;
    private javax.swing.JTextField txt3dXRatio;
    private javax.swing.JTextField txt3dYRatio;
    private javax.swing.JTextField txtMapperClassPath;
    // End of variables declaration//GEN-END:variables
}
