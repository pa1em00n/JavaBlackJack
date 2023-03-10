
import java.util.ArrayList;

public class Hand {
    // member
    private ArrayList<Card> cardList;

    public Hand() {
        cardList = new ArrayList<Card>();
    }

    public void addCard(Card newCard) {
        cardList.add(newCard);
    }

    public void removeCard(int order, Deck deck) {
        final Card removeCard = cardList.get(order);
        deck.back(removeCard);
        cardList.remove(order);
    }

    public void fripCard(int order, boolean which) {
        final Card selectedCard = cardList.get(order);
        selectedCard.setFace(which);
    }

    public int calcHands() {
        int total = 0;
        int aceCount = 0;
        for (int i = 0; i < cardList.size(); ++i) {
            if ((cardList.get(i).getNumber()) != 1) {
                total += bjNormalize(cardList.get(i).getNumber());
            } else {
                total += bjNormalize(cardList.get(i).getNumber());
                ++aceCount;
            }
        }
        total = bjNormalize(total, aceCount);
        return total;
    }

    private int bjNormalize(int value) {
        int fixed = value;
        if (value > 10)
            fixed = 10;
        return fixed;
    }

    private int bjNormalize(int total, int aceCount) {
        int unResolvedAce = aceCount;
        int fixed = total;
        while (12 > fixed && unResolvedAce > 0) {
            fixed += 10;
            --unResolvedAce;
        }
        return fixed;
    }

    public String getCard(int order) {
        final int cardNo = cardList.get(order).getNumber();
        final String cardSuit = cardList.get(order).getSuit();
        return "[" + cardNo + " / " + cardSuit + "]";
    }

    public int getCardNo(int order) {
        final int cardNo = cardList.get(order).getNumber();
        return cardNo;
    }

    public String getCardSuit(int order) {
        final String cardSuit = cardList.get(order).getSuit();
        return cardSuit;
    }

    public boolean getCardFace(int order) {
        final boolean cardFace = cardList.get(order).isFace();
        return cardFace;
    }

    public int getCardAmount() {
        return cardList.size();
    }
}
