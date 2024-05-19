package view;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class EventDispatcher implements KeyEventDispatcher {
    private ActionPlayerPanel actionPlayer;

    public EventDispatcher(ActionPlayerPanel actionPlayer) {
        this.actionPlayer = actionPlayer;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            keyReleased(e);
        } else if (e.getID() == KeyEvent.KEY_TYPED) {
            keyTyped(e);
        }
        return false;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            actionPlayer.getChat().getSender().actionPerformed(null);
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }
}
