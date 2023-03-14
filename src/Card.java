
public class Card {
    // member
    private final int number;
    private final String suit;
    private boolean isFace;

    /* コンストラクタ */
    public Card(int number, int suit) {
        final String[] suitList = { "Spade", "Heart", "Diamond", "Club", "joker" };

        this.number = number;
        this.suit = suitList[suit];
        this.isFace = false;
    }

    // setter
    public void setFace(boolean isFace) {
        this.isFace = isFace;
    }
    // getter
    public int getNumber() { return number; }
    public String getSuit() { return suit; }
    public boolean isFace() { return isFace; }
}
