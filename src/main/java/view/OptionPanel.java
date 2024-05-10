package view;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.*;

import others.Music;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class OptionPanel extends JPanel {
    private final String basePath = "src/main/resources/";
    private JCheckBox d20CheckBox;
    private JButton tutoJButton;

    public OptionPanel() {
        setLayout(null);
        setVisible(true);
        JSlider volumeSlider = new JSlider(-80, 6);
        volumeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Music.setVolume(volumeSlider.getValue());
            }
        });
        int[] coords = Resolution.calculateResolution(550, 100);
        volumeSlider.setBounds(coords[0], coords[1], 200, 50);
        volumeSlider.setLocation(coords[0], coords[1]);
        add(volumeSlider);

        JLabel volume = new JLabel("Volume");
        volume.setHorizontalAlignment(JLabel.CENTER);
        int[] coordsVolume = Resolution.calculateResolution(550, 150);
        volume.setBounds(coordsVolume[0], coordsVolume[1], 200, 25);
        volume.setLocation(coordsVolume[0], coordsVolume[1]);
        add(volume);

        JButton quitBtn = new ButtonImage(basePath + "quitOption.png", basePath + "quitOption.png",
                558, 450, 1, this::quitoption, null);
        add(quitBtn);

        tutoJButton = new ButtonImage(basePath + "tuto.png", basePath + "tuto.png",
                550, 200, 1, this::launchTutorial, null);
        add(tutoJButton);

        d20CheckBox = new JCheckBox("dé à 20 faces");
        int[] coords2 = Resolution.calculateResolution(550, 300);
        d20CheckBox.setBounds(coords2[0], coords2[1], 200, 100);
        d20CheckBox.setLocation(coords2[0], coords2[1]);
        add(d20CheckBox);
    }

    public void quitoption() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "mainMenu");
        return;
    }

    public boolean d20Selected() {
        return d20CheckBox.isSelected();
    }

    public void launchTutorial() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "tutorial");
        return;
    }
}
