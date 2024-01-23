
import javax.swing.*;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class GamePanel extends JPanel {
    private Game game;

    private MouseInputs mouseInputs;


    public GamePanel(Game game) {
        this.game = game;

        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        addKeyListener(new KeyBoardInputs());

        setPanelSize();
    }

    private void setPanelSize(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
    }

    private void updateGame(){
        game.update();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        revalidate();
        game.render(g);
    }
}

