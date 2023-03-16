import java.util.ArrayList;

public class BjEngine {
    final private Deck deck;
    final private ArrayList<Player> playerList = new ArrayList<Player>();

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
    public void hit(int id) {
        // ドロー
        playerList.get(id).draw(deck);
        playerList.get(id).flip(playerList.get(id).getCardAmount() - 1, true);
    }

    /* スタンド */
    public void stand(int id) { ; }

    /* ディーラー処理 */
    public void dealerAi() {
        // 手札が17未満なら引く
        while (true) {
            if (dealer().getHandTotal() >= 17) break;
            if (dealer().isBurst()) break;
            hit(dealer().getId());
        }
    }

    /* 比較 */
    public int compare() {
        // プレイヤーバースト
        if (player().isBurst())  return -1;
        // ディーラーバースト
        if (dealer().isBurst()) {
            pay(0);
            return 1;
        }
        // 数値の比較

        // プレイヤー勝利
        if (player().getHandTotal() > dealer().getHandTotal()) {
            pay(0);
            return 1;
        }
        // ディーラー勝利
        if (player().getHandTotal() < dealer().getHandTotal()) return -1;
        // ひきわけ
        pay(1);
        return 0;
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
    private Player dealer() {
        Player obj = null;
        for (Player dealer : playerList) if (dealer.isDealer()) obj = dealer;
        return obj;
    }
    private Player player() {
        Player obj = null;
        for (Player player : playerList) if (!player.isDealer()) obj = player;
        return obj;
    }
}
