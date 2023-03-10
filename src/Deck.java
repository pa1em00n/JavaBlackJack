
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    // member
    private final int total;
    private ArrayList<Card> cardList = new ArrayList<Card>();

    public Deck(boolean isUseJoker) {
        total = (isUseJoker) ? 53 : 52;
        for (int i = 0; i < 52; ++i) {
            final int cardNumber = (i % 13) + 1;
            final int cardSuit = (int) Math.floor(i / 13);
            Card card = new Card(cardNumber, cardSuit);
            cardList.add(card);
        }
        if (total == 53) {
            Card card = new Card(0, 4);
            cardList.add(card);
        }
        Collections.shuffle(cardList);
    }

    public Card pick() {
        final Card pickedCard = cardList.get(0);
        cardList.remove(0);
        return pickedCard;
    }

    public void back(Card discarded) {
        cardList.add(discarded);
    }

    public void shuffle() {
        Collections.shuffle(cardList);
    }
}
