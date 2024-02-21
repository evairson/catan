package model.cards;

import java.util.ArrayList;
import java.util.Stack;

public class CardStack {
    public static final int NB_KNIGHTS = 14;
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
        for (int i = 0; i < NB_POINT; i++) {
            cardToMix.add(new ProgessCard());
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
    }

}
