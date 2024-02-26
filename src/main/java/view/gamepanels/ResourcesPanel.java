package view.gamepanels;

import javax.swing.*;

import model.Player;
import view.TileType;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResourcesPanel extends JPanel {

    private JLabel clayLabelTrade;
    private JLabel oreLabelTrade;
    private JLabel wheatLabelTrade;
    private JLabel woodLabelTrade;
    private JLabel woolLabelTrade;

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
                220, 30, 2, () -> { }, animMouse); // 400
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                120, 30, 2, () -> { }, animMouse); // 300 - 550
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                320, 30, 2, () -> { }, animMouse); // 500
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                20, 30, 2, () -> { }, animMouse); // 200 - 550
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                420, 30, 2, () -> { }, animMouse); // 600

        clayLabel = createResourceLabel(220, 133, clay.getWidth());
        oreLabel = createResourceLabel(120, 133, ore.getWidth());
        wheatLabel = createResourceLabel(323, 133, wheat.getWidth());
        woodLabel = createResourceLabel(20, 133, wood.getWidth());
        woolLabel = createResourceLabel(420, 133, wool.getWidth());

        clayLabelTrade = createCounterLabel(220, 20, clay.getWidth());
        oreLabelTrade = createCounterLabel(120, 20, ore.getWidth());
        wheatLabelTrade = createCounterLabel(323, 20, wheat.getWidth());
        woodLabelTrade = createCounterLabel(20, 20, wood.getWidth());
        woolLabelTrade = createCounterLabel(420, 20, wool.getWidth());

        configureButton(wood, woodLabelTrade);
        configureButton(ore, oreLabelTrade);
        configureButton(wheat, wheatLabelTrade);
        configureButton(clay, clayLabelTrade);
        configureButton(wool, woolLabelTrade);

        add(clayLabel);
        add(oreLabel);
        add(wheatLabel);
        add(woodLabel);
        add(woolLabel);

        setLabelTradeVisible(false);
        add(clayLabelTrade);
        add(oreLabelTrade);
        add(wheatLabelTrade);
        add(woodLabelTrade);
        add(woolLabelTrade);

        add(clay);
        add(ore);
        add(wheat);
        add(wood);
        add(wool);
    }

    public void setLabelTradeVisible(boolean b) {
        woodLabelTrade.setVisible(b);
        clayLabelTrade.setVisible(b);
        oreLabelTrade.setVisible(b);
        wheatLabelTrade.setVisible(b);
        woolLabelTrade.setVisible(b);
    }

    private void configureButton(ButtonImage button, JLabel counterLabel) {
        button.addActionListener(e -> {
            int value = Integer.parseInt(counterLabel.getText()) + 1;
            counterLabel.setText(String.valueOf(value));
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                }
            }
        });
    }

    private JLabel createCounterLabel(int x, int y, int width) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setForeground(Color.RED);
        label.setBounds(coords[0], coords[1], width, 20);
        return label;
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
        updateResourceLabels(p.getResources().get(TileType.CLAY),
                p.getResources().get(TileType.ORE),
                p.getResources().get(TileType.WHEAT),
                p.getResources().get(TileType.WOOD),
                p.getResources().get(TileType.WOOL));
    }
}
