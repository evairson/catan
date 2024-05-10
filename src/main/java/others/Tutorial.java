package others;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;

import view.utilities.ButtonImage;

public class Tutorial extends JPanel {
    public Tutorial() {
        setLayout(null);
        String basePath = "src/main/resources/";
        setVisible(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        JButton quitBtn = new ButtonImage(basePath + "quitOption.png", basePath + "quitOption.png",
                558, 450, 1, this::backToMenu, null);
        add(quitBtn);
    }

    public void backToMenu() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "optionPanel");
    }
}
