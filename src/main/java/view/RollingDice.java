package view;

import view.utilities.ImgService;

import javax.swing.*;

import model.Game;
import model.Player;
import view.utilities.Resolution;

import java.awt.*;

public class RollingDice extends JPanel {
    private Game game;
    private Player player;
    private JButton rollButton;
    private JLabel diceOneImg;
    private JLabel diceTwoImg;

    public int getDiceOne() {
        return player.getDice1();
    }

    public int getDiceTwo() {
        return player.getDice2();
    }

    public RollingDice(Game game) {
        this.game = game;
        this.player = game.getCurrentPlayer();
        int[] coords = Resolution.calculateResolution(0, 0);
        setLayout(null);
        setOpaque(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/view/dice/d1b.png"));
        Image originalImage = icon.getImage();

        double divider = 0.75 * Resolution.divider(); // Obtenir le facteur de division

        int scaledWidth = (int) (originalImage.getWidth(null) / divider);
        int scaledHeight = (int) (originalImage.getHeight(null) / divider);

        Image resizedImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        diceOneImg = new JLabel(resizedIcon);
        int xCoord = coords[0];
        int yCoord = coords[1];
        diceOneImg.setBounds(xCoord, yCoord, (int) (85 / Resolution.divider()),
                (int) (85 / Resolution.divider()));
        this.add(diceOneImg);
        icon = new ImageIcon(getClass().getResource("/view/dice/d1r.png"));
        originalImage = icon.getImage();

        divider = 0.75 * Resolution.divider(); // Obtenir le facteur de division

        scaledWidth = (int) (originalImage.getWidth(null) / divider);
        scaledHeight = (int) (originalImage.getHeight(null) / divider);

        resizedImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        resizedIcon = new ImageIcon(resizedImage);
        diceTwoImg = new JLabel(resizedIcon);

        coords = Resolution.calculateResolution(60, 0);
        xCoord = coords[0];
        yCoord = coords[1];
        diceTwoImg.setBounds(xCoord, yCoord, (int) (85 / Resolution.divider()),
                (int) (85 / Resolution.divider()));
        this.add(diceTwoImg);
        rollButton = new JButton("Roll!");

        coords = Resolution.calculateResolution(17, 50);
        xCoord = coords[0];
        yCoord = coords[1];
        rollButton.setBounds(xCoord, yCoord, (int) (133 / Resolution.divider()),
                (int) (33 / Resolution.divider()));
        rollButton.addActionListener(actionEvent -> roll());
        this.add(rollButton);
    }

    public void roll() {
        rollButton.setEnabled(false);
        //Start for 3 seconds
        long startTime = System.currentTimeMillis();
        Thread rollThread = new Thread(() -> {
            long endTime = System.currentTimeMillis();
            int refresh = 60;
            try {
                while ((endTime - startTime) / 1000F < 3) {
                    //roll dice

                    player.throwDices();

                    //update dice images
                    ImgService.updateImage(diceOneImg, "/view/dice/d" + getDiceOne() + "b.png", 0.75);
                    ImgService.updateImage(diceTwoImg, "/view/dice/d" + getDiceTwo() + "r.png", 0.75);

                    repaint();
                    revalidate();

                    //Sleep thread
                    Thread.sleep(refresh);
                    refresh += 10;

                    endTime = System.currentTimeMillis();

                }
                player.setHasTrowDices(true);
                if (player.getDice() == 7) {
                    game.setThiefMode(true);
                }
            } catch (InterruptedException e) {
                System.out.println("Threading Error in class RollingDice " + e);
            }
        });
        rollThread.start();
    }

    public void newPlayer(Player player) {
        player.setHasTrowDices(false);
        this.player = player;
        rollButton.setEnabled(true);
    }
}
