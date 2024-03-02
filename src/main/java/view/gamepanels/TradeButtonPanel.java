package view.gamepanels;

import view.utilities.ButtonImage;

import javax.swing.*;

public class TradeButtonPanel extends JPanel {
    private ButtonImage trade;
    private Runnable tradeAction;

    public TradeButtonPanel(Runnable tradeAction) {
        this.tradeAction = tradeAction;
        setLayout(null);
        createTradeButton();
    }

    private void createTradeButton() {
        String basePath = "src/main/resources/";
        trade = new ButtonImage(basePath + "tradeButton.png", basePath + "tradeButtonHover.png",
                0, 0, 5, tradeAction, null);
        add(trade);
    }

}
