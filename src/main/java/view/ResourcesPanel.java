package view;

import javax.swing.JPanel;
import view.utilities.ButtonImage;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class ResourcesPanel extends JPanel {
    private ButtonImage wood;
    private ButtonImage ore;
    private ButtonImage clay;
    private ButtonImage wheat;
    private ButtonImage wool;
    private MouseAdapter animMouse;

    public ResourcesPanel(MouseAdapter animMouse) {
        this.animMouse = animMouse;
        setLayout(null); // Utiliser null layout pour un positionnement manuel
//        setBackground(Color.RED);
        createResourceButtons();
    }

    private void createResourceButtons() {
        String basePath = "src/main/resources/resources/";
        wood = new ButtonImage(basePath + "wood.png", basePath + "wood.png",
                0, 0, 2, null, animMouse); // 200 - 550
        ore = new ButtonImage(basePath + "ore.png", basePath + "ore.png",
                100, 0, 2, null, animMouse); // 300 - 550
        clay = new ButtonImage(basePath + "clay.png", basePath + "clay.png",
                200, 0, 2, null, animMouse); // 400
        wheat = new ButtonImage(basePath + "wheat.png", basePath + "wheat.png",
                300, 0, 2, null, animMouse); // 500
        wool = new ButtonImage(basePath + "wool.png", basePath + "wool.png",
                400, 0, 2, null, animMouse); // 600

        add(wood);
        add(ore);
        add(clay);
        add(wheat);
                add(wool);
    }
}
