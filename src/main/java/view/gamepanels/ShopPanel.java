package view.gamepanels;

import view.utilities.ButtonImage;

import javax.swing.*;
import java.awt.event.MouseAdapter;

public class ShopPanel extends JPanel {
    private ButtonImage colony;
    private ButtonImage city;
    private ButtonImage road;
    private ButtonImage card;
    private MouseAdapter animMouse;
    private Runnable cardAction;
    private boolean isMouseInside = false;

    public boolean isMouseInside() {
        return isMouseInside;
    }
    public void setMouseInside(boolean mouseInside) {
        isMouseInside = mouseInside;
    }

    public void setAnimMouse(MouseAdapter animMouse) {
        this.animMouse = animMouse;
    }

    public ShopPanel(Runnable cardAction) {
//        setBackground(Color.RED);
        this.cardAction = cardAction;
        setLayout(null);
        createShopButton();
    }

    private void createShopButton() {
        String basePath = "src/main/resources/";
        city = new ButtonImage(basePath + "building/city.png", basePath + "building/cityHover.png",
                30, 20, 2, null, animMouse);
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colonyHover.png",
                30, 130, 2, null, animMouse);
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/roadHover.png",
                30, 220, 2, null, animMouse);
        card = new ButtonImage(basePath + "card.png", basePath + "shopCardHover.png",
                40, 310, 3, cardAction, null);

        add(city);
        add(colony);
        add(road);
        add(card);
    }

}
