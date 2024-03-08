package model.cards;

import java.util.ArrayList;
import java.util.Stack;


public class CardStack {
    public static final int NB_KNIGHTS = 14;
    public static final int NB_MONOPOLY = 2;
    public static final int NB_YEAR_OF_PLENTY = 2;
    public static final int NB_ROAD_BUILDING = 2;
    public static final int NB_PROGRESS = 6;
    public static final int NB_POINT = 5;

    private Stack<DevelopmentCard> cardStack;
    private ArrayList<DevelopmentCard> cardToMix;

    public CardStack() {
        cardStack = new Stack<>();
        cardToMix = new ArrayList<>();
        for (int i = 0; i < NB_KNIGHTS; i++) {
            cardToMix.add(new KnightCard());
        }
        for (int i = 0; i < NB_MONOPOLY; i++) {
            cardToMix.add(new Monopoly());
        }
        for (int i = 0; i < NB_ROAD_BUILDING; i++) {
            cardToMix.add(new RoadBuilding());
        }
        for (int i = 0; i < NB_YEAR_OF_PLENTY; i++) {
            cardToMix.add(new YearOfPlenty());
        }
        for (int i = 0; i < NB_PROGRESS; i++) {
            cardToMix.add(new VictoryPointCard());
        }
        mix();
    }

    public Stack<DevelopmentCard> getCardStack() {
        return cardStack;
    }

    public void setCardStack(Stack<DevelopmentCard> cardStack) {
        this.cardStack = cardStack;
    }



    public void mix() {
        for (int i = 0; i < 25; i += 1) {
            int nbCard = (int) (Math.random() * 25);
            while (cardToMix.get(nbCard) == null) {
                nbCard = (int) (Math.random() * 25);
            }
            cardStack.add(cardToMix.get(nbCard));
        }
        System.out.println(cardStack.size());
    }

}
