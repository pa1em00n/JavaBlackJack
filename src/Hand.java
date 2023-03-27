
import java.util.ArrayList;

public class Hand {
    // member
    private final ArrayList<Card> cardList;

    /* コンストラクタ */
    public Hand() { cardList = new ArrayList<>(); }

    /* カード情報関連 */
    public void addCard(Card newCard) { cardList.add(newCard); }
    public void removeCard(int order) { cardList.remove(order); }
    public void flipCard(int order, boolean which) { cardList.get(order).setFace(which); }
    // setter

    // getter
    public Card getCard(int order) { return cardList.get(order); }
    public int getCardAmount() { return cardList.size(); }

    /* 点数計算関連 */
    public int calc() {
        int total = 0;
        int aceCount = 0;
        for (Card card : cardList) {
            total += normalize(card.getNumber());
            aceCount += (card.getNumber() == 1) ? 1 : 0;
        }
        // ace
        total = aceAddition(total, aceCount);
        return total;
    }
    private int normalize(int value) { return Math.min(value, 10); }
    private int aceAddition(int total, int aceCount) {
        int unResolvedAce = aceCount;
        int fixed = total;
        while (12 > fixed && unResolvedAce > 0) {
            fixed += 10;
            --unResolvedAce;
        }
        return fixed;
    }
}
