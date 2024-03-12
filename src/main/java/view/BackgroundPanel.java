package view;

import javax.swing.*;

import others.Constants;

import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/background.png");
    }

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int width = this.getWidth();
        int height = this.getHeight();
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
