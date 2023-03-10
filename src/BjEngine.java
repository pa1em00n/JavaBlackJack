import java.util.ArrayList;

public class BjEngine {
    private Deck deck;
    private ArrayList<Player> playerList = new ArrayList<Player>();
    public BjEngine() {
        deck = new Deck(false);
        deck.shuffle();
    }

    /* ディーラーの追加 */
    private void joinDealer() {
        Player newPlayer = new Player();
        playerList.add(newPlayer);
        final int id = playerList.size();
    }

    /* プレイヤーの追加 */
    public void joinPlayer(String name) {
        Player newPlayer = new Player(name, 500);
        playerList.add(newPlayer);
        final int id = playerList.size();
    }

    /* 1ゲームの開始処理 */
    public void gameInit() {
        playerList.forEach(pi -> {
            // 手札を全て捨てて発動
            while(pi.getHandAmount() > 0) {
                pi.frip(0, false);
                pi.discard(0, deck);
            }
        });
        // デッキをシャッフル
        deck.shuffle();
        // ドロー
        playerList.forEach(pi -> {
            pi.draw(deck);
            pi.frip(0, true);
            pi.draw(deck);
            pi.frip(1, true);

            if (pi.isComputer()) pi.frip(1, false);
        });
    }

    /* ヒット */
    public boolean hit(int id) {
        // ドロー
        playerList.get(id).draw(deck);
        playerList.get(id).frip(playerList.get(id).getHandAmount() - 1, true);
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
            if (playerList.get(dealerId).calcHandTotal() > 17) break;
            if (hit(dealerId)) break;
        }
    }
    /* 比較 */
    public void compare() {
        int dealerId = 0;
        for (int pi = 0; pi<playerList.size(); ++pi) if (playerList.get(pi).isComputer()) dealerId = pi;
        
    }
}
