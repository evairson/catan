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
    public void setAnimMouse(MouseAdapter animMouse) {
        this.animMouse = animMouse;
    }

    public ShopPanel() {
        setBackground(Color.RED);
        setLayout(null);
        createShopButton();
    }

    private void createShopButton() {
        String basePath = "src/main/resources/";
        city = new ButtonImage(basePath + "building/city.png", basePath + "building/cityHover.png",
                30, 0, 2, null, animMouse);
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colonyHover.png",
                30, 110, 2, null, animMouse);
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/roadHover.png",
                30, 200, 2, null, animMouse);
        plus = new ButtonImage(basePath + "plus.png", basePath + "plusHover.png",
                40, 290, 8, null, animMouse);
        city.setBackground(Color.CYAN);
        colony.setBackground(Color.CYAN);
        road.setBackground(Color.CYAN);
        plus.setBackground(Color.CYAN);
        city.setOpaque(true);
        colony.setOpaque(true);
        road.setOpaque(true);
        plus.setOpaque(true);
        add(city);
        add(colony);
        add(road);
        add(plus);
    }

}
