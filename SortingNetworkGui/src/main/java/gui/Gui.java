package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;


import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import javax.imageio.ImageIO;

/**
 *
 * @author Vlad
 */
public class Gui extends javax.swing.JFrame {

    /**
     * Creates new form Gui
     */
    public Gui() {
        initComponents();
        initMenu();
        initVideo();
       
    }
    
    public void initVideo(){
        
        File video_source = new File("src//main//resources//video.mp4");
        Media m = new Media(video_source.toURI().toString());
        player = new MediaPlayer(m);
        MediaView viewer = new MediaView(player);

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        // center video position
        javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        viewer.setX((screen.getWidth() - VideoPlayer.getWidth()) / 2);
        viewer.setY((screen.getHeight() - VideoPlayer.getHeight()) / 2);

        // resize video based on screen size
        DoubleProperty width = viewer.fitWidthProperty();
        DoubleProperty height = viewer.fitHeightProperty();
        width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
        viewer.setPreserveRatio(true);

        // add video to stackpane
        root.getChildren().add(viewer);

        VFXPanel.setScene(scene);
        //player.play();
        VideoPlayer.setLayout(new BorderLayout());
        VideoPlayer.add(VFXPanel, BorderLayout.CENTER);
    }
    public void initMenu(){
        Menu.setVisible(true);
        Menu.setEnabled(true);
        
        
        Tutorial1.setVisible(false);
        Tutorial1.setEnabled(false);
        
   
        Tutorial2.setVisible(false);
        Tutorial2.setEnabled(false);
        
        Try_Algorithm.setVisible(false);
        Try_Algorithm.setEnabled(false);
        
        
        Show.setVisible(false);
        Show.setEnabled(false);
        
        Hibrid.setVisible(false);
        Hibrid.setVisible(false);
        
        Cmo.setVisible(false);
        Cmo.setVisible(false);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        Menu = new javax.swing.JPanel();
        tutorial = new javax.swing.JLabel();
        found = new javax.swing.JLabel();
        try_algorithm = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Tutorial1 = new javax.swing.JPanel();
        NextTutorial = new javax.swing.JLabel();
        BackToMenu = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Tutorial2 = new javax.swing.JPanel();
        BackToTutorial = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        VideoPlayer = new javax.swing.JPanel();
        Try_Algorithm = new javax.swing.JPanel();
        ComputeAlgorithm = new javax.swing.JLabel();
        BackToMenuFromAlgorithm = new javax.swing.JLabel();
        to_hibrid = new javax.swing.JLabel();
        to_cmo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Show = new javax.swing.JPanel();
        ComputeShow = new javax.swing.JLabel();
        BackToMenuFromShow = new javax.swing.JLabel();
        ShowPanel = new javax.swing.JPanel();
        NShow = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        Hibrid = new javax.swing.JPanel();
        BackToSelectFromHibrid = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Cmo = new javax.swing.JPanel();
        BackToSelectFromCmo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sorting Networks");
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(1040, 815));
        setPreferredSize(new java.awt.Dimension(1040, 815));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(255, 255, 255));
        Menu.setMinimumSize(new java.awt.Dimension(1024, 768));
        Menu.setPreferredSize(new java.awt.Dimension(1024, 768));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tutorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tutorialMouseClicked(evt);
            }
        });
        Menu.add(tutorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 490, 60));

        found.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foundMouseClicked(evt);
            }
        });
        Menu.add(found, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 280, 500, 70));

        try_algorithm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try_algorithmMouseClicked(evt);
            }
        });
        Menu.add(try_algorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, 500, 70));

        credits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                creditsMouseClicked(evt);
            }
        });
        Menu.add(credits, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 510, 510, 60));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Home.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1024, 768));
        jLabel1.setMinimumSize(new java.awt.Dimension(1024, 768));
        Menu.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        getContentPane().add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        Tutorial1.setBackground(new java.awt.Color(255, 255, 255));
        Tutorial1.setMinimumSize(new java.awt.Dimension(1024, 768));
        Tutorial1.setPreferredSize(new java.awt.Dimension(1024, 768));
        Tutorial1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NextTutorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NextTutorialMouseClicked(evt);
            }
        });
        Tutorial1.add(NextTutorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 630, 150, 50));

        BackToMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToMenuMouseClicked(evt);
            }
        });
        Tutorial1.add(BackToMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 700, 130, 50));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tutorial.png"))); // NOI18N
        Tutorial1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        getContentPane().add(Tutorial1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        Tutorial2.setBackground(new java.awt.Color(255, 255, 255));
        Tutorial2.setMinimumSize(new java.awt.Dimension(1024, 768));
        Tutorial2.setPreferredSize(new java.awt.Dimension(1024, 768));
        Tutorial2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackToTutorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToTutorialMouseClicked(evt);
            }
        });
        Tutorial2.add(BackToTutorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 720, 140, 40));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tutorial-2.png"))); // NOI18N
        Tutorial2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1030, 770));

        javax.swing.GroupLayout VideoPlayerLayout = new javax.swing.GroupLayout(VideoPlayer);
        VideoPlayer.setLayout(VideoPlayerLayout);
        VideoPlayerLayout.setHorizontalGroup(
            VideoPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 930, Short.MAX_VALUE)
        );
        VideoPlayerLayout.setVerticalGroup(
            VideoPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        Tutorial2.add(VideoPlayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 930, 460));

        getContentPane().add(Tutorial2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        Try_Algorithm.setBackground(new java.awt.Color(255, 255, 255));
        Try_Algorithm.setMinimumSize(new java.awt.Dimension(1024, 768));
        Try_Algorithm.setPreferredSize(new java.awt.Dimension(1024, 768));
        Try_Algorithm.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ComputeAlgorithm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ComputeAlgorithmMouseClicked(evt);
            }
        });
        Try_Algorithm.add(ComputeAlgorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(681, 126, 90, 40));

        BackToMenuFromAlgorithm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToMenuFromAlgorithmMouseClicked(evt);
            }
        });
        Try_Algorithm.add(BackToMenuFromAlgorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 680, 150, 50));

        to_hibrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                to_hibridMouseClicked(evt);
            }
        });
        Try_Algorithm.add(to_hibrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 540, 90));
        to_hibrid.getAccessibleContext().setAccessibleName("hibrid");

        to_cmo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                to_cmoMouseClicked(evt);
            }
        });
        Try_Algorithm.add(to_cmo, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, 560, 90));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Select the algorithm.png"))); // NOI18N
        jLabel4.setText("aaaaaaaasddddddddddddddddddddddddddddddddddd");
        Try_Algorithm.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 770));

        getContentPane().add(Try_Algorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        Show.setBackground(new java.awt.Color(255, 255, 255));
        Show.setMinimumSize(new java.awt.Dimension(1024, 768));
        Show.setPreferredSize(new java.awt.Dimension(1024, 768));
        Show.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ComputeShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ComputeShowMouseClicked(evt);
            }
        });
        Show.add(ComputeShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 110, 140, 60));

        BackToMenuFromShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToMenuFromShowMouseClicked(evt);
            }
        });
        Show.add(BackToMenuFromShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 690, 130, 50));

        ShowPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout ShowPanelLayout = new javax.swing.GroupLayout(ShowPanel);
        ShowPanel.setLayout(ShowPanelLayout);
        ShowPanelLayout.setHorizontalGroup(
            ShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );
        ShowPanelLayout.setVerticalGroup(
            ShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        Show.add(ShowPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 720, 440));

        NShow.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        NShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", "  10", "  11", "  12", "  13", "  14", "  15", "  16" }));
        NShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NShowActionPerformed(evt);
            }
        });
        Show.add(NShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 80, 40));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Show.png"))); // NOI18N
        Show.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 770));

        getContentPane().add(Show, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        Hibrid.setBackground(new java.awt.Color(255, 255, 255));
        Hibrid.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackToSelectFromHibrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToSelectFromHibridMouseClicked(evt);
            }
        });
        Hibrid.add(BackToSelectFromHibrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 700, 140, 50));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Hibrid.png"))); // NOI18N
        jLabel7.setText("aaaaaaaasddddddddddddddddddddddddddddddddddd");
        Hibrid.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        getContentPane().add(Hibrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        Cmo.setBackground(new java.awt.Color(255, 255, 255));
        Cmo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackToSelectFromCmo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToSelectFromCmoMouseClicked(evt);
            }
        });
        Cmo.add(BackToSelectFromCmo, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 690, 130, 50));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CMO.png"))); // NOI18N
        jLabel8.setText("aaaaaaaasddddddddddddddddddddddddddddddddddd");
        Cmo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        getContentPane().add(Cmo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1024, 768));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //region Menu
    private void tutorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tutorialMouseClicked
        Tutorial1.setVisible(true);
        Tutorial1.setEnabled(true);
        
        Menu.setVisible(false);
        Menu.setEnabled(false);
        
        
    }//GEN-LAST:event_tutorialMouseClicked

    private void foundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foundMouseClicked
      
        Menu.setVisible(false);
        Menu.setEnabled(false);
        
      
        Show.setVisible(true);
        Show.setEnabled(true);
       
       
    }//GEN-LAST:event_foundMouseClicked

    private void try_algorithmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_try_algorithmMouseClicked

        Menu.setVisible(false);
        Menu.setEnabled(false);
        
        Try_Algorithm.setVisible(true);
        Try_Algorithm.setEnabled(true);
        
    }//GEN-LAST:event_try_algorithmMouseClicked

    private void creditsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_creditsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_creditsMouseClicked
    //endregion
    
    
    
    private void NextTutorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NextTutorialMouseClicked
        
        Tutorial2.setVisible(true);
        Tutorial2.setEnabled(true);
        
        
        Tutorial1.setVisible(false);
        Tutorial1.setEnabled(false);
        player.play();
        
        
       
    }//GEN-LAST:event_NextTutorialMouseClicked

    private void BackToMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToMenuMouseClicked
        
        Menu.setVisible(true);
        Menu.setEnabled(true);
        
        
        Tutorial1.setVisible(false);
        Tutorial1.setEnabled(false);
        
   
        Tutorial2.setVisible(false);
        Tutorial2.setEnabled(false);
        
        
        Try_Algorithm.setVisible(false);
        Try_Algorithm.setEnabled(false);
        
        Show.setVisible(false);
        Show.setEnabled(false);
        
        Hibrid.setVisible(false);
        Hibrid.setVisible(false);
        
        Cmo.setVisible(false);
        Cmo.setVisible(false);
    }//GEN-LAST:event_BackToMenuMouseClicked
    
    private void ComputeAlgorithmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComputeAlgorithmMouseClicked
        /*GeneticAlgorithm algorithm = new GeneticAlgorithm(100,1);
        int n = Integer.parseInt(((String) Objects.requireNonNull(NAlgorithm.getSelectedItem())).trim());
        int d = Integer.parseInt(((String) Objects.requireNonNull(DAlgorithm.getSelectedItem())).trim());
        System.out.println(d);
        Network network = algorithm.getSortedNetwork(n,d);
        createImageFromNetwork(network);
        */
    }//GEN-LAST:event_ComputeAlgorithmMouseClicked
