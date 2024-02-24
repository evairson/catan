package view.gamepanels;

import javax.swing.*;

import model.Player;
import model.resources.Resources;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class ResourcesPanel extends JPanel {

    private JLabel clayLabel;
    private JLabel oreLabel;
    private JLabel wheatLabel;
    private JLabel woodLabel;
    private JLabel woolLabel;
    private ButtonImage clay;
    private ButtonImage ore;
    private ButtonImage wheat;
    private ButtonImage wood;
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
        clay = new ButtonImage(basePath + "clay.png", basePath + "clay.png",
                220, 30, 2, null, animMouse); // 400
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                120, 30, 2, null, animMouse); // 300 - 550
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                320, 30, 2, null, animMouse); // 500
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                20, 30, 2, null, animMouse); // 200 - 550
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                420, 30, 2, null, animMouse); // 600

        clayLabel = createResourceLabel(220, 133, clay.getWidth());
        oreLabel = createResourceLabel(120, 133, ore.getWidth());
        wheatLabel = createResourceLabel(323, 133, wheat.getWidth());
        woodLabel = createResourceLabel(20, 133, wood.getWidth());
        woolLabel = createResourceLabel(420, 133, wool.getWidth());

        add(clayLabel);
        add(oreLabel);
        add(wheatLabel);
        add(woodLabel);
        add(woolLabel);

        add(clay);
        add(ore);
        add(wheat);
        add(wood);
        add(wool);
    }

    private JLabel createResourceLabel(int x, int y, int width) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setBounds(coords[0], coords[1], width, 20);
        return label;
    }

    public void updateResourceLabels(int clayAmount, int oreAmount, int wheatAmount,
                                     int woodAmount, int woolAmount) {
        clayLabel.setText(String.valueOf(clayAmount));
        oreLabel.setText(String.valueOf(oreAmount));
        wheatLabel.setText(String.valueOf(wheatAmount));
        woodLabel.setText(String.valueOf(woodAmount));
        woolLabel.setText(String.valueOf(woolAmount));
    }
    public void updateResourceLabels(Player p) {
        ArrayList<Resources> playerResources = p.getResources();
        updateResourceLabels(playerResources.get(0).getAmount(),
                playerResources.get(1).getAmount(),
                playerResources.get(2).getAmount(),
                playerResources.get(3).getAmount(),
                playerResources.get(4).getAmount());
    }
}
