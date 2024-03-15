package others;

import java.util.ArrayList;

import model.Player;

public class ListPlayers extends ArrayList<Player> {

    private int currentPlayerIndex;
    private Player currentPlayer;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ListPlayers(int start, Player... players) {
        super();
        for (Player player : players) {
            this.add(player);
        }
        currentPlayerIndex = start;
        currentPlayer = this.get(start);
    }


    /** Change currentPlayerIndex and return nextPlayer.
     * @return nextPlayer
     *
     */
    public Player next() {
        if (currentPlayerIndex + 1 >= this.size()) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        currentPlayer = this.get(currentPlayerIndex);
        return currentPlayer;
    }

    public Player peekNext() {
        if (currentPlayerIndex + 1 >= this.size()) {
            return this.get(0);
        }
        return this.get(currentPlayerIndex + 1);
    }

    public Player prev() {
        if (currentPlayerIndex - 1 < 0) {
            currentPlayerIndex = this.size();
        } else {
            currentPlayerIndex--;
        }
        currentPlayer = this.get(currentPlayerIndex);
        return currentPlayer;
    }
}
