package others;

import java.util.ArrayList;
import java.util.HashSet;

import model.Player;
import network.PlayerClient;

public class ListPlayers extends ArrayList<Player> {

    private int currentPlayerIndex;
    private Player currentPlayer;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ListPlayers(int start, HashSet<Player> players) {
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
        System.out.println(currentPlayer instanceof PlayerClient);
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
        for (int i = 0; i < size(); i++) {
            if (get(i) == p) {
                currentPlayerIndex = i;
            }
        }
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
