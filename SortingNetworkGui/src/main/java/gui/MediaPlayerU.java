package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
 
/**
*
* @author BUDDHIMA
*/
 
public class MediaPlayerU extends javax.swing.JPanel {

        public MediaPlayerU() {
            
                URL mediaUrl=null;
                
                JFileChooser fileChooser = new JFileChooser();
 
                fileChooser.showOpenDialog(null);

                try {

                //mediaUrl = new File("C:\\Users\\Vlad\\Documents\\NetBeansProjects\\SortingNetworkGui\\src\\main\\resources\\video.mp4").toURI().toURL();
                mediaUrl = fileChooser.getSelectedFile().toURI().toURL();

                } catch (MalformedURLException ex) {

                System.out.println(ex);
                }
 
                //initComponents();

                setLayout(new BorderLayout());

                try {

                Player mediaPlayer=Manager.createRealizedPlayer(new MediaLocator(mediaUrl));

                Component video=mediaPlayer.getVisualComponent();

                Component control=mediaPlayer.getControlPanelComponent();

                if (video!=null) {

                add(video, BorderLayout.CENTER);          // place the video component in the panel

                }

                add(control, BorderLayout.SOUTH);            // place the control in  panel

                mediaPlayer.start();

                } catch (Exception e) {

                }
        }
        
        
        public static void main(String[] args) {


            JFrame mediaTest = new JFrame( "Movie Player" );

            mediaTest.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            MediaPlayerU mediaPanel = new MediaPlayerU( );

            mediaTest.add( mediaPanel );

            mediaTest.setSize( 800, 700 ); // set the size of the player

            mediaTest.setLocationRelativeTo(null);

            mediaTest.setVisible( true );
        }
}