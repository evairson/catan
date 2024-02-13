package view;

import view.utilities.ImgService;

import javax.swing.*;

import others.Constants;

import java.util.Random;

public class RollingDice extends JPanel {

    public RollingDice() {
        setLayout(null);
        setOpaque(true);
        setSize(500, 500);
        JLabel diceOneImg = ImgService.loadImage("/view/dice/d1b.png");
        diceOneImg.setBounds(0, 45, 64, 64);
        this.add(diceOneImg);

        JLabel diceTwoImg = ImgService.loadImage("/view/dice/d1r.png");
        diceTwoImg.setBounds(90, 45, 64, 64);
        this.add(diceTwoImg);

        setBounds(Constants.Game.WIDTH - 230, Constants.Game.HEIGHT - 400, 250, 250);


        Random rand = new Random();
        JButton rollButton = new JButton("Roll!");
        rollButton.setBounds(26, 135, 100, 25);
        rollButton.addActionListener(actionEvent -> {
            rollButton.setEnabled(false);

            //Start for 3 seconds
            long startTime = System.currentTimeMillis();
            Thread rollThread = new Thread(() -> {
                long endTime = System.currentTimeMillis();
                int refresh = 60;
                try {
                    while ((endTime - startTime) / 1000F < 3) {
                        //roll dice

                        int diceOne = rand.nextInt(1, 7);
                        int diceTwo = rand.nextInt(1, 7);

                        //update dice images
                        ImgService.updateImage(diceOneImg, "/view/dice/d" + diceOne + "b.png");
                        ImgService.updateImage(diceTwoImg, "/view/dice/d" + diceTwo + "r.png");

                        repaint();
                        revalidate();

                        //Sleep thread
                        Thread.sleep(refresh);
                        refresh += 10;

                        endTime = System.currentTimeMillis();

                    }

                    rollButton.setEnabled(true);
                } catch (InterruptedException e) {
                    System.out.println("Threading Error in class RollingDice " + e);
                }
            });
            rollThread.start();
        });
        this.add(rollButton);
    }
}
