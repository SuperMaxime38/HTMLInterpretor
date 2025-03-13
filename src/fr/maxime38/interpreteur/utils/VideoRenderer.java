package fr.maxime38.interpreteur.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;

public class VideoRenderer {
    private final JFrame frame;

    private final CallbackMediaPlayerComponent mediaPlayerComponent;

    public VideoRenderer(String source) {
        frame = new JFrame("My First Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        mediaPlayerComponent = new CallbackMediaPlayerComponent();  // This is the only change
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.mediaPlayer().media().play(source);
    }
    
    public JFrame getPlayer(String source) {
    	new VideoRenderer(source);
    	return frame;
    }
}