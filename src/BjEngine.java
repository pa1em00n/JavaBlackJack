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
    private void joinDealer() {
        Player newPlayer = new Player(playerList.size());
        playerList.add(newPlayer);
    }

    /* プレイヤーの追加 */
    public void joinPlayer(String name) {
        Player newPlayer = new Player(playerList.size(), name, 500);
        playerList.add(newPlayer);
        final int id = playerList.size();
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
            pi.flip(1, true);

            if (pi.isDealer()) pi.flip(1, false);
        });
    }

    /* ヒット */
    public boolean hit(int id) {
        // ドロー
        playerList.get(id).draw(deck);
        playerList.get(id).flip(playerList.get(id).getCardAmount() - 1, true);
        // バーストチェック
        return playerList.get(id).isBurst();
    }

    /* スタンド */
    public void stand(int id) {
        ;
    }

    /* ディーラー処理 */
    public void dealerAi() {
        // ディーラーId取得
        int dealerId = 0;
        for (int pi = 0; pi<playerList.size(); ++pi) if (playerList.get(pi).isComputer()) dealerId = pi;
        // 手札が17未満なら引く
        while (true) {
            if (playerList.get(dealerId).getHandTotal() > 17) break;
            if (hit(dealerId)) break;
        }
    }
    /* 比較 */
    public void compare() {
        if (dealer().getHandTotal() > 17);

    }

    /* 情報取得メソッド*/
    private Player dealer() {
        for (Player player : playerList) if (player.isDealer()) return player;
    }
}
