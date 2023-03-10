
public class Player {
    // member
    private String name;
    private Hand hand;
    private Money money;
    private int bet;
    private final boolean isComputer;

    public Player() {
        // for dealer
        this.name = "dealer";
        this.hand = new Hand();
        this.money = new Money();
        this.isComputer = true;
        this.bet = 0;
    }

    public Player(String name, int initMoney) {
        this.name = name;
        this.hand = new Hand();
        this.money = new Money(initMoney);
        this.isComputer = false;
        this.bet = 0;
    }

    public void draw(Deck deck) {
        Card pickedCard = deck.pick();
        hand.addCard(pickedCard);
    }

    public void discard(int order, Deck deck) {
        hand.removeCard(order, deck);
    }

    public void frip(int order, boolean which) {
        hand.fripCard(order, which);
    }

    public void addMoney(int value) {
        money.addition(value);
    }

    public void subtMoney(int value) {
        money.subtraction(value);
    }

    public String viewHand(int order) {
        return hand.getCard(order);
    }

    public int viewOneCardNo(int order) {
        return hand.getCardNo(order);
    }

    public String viewOneCardSuit(int order) {
        return hand.getCardSuit(order);
    }

    public boolean viewOneCardFace(int order) {
        return hand.getCardFace(order);
    }

    public int calcHandTotal() {
        return hand.calcHands();
    }

    public boolean isBurst() {
        final boolean isBurst = (hand.calcHands() > 21) ? true : false;
        return isBurst;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }

    public String getName() {
        return name;
    }

    public int getHandAmount() {
        return hand.getCardAmount();
    }

    public int getMoney() {
        return money.getValue();
    }

    public boolean isComputer() {
        return isComputer;
    }
}
