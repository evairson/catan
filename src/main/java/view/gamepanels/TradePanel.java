package view.gamepanels;

import view.utilities.ButtonImage;
import view.utilities.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TradePanel extends JPanel {
    private Image backgroundImage;
    private ButtonImage clay;
    private ButtonImage ore;
    private ButtonImage wheat;
    private ButtonImage wood;
    private ButtonImage wool;
    private ButtonImage accept;
    private ButtonImage decline;

    private JLabel clayLabelTrade;
    private JLabel oreLabelTrade;
    private JLabel wheatLabelTrade;
    private JLabel woodLabelTrade;
    private JLabel woolLabelTrade;


    public TradePanel(int xCoord, int yCoord, int width, int height) {
        setLayout(null);
        setBounds(xCoord, yCoord, width, height);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        createResourceButtons();
        createTradeButtons();
        createLabelsButtons();
    }

    private void createLabelsButtons() {
        clayLabelTrade = createCounterLabel(180, 200, clay.getWidth());
        oreLabelTrade = createCounterLabel(140, 200, ore.getWidth());
        wheatLabelTrade = createCounterLabel(220, 200, wheat.getWidth());
        woodLabelTrade = createCounterLabel(100, 200, wood.getWidth());
        woolLabelTrade = createCounterLabel(260, 200, wool.getWidth());

        add(clayLabelTrade);
        add(oreLabelTrade);
        add(wheatLabelTrade);
        add(woodLabelTrade);
        add(woolLabelTrade);

        woodLabelTrade.setBackground(Color.CYAN);
        configureButton(wood, woodLabelTrade);
        configureButton(ore, oreLabelTrade);
        configureButton(wheat, wheatLabelTrade);
        configureButton(clay, clayLabelTrade);
        configureButton(wool, woolLabelTrade);
    }

    private void createTradeButtons() {
        accept = new ButtonImage("src/main/resources/acceptButton.png", "src/main/resources/acceptButton.png",
                50, 260, 2.5, null, null);
        decline = new ButtonImage("src/main/resources/refuseButton.png",
                "src/main/resources/refuseButton.png",
                290, 260, 2.5, null, null);

        add(accept);
        add(decline);
    }
    private void createResourceButtons() {
        String basePath = "src/main/resources/resources/";
        clay = new ButtonImage(basePath + "clay.png", basePath + "clay.png",
                180, 210, 5, () -> { }, null); // 400
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                140, 210, 5, () -> { }, null); // 300 - 550
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                220, 210, 5, () -> { }, null); // 500
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                100, 210, 5, () -> { }, null); // 200 - 550
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                260, 210, 5, () -> { }, null); // 600

        add(clay);
        add(ore);
        add(wheat);
        add(wood);
        add(wool);
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

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(this.getWidth(),
                this.getHeight(), Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
