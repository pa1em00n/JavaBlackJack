
public class Player {
    // member
    private final int id;
    private String name;
    final private Hand hand;
    private Money money;
    private int bet;
    private final boolean isComputer;
    private final boolean isDealer;

    public Player(int id) {
        // for dealer
        this.id = id;
        this.name = "dealer";
        this.hand = new Hand();
        this.money = new Money();
        this.bet = 0;
        this.isComputer = true;
        this.isDealer = true;
    }

    public Player(int id, String name, int initMoney) {
        this.id = id;
        this.name = name;
        this.hand = new Hand();
        this.money = new Money(initMoney);
        this.bet = 0;
        this.isComputer = false;
        this.isDealer = false;
    }

    /* プレイヤー情報関連　*/
    // setter
    public void changePlayerName(String name) { this.name = name; }

    // getter
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isDealer() { return isDealer; }
    public boolean isComputer() { return isComputer; }

    /* 手札関連 */
    public void draw(Deck deck) { hand.addCard(deck.pick()); }
    public void discard(int order, Deck deck) {
        hand.removeCard(order, deck);
        deck.back(hand.getCard(order));
    }
    public void flip(int order, boolean which) { hand.flipCard(order, which); }
    // setter
        ;
    // getter
    public Card getCard(int order) { return hand.getCard(order); }
    public int getCardNo(int order) { return hand.getCardNo(order); }
    public String getCardSuit(int order) { return hand.getCardSuit(order); }
    public boolean getCardFace(int order) { return hand.getCardFace(order); }
    public int getCardAmount() { return hand.getCardAmount(); }
    public int getHandTotal() { return hand.calc(); }
    public boolean isBurst() { return (hand.calc() > 21); }

    /* 資金関連 */
    public void addMoney(int value) { money.addition(value); }
    public void subtractionMoney(int value) { money.subtraction(value); }
    // setter
    public void setMoney(int value) { money = new Money(value); }
    // getter
    public int getMoneyValue() { return money.getValue(); }

    /* 賭け金関連 */
    // setter
    public void setBet(int bet) { this.bet = bet; }
    // getter
    public int getBet() { return bet; }
}
