
public class Card {
    // member
    private final int number;
    private final String suit;
    private boolean isFace;
    private int posX;
    private int posY;
    private String animationPhase;

    /* コンストラクタ */
    public Card(int number, int suit) {
        final String[] suitList = { "Spade", "Heart", "Diamond", "Club", "joker" };

        this.number = number;
        this.suit = suitList[suit];
        this.isFace = false;
        this.animationPhase = "in deck";
    }

    // setter
    public void setFace(boolean isFace) {
        this.isFace = isFace;
    }
    // getter
    public int getNumber() { return number; }
    public String getSuit() { return suit; }
    public boolean isFace() { return isFace; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public void setPosX(int val) { posX = val; }
    public void modPosX(int val) { posX +=val; }
    public void setPosY(int val) { posY = val; }
    public void modPosY(int val) { posY +=val; }
    public String getAnimationPhase() { return animationPhase; }
    public void setAnimationPhase(String phase) { animationPhase = phase; }
}
