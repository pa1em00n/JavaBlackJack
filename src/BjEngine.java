import java.util.ArrayList;

public class BjEngine {
    final private Deck deck;
    final private ArrayList<Player> playerList = new ArrayList<>();

    /* デッキの作成 */
    public BjEngine() {
        deck = new Deck(false);
        deck.shuffle();
    }

    /* ディーラーの追加 */
    public void joinDealer() {
        if (getDealer() != null) return;
        Player newPlayer = new Player(playerList.size());
        playerList.add(newPlayer);
    }

    /* プレイヤーの追加 */
    public void joinPlayer(String name) {
        if (getPlayer() != null) return;
        Player newPlayer = new Player(playerList.size(), name, 500);
        playerList.add(newPlayer);
    }

    /* 賭け金設定 */
    public void bet(int value) {
        getPlayer().setBet(value);
        getPlayer().subtractionMoney(value);
    }

    /* 1ゲームの開始処理 */
    public void gameInit() {
        playerList.forEach(pi -> {
            // 手札を全てデッキに戻す
            while(pi.getCardAmount() > 0) {
                pi.flip(0, false);
                pi.discard(0, deck);
            }
        });
        // デッキをシャッフル
        deck.shuffle();
        // ドロー
        playerList.forEach(pi -> {
            pi.draw(deck);
            pi.flip(0, true);
            pi.draw(deck);
            if (!pi.isDealer()) pi.flip(1, true);
        });
    }

    /* ヒット */
    public void hit(Player player) {
        // ドロー
        player.draw(deck);
        player.flip(player.getCardAmount() - 1, true);
    }

    /* スタンド */
    // public void stand(int id) { ; }

    /* ディーラー処理 */
    public void dealerAi() {
        // 手札が17未満なら引く
        while (true) {
            if (getDealer().getHandTotal() >= 17) break;
            if (getDealer().isBurst()) break;
            hit(getDealer());
        }
    }

    /* 比較 */
    public String compare() {
        // ディーラー手札公開
        getDealer().flip(1, true);
        // プレイヤーバースト
        if (getPlayer().isBurst())  return "LOSE";
        // ディーラーバースト
        if (getDealer().isBurst()) return "WIN";
        /* 数値の比較 */
        // プレイヤー勝利
        if (getPlayer().getHandTotal() > getDealer().getHandTotal()) return "WIN";
        // ディーラー勝利
        if (getPlayer().getHandTotal() < getDealer().getHandTotal()) return "LOSE";
        // ひきわけ
        return "DRAW";
    }

    /* 払い戻し */
    public void pay(String result) {
        switch (result) {
            case "WIN" -> getPlayer().addMoney(getPlayer().getBet() * 2);
            case "DRAW" -> getPlayer().addMoney(getPlayer().getBet());
            default -> {}
        }
    }

    /* 情報取得メソッド*/
    public Player getDealer() {
        Player obj = null;
        for (Player dealer : playerList) if (dealer.isDealer()) obj = dealer;
        return obj;
    }
    public Player getPlayer() {
        Player obj = null;
        for (Player player : playerList) if (!player.isDealer()) obj = player;
        return obj;
    }
    // getter
    public Deck getDeck() { return deck; }
    public int getCardAmount(Player player) { return player.getCardAmount(); }
    public int getHandTotal(Player player) {return player.getHandTotal(); }
    public Card getCard(Player player, int order) { return player.getCard(order); }
    public int getPlayerHandAmount() { return getPlayer().getCardAmount(); }
    public int getDealerHandAmount() { return getDealer().getCardAmount(); }
    public int getPlayerHandCalc() { return getPlayer().getHandTotal(); }
    public int getDealerHandCalc() { return getDealer().getHandTotal(); }
    public Card getPlayerCard(int order) { return getPlayer().getCard(order); }
    public Card getDealerCard(int order) { return getDealer().getCard(order); }
}
