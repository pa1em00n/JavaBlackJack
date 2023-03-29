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
        Player newPlayer = new Player(playerList.size());
        playerList.add(newPlayer);
    }

    /* プレイヤーの追加 */
    public void joinPlayer(String name) {
        Player newPlayer = new Player(playerList.size(), name, 500);
        playerList.add(newPlayer);
    }

    /* 賭け金設定 */
    public void bet(int value) {
        player().setBet(value);
        player().subtractionMoney(value);
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
            if (dealer().getHandTotal() >= 17) break;
            if (dealer().isBurst()) break;
            hit(dealer());
        }
    }

    /* 比較 */
    public String compare() {
        // ディーラー公開
        dealer().flip(1, true);
        // プレイヤーバースト
        if (player().isBurst())  return "LOSE";
        // ディーラーバースト
        if (dealer().isBurst()) {
            pay(0);
            return "WIN";
        }
        // 数値の比較

        // プレイヤー勝利
        if (player().getHandTotal() > dealer().getHandTotal()) {
            pay(0);
            return "WIN";
        }
        // ディーラー勝利
        if (player().getHandTotal() < dealer().getHandTotal()) return "LOSE";
        // ひきわけ
        pay(1);
        return "DRAW";
    }

    /* 払い戻し */
    public void pay(int property) {
        final int WIN = 0;
        final int DRAW = 1;
        switch (property) {
            case WIN -> player().addMoney(player().getBet() * 2);
            case DRAW -> player().addMoney(player().getBet());
            default -> {}
        }
    }

    /* 情報取得メソッド*/
    public Player dealer() {
        Player obj = null;
        for (Player dealer : playerList) if (dealer.isDealer()) obj = dealer;
        return obj;
    }
    public Player player() {
        Player obj = null;
        for (Player player : playerList) if (!player.isDealer()) obj = player;
        return obj;
    }
    // getter
    public Deck getDeck() { return deck; }
    public int getPlayerHandAmount() { return player().getCardAmount(); }
    public int getDealerHandAmount() { return dealer().getCardAmount(); }
    public int getPlayerHandCalc() { return player().getHandTotal(); }
    public int getDealerHandCalc() { return dealer().getHandTotal(); }
    public Card getPlayerCard(int order) { return player().getCard(order); }
    public Card getDealerCard(int order) { return dealer().getCard(order); }
}
