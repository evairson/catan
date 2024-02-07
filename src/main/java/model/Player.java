package model;

public class Player {
    static final int NUMBER_DICE = 7;

    public enum Color {
        RED,
        WHITE,
        BLUE,
        ORANGE
    }

     private Color color;
    private Boolean turn;
    private int dice1;
    private int dice2;
    private String nom;
    // private Coordonnee cord;
    // private ArrayList<Card> cardsRessources;
    // private ArrayList<Card> cardsDev;
    // private ArrayList<Building> buildings;

// Getter / Setter :  ---------------

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public Boolean isTurn() {
        return turn;
    }

    public void setTurn(Boolean b) {
        turn = b;
    }

    public int getDies() {
        return dice1 + dice2;
    }

// ------------------------------------

    public void throwDice1() {
        dice1 = (int) (Math.random() * NUMBER_DICE); // (max-min+1)*min
    }

    public void throwDice2() {
        dice2 = (int) (Math.random() * NUMBER_DICE);
    }

    public void throwDices() {
        throwDice1();
        throwDice2();
    }
}
