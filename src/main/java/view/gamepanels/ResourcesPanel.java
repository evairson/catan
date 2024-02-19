package view.gamepanels;

import javax.swing.*;

import view.utilities.ButtonImage;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class ResourcesPanel extends JPanel {

    private JLabel woodLabel;
    private JLabel oreLabel;
    private JLabel clayLabel;
    private JLabel wheatLabel;
    private JLabel woolLabel;
    private ButtonImage wood;
    private ButtonImage ore;
    private ButtonImage clay;
    private ButtonImage wheat;
    private ButtonImage wool;
    private MouseAdapter animMouse;
    private boolean isMouseInside = false;
    public void setAnimMouse(MouseAdapter animMouse) {
        this.animMouse = animMouse;
    }
    public void setMouseInside(boolean b) {
        this.isMouseInside = b;
    }

    public boolean isMouseInside() {
        return isMouseInside;
    }

    public ResourcesPanel() {
        setBackground(Color.red);
        setLayout(null);
        createResourceButtons();
    }

    private void createResourceButtons() {
        String basePath = "src/main/resources/resources/";
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                20, 10, 2, null, animMouse); // 200 - 550
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                120, 10, 2, null, animMouse); // 300 - 550
        clay = new ButtonImage(basePath + "clay.png", basePath + "clay.png",
                220, 10, 2, null, animMouse); // 400
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                320, 10, 2, null, animMouse); // 500
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                420, 10, 2, null, animMouse); // 600

        woodLabel = new JLabel("0", SwingConstants.CENTER);
        woodLabel.setBounds(30, 180, wood.getWidth(), 20);
        oreLabel = new JLabel("0", SwingConstants.CENTER);
        oreLabel.setBounds(180, 180, ore.getWidth(), 20);
        clayLabel = new JLabel("0", SwingConstants.CENTER);
        clayLabel.setBounds(330, 180, clay.getWidth(), 20);
        wheatLabel = new JLabel("0", SwingConstants.CENTER);
        wheatLabel.setBounds(485, 180, wheat.getWidth(), 20);
        woolLabel = new JLabel("0", SwingConstants.CENTER);
        woolLabel.setBounds(630, 180, wool.getWidth(), 20);
        add(woodLabel);
        add(oreLabel);
        add(clayLabel);
        add(wheatLabel);
        add(woolLabel);
        wood.setBackground(Color.CYAN);
        ore.setBackground(Color.CYAN);
        clay.setBackground(Color.CYAN);
        wheat.setBackground(Color.CYAN);
        wool.setBackground(Color.CYAN);
        wood.setOpaque(true);
        ore.setOpaque(true);
        clay.setOpaque(true);
        wheat.setOpaque(true);
        wool.setOpaque(true);
        add(wood);
        add(ore);
        add(clay);
        add(wheat);
        add(wool);
    }

    public void updateResourceLabels(int woodAmount, int oreAmount, int clayAmount,
                                     int wheatAmount, int woolAmount) {
        woodLabel.setText(String.valueOf(woodAmount));
        oreLabel.setText(String.valueOf(oreAmount));
        clayLabel.setText(String.valueOf(clayAmount));
        wheatLabel.setText(String.valueOf(wheatAmount));
        woolLabel.setText(String.valueOf(woolAmount));
    }
}
