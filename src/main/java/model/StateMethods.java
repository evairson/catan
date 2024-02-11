package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {

    void draw(Graphics g); //update graphic
    void update(); //update du jeu en lui meme

    //mouse inputs
    void mouseDragged(MouseEvent e);
    void mouseMoved(MouseEvent e);
    void mouseClicked(MouseEvent e);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseEntered(MouseEvent e);
    void mouseExited(MouseEvent e);

    //keyboard inputs
    void keyTyped(KeyEvent e);
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);

}
