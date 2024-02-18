package view.gamepanels;

import view.utilities.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ShopPanel extends JPanel {
    private ButtonImage colony;
    private ButtonImage city;
    private ButtonImage road;
    private ButtonImage plus;
    private MouseAdapter animMouse;

    public ShopPanel(MouseAdapter animMouse) {
        this.animMouse = animMouse;
        setBackground(Color.RED);
        setLayout(null);
        createShopButton();
    }

    private void createShopButton() {
        String basePath = "src/main/resources/";
        city = new ButtonImage(basePath + "building/city.png", basePath + "building/cityHover.png",
                0, 0, 2, null, animMouse);
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colonyHover.png",
                0, 110, 2, null, animMouse);
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/roadHover.png",
                0, 200, 2, null, animMouse);
        plus = new ButtonImage(basePath + "plus.png", basePath + "plus.png",
                10, 290, 8, null, animMouse);

        add(city);
        add(colony);
        add(road);
        add(plus);
    }

}
