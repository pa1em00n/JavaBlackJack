
public class Card {
    // const
    private final String suitList[] = { "Spade", "Heart", "Diamond", "Club", "joker" };
    // member
    private final int number;
    private final String suit;
    private boolean isFace;

    public Card(int number, int suit) {
        this.number = number;
        this.suit = suitList[suit];
        this.isFace = false;
    }

    public int getNumber() {
        return number;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isFace() {
        return isFace;
    }

    public void setFace(boolean isFace) {
        this.isFace = isFace;
    }
}
