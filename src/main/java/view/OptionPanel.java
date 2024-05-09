package view;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.*;

import others.Music;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class OptionPanel extends JPanel {
    private final String basePath = "src/main/resources/";

    public OptionPanel() {
        setLayout(null);
        JSlider volumeSlider = new JSlider(-80, 6);
        setVisible(true);
        volumeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Music.setVolume(volumeSlider.getValue());
            }
        });
        int[] coords = Resolution.calculateResolution(550, 100);
        volumeSlider.setBounds(coords[0], coords[1], 200, 100);
        volumeSlider.setLocation(coords[0], coords[1]);
        add(volumeSlider);

        JButton quitBtn = new ButtonImage(basePath + "quitOption.png", basePath + "quitOption.png",
                558, 450, 1, this::quitoption, null);
        add(quitBtn);
    }

    public void quitoption() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "mainMenu");
        return;
    }
}
