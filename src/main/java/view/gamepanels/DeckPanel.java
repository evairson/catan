package view.gamepanels;

import view.utilities.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class DeckPanel extends JPanel {
    private ButtonImage deck;
    private Runnable deckAction;
    private MouseAdapter animMouse;
    public void setAnimMouse(MouseAdapter animMouse) {
        this.animMouse = animMouse;
    }

    private boolean isMouseInside = false;

    public void setMouseInside(boolean mouseInside) {
        isMouseInside = mouseInside;
    }

    public boolean isMouseInside() {
        return isMouseInside;
    }

    public DeckPanel(Runnable deckAction) {
        this.deckAction = deckAction;
        setLayout(null);
        createDeckButton();
    }

    private void createDeckButton() {
        String basePath = "src/main/resources/";
        deck = new ButtonImage(basePath + "card.png", basePath + "cardHover.png",
                20, 30, 3, deckAction, animMouse);
        add(deck);
    }
}
