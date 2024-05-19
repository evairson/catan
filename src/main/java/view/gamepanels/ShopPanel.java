package view.gamepanels;

import view.utilities.ButtonImage;
import model.Game;
import model.Player;
import start.Main;

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
    private Game game;

    public boolean isMouseInside() {
        return isMouseInside;
    }

    public void setMouseInside(boolean mouseInside) {
        isMouseInside = mouseInside;
    }

    public void setAnimMouse(MouseAdapter animMouse) {
        this.animMouse = animMouse;
    }

    public ShopPanel(Runnable cardAction, Game game) {
        // setBackground(Color.RED);
        this.cardAction = cardAction;
        setLayout(null);
        createShopButton();
        this.game = game;
    }

    private void createShopButton() {
        String basePath = "src/main/resources/";
        Runnable cityRunnable = () -> game.buildCityButtonAction();
        city = new ButtonImage(basePath + "building/city.png", basePath + "building/cityHover.png",
                30, 20, 2, cityRunnable, animMouse);
        Runnable colonyRunnable = () -> game.buildColonyButtonAction();
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colonyHover.png",
                30, 130, 2, colonyRunnable, animMouse);
        Runnable roadRunnable = () -> game.buildRoadButtonAction();
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/roadHover.png",
                30, 220, 2, roadRunnable, animMouse);
        card = new ButtonImage(basePath + "card.png", basePath + "shopCardHover.png",
                40, 310, 3, cardAction, null);

        add(city);
        add(colony);
        add(road);
        add(card);
    }

    public void setEnabledPanel(boolean b) {
        card.setEnabled(b);
        road.setEnabled(b);
        city.setEnabled(b);
        colony.setEnabled(b);
        repaint();
    }

    public void updateEnablePanel(Game game) {
        Player player = Main.hasServer() ? game.getPlayerClient() : game.getCurrentPlayer();
        card.setEnabled(game.canDraw());
        road.setEnabled(game.canBuildRoad());
        city.setEnabled(game.canBuildCity());
        colony.setEnabled(game.canBuildColony());
        repaint();
        revalidate();
    }

}