/*
    private void createImageFromNetwork(Network network) {
        System.out.println(network.visualize());
    }
    */
    private void BackToMenuFromAlgorithmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToMenuFromAlgorithmMouseClicked
        Menu.setVisible(true);
        Menu.setEnabled(true);
        
        Try_Algorithm.setVisible(false);
        Try_Algorithm.setEnabled(false);
       
        
    }//GEN-LAST:event_BackToMenuFromAlgorithmMouseClicked

    private void ComputeShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComputeShowMouseClicked
        int n = Integer.parseInt(((String) Objects.requireNonNull(NShow.getSelectedItem())).trim());
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(getClass().getResource("/networks/"+n+".jpg").toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assert myPicture != null;
        myPicture = resize(myPicture,ShowPanel.getWidth(),ShowPanel.getHeight());
        ShowPanel.getGraphics().drawImage(myPicture,0,0,ShowPanel);
    }//GEN-LAST:event_ComputeShowMouseClicked
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    private void BackToMenuFromShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToMenuFromShowMouseClicked
        Menu.setVisible(true);
        Menu.setEnabled(true);
        
        
        Show.setVisible(false);
        Show.setEnabled(false);
 
    }//GEN-LAST:event_BackToMenuFromShowMouseClicked

    private void NShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NShowActionPerformed
    }//GEN-LAST:event_NShowActionPerformed

    private void BackToTutorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToTutorialMouseClicked
        Tutorial1.setVisible(true);
        Tutorial1.setEnabled(true);

        Tutorial2.setVisible(false);
        Tutorial2.setEnabled(false);


        player.stop();

    }//GEN-LAST:event_BackToTutorialMouseClicked

    private void to_hibridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_to_hibridMouseClicked
        Try_Algorithm.setVisible(false);
        Try_Algorithm.setEnabled(false);
        
        Hibrid.setVisible(true);
        Hibrid.setVisible(true);
        
    }//GEN-LAST:event_to_hibridMouseClicked

    private void to_cmoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_to_cmoMouseClicked
        Try_Algorithm.setVisible(false);
        Try_Algorithm.setEnabled(false);
        
        Cmo.setVisible(true);
        Cmo.setVisible(true);
    }//GEN-LAST:event_to_cmoMouseClicked

    private void BackToSelectFromHibridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToSelectFromHibridMouseClicked
        Try_Algorithm.setVisible(true);
        Try_Algorithm.setEnabled(true);
        
        Hibrid.setVisible(false);
        Hibrid.setVisible(false);
    }//GEN-LAST:event_BackToSelectFromHibridMouseClicked

    private void BackToSelectFromCmoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToSelectFromCmoMouseClicked
         Try_Algorithm.setVisible(true);
        Try_Algorithm.setEnabled(true);
        
        Cmo.setVisible(false);
        Cmo.setVisible(false);
    }//GEN-LAST:event_BackToSelectFromCmoMouseClicked

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
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }

    private JFXPanel VFXPanel = new JFXPanel();
    private MediaPlayer player; 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BackToMenu;
    private javax.swing.JLabel BackToMenuFromAlgorithm;
    private javax.swing.JLabel BackToMenuFromShow;
    private javax.swing.JLabel BackToSelectFromCmo;
    private javax.swing.JLabel BackToSelectFromHibrid;
    private javax.swing.JLabel BackToTutorial;
    private javax.swing.JPanel Cmo;
    private javax.swing.JLabel ComputeAlgorithm;
    private javax.swing.JLabel ComputeShow;
    private javax.swing.JPanel Hibrid;
    private javax.swing.JPanel Menu;
    private javax.swing.JComboBox<String> NShow;
    private javax.swing.JLabel NextTutorial;
    private javax.swing.JPanel Show;
    private javax.swing.JPanel ShowPanel;
    private javax.swing.JPanel Try_Algorithm;
    private javax.swing.JPanel Tutorial1;
    private javax.swing.JPanel Tutorial2;
    private javax.swing.JPanel VideoPlayer;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel found;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel to_cmo;
    private javax.swing.JLabel to_hibrid;
    private javax.swing.JLabel try_algorithm;
    private javax.swing.JLabel tutorial;
    // End of variables declaration//GEN-END:variables
}
