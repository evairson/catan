import java.util.ArrayList;

public class GameBoard {
    private ArrayList<Tile> tiles;
    private int size = Constants.GameBoard.SIZE;

    public GameBoard() {
        this.makeBoard();
    }

    public void makeBoard() {
        

    }

    public String toString() {
        String board = "";
        for (int i = 0; i < this.tiles.size(); i++) {
            board += this.tiles.get(i).toString() + "\n";
        }
        return board;
    }
}
