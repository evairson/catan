package view;


import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import others.Music;

public class OptionPanel extends JPanel {


    public OptionPanel() {
        JSlider volumeSlider = new JSlider(-80, 6);
        setVisible(true);
        volumeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Music.setVolume(volumeSlider.getValue());
            }
        });
        add(volumeSlider);
    }
}
