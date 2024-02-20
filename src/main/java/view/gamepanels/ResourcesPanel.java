package view.gamepanels;

import javax.swing.*;

import view.utilities.ButtonImage;
import view.utilities.Resolution;

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
        setLayout(null);
        createResourceButtons();
    }

    private void createResourceButtons() {
        String basePath = "src/main/resources/resources/";
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                20, 30, 2, null, animMouse); // 200 - 550
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                120, 30, 2, null, animMouse); // 300 - 550
        clay = new ButtonImage(basePath + "clay.png", basePath + "clay.png",
                220, 30, 2, null, animMouse); // 400
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                320, 30, 2, null, animMouse); // 500
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                420, 30, 2, null, animMouse); // 600

        woodLabel = createResourceLabel(20, 133, wood.getWidth());
        oreLabel = createResourceLabel(120, 133, ore.getWidth());
        clayLabel = createResourceLabel(220, 133, clay.getWidth());
        wheatLabel = createResourceLabel(323, 133, wheat.getWidth());
        woolLabel = createResourceLabel(420, 133, wool.getWidth());

        add(woodLabel);
        add(oreLabel);
        add(clayLabel);
        add(wheatLabel);
        add(woolLabel);

        add(wood);
        add(ore);
        add(clay);
        add(wheat);
        add(wool);
    }

    private JLabel createResourceLabel(int x, int y, int width) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setBounds(coords[0], coords[1], width, 20);
        return label;
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
