import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, ActionListener {
    /* メインフォント */
    private final String mainFont = "メイリオ";
    /* ゲームモデル */
    private BjEngine engine;
    private GeneralButton game5Btn, game10Btn, endlessBtn, gotoBetBtn, hitBtn, standBtn;
    private RoundRectTextField nameInput, betInput;
    /* 描画ユーティリティクラス */
    private final DrawComponents comp = new DrawComponents();
    /* シーン */
    private String scene = "ttl";
    /* 保持の必要な変数 */
    // タイトル画面
    private boolean ttl_isSelectedBtn = false;
    private String ttl_DESC = "";
    // 設定
    private boolean isInitializingGame;
    // ゲーム
    private boolean isAnimatedGameStarting, isMyTurn, isBurstAnimated, isGameFinished;
    private int gameCount, gameMax, winCount, loseCount;
    private String gameResultJudge;
    // リザルト
    private final ArrayList<Ranker> rankerList = new ArrayList<>();
    private boolean isRankingInitialized = false;

    public GamePanel() {
        super();
        setLayout(null);
        try {
            comp.bufferInclude();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Exception occurred.");
        }
        postTtlComponents();
        ttlEngine();
        setVisible(true);
    }
    @Override
    public void run() {
        while (true) {
            try {
                /* method */
                repaint();
                /* wait */
                Thread.sleep(16);
            } catch (Exception e) {
                System.out.println("Exception occurred.");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (scene) {
            case "ttl" -> ttlDraw(g2);
            case "setting" -> settingDraw(g2);
            case "bet" -> betDraw(g2);
            case "game" -> gameDraw(g2);
            case "result" -> resultDraw(g2);
            default -> {
            }
        }
    }

    /* 処理メソッド */

    // タイトル
    private void ttlEngine(){
        if (!isRankingInitialized) {
            // ランキングファイルが存在するなら読み込み
            if (Files.exists(Paths.get("./java.txt"))) importLocalSaveData();
            // なければサンプル生成
            else {
                // ランキング生成
                final String[] defaultRankerName = {"MUR", "AIG", "ATU", "KUT", "TA.", ""};
                final int[] defaultRankerWins = {6, 5, 4, 3, 2};
                final int[] defaultRankerLoses = {4, 5, 6, 7, 8};
                final int[] defaultRankerMoneys = {300, 250, 200, 150, 100};
                for (int i = 0; i < 5; ++i) {
                    final Ranker ranker = new Ranker(i + 1, defaultRankerName[i], defaultRankerWins[i], defaultRankerLoses[i], defaultRankerMoneys[i]);
                    rankerList.add(ranker);
                }
            }
            isRankingInitialized = true;
        }
    }
    // 名前入力
    private void nameEngine(){ ttl_isSelectedBtn = false; }
    // 賭け金入力
    private void betEngine(){
        // 開始処理
        if (isInitializingGame) {
            // ゲームエンジンの作成
            engine = new BjEngine();
            // ディーラーの参加
            engine.joinDealer();
            // プレイヤーの参加
            String setting_name = nameInput.getText();
            engine.joinPlayer(setting_name);
            isInitializingGame = false;
            // ゲーム変数初期化
            winCount = 0;
            loseCount = 0;
            gameCount = 1;
            isBurstAnimated = false;
        }
        // 判定をリセット
        gameResultJudge = "before";
        // 賭け金を0にする
        engine.bet(0);
    }
    // ゲームエンジン
    private void gameEngine(){
        // ベット
        engine.bet(Integer.parseInt(betInput.getText()));
        // 初手配置
        engine.gameInit();
        // 自分ターン
        isMyTurn = true;
        // 各種アニメーション一時変数リセット
        comp.initTextAndCenterLine();
        isAnimatedGameStarting = true;
        isGameFinished = false;
    }
    private void hitEngine() {
        // ヒット(1枚ひく)
        engine.hit(engine.getPlayer());
        // 21以上でターンを飛ばす
        if (engine.getPlayer().getHandTotal() >= 21) {
            standEngine();
            if (engine.getPlayer().getHandTotal() > 21) {
                isBurstAnimated = true;
                comp.initTextAndCenterLine();
            }
        }
    }
    private void standEngine() {
        hitBtn.setEnabled(false);
        standBtn.setEnabled(false);
        isMyTurn = false;
        // バーストしていればディーラーのターンは飛ばす
        if (engine.getPlayer().getHandTotal() <= 21) engine.dealerAi();
        // 勝敗判定
        gameResultJudge = engine.compare();
        // 払い戻し
        engine.pay(gameResultJudge);
        switch (gameResultJudge) {
            case "LOSE" -> ++loseCount;
            case "WIN" -> ++winCount;
        }
        comp.initTextAndCenterLine();
    }
    private void resultEngine() {
        // ランクインかどうか
        boolean isRanked = false;
        Ranker rankedPlayer = null;
        for(Ranker rankers: rankerList) {
            // ランクインした場合ランクイン位置へ挿入
            if (!isRanked && engine.getPlayer().getMoneyValue() > rankers.getMoney()) {
                rankedPlayer = new Ranker(rankers.getRank(), engine.getPlayer().getName(), winCount, loseCount, engine.getPlayer().getMoneyValue());
                isRanked = true;
            }
            // その他のプレイヤーはランクダウン
            if (isRanked) { rankers.rankDown(); }
        }
        // 後処理
        if (rankedPlayer != null) {
            rankerList.add(rankedPlayer.getRank() - 1, rankedPlayer);
            rankerList.remove(rankerList.size() - 1);
            // ファイル保存
            saveDataToFile();
        }
    }

    /* コンポーネント描画メソッド */
    private void postTtlComponents() {
        // ボタン - 開始
        SidePositionedButton startBtn = new SidePositionedButton("開始");
        startBtn.setBounds(500,300,300,80);
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ttl_DESC = "ゲームを開始します。";
                ttl_isSelectedBtn = true;
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ttl_isSelectedBtn = false;
            }
        });
        startBtn.setActionCommand("gotoSetting");
        startBtn.addActionListener(this);
        add(startBtn);
        // ボタン - 設定
        SidePositionedButton optBtn = new SidePositionedButton("設定");
        optBtn.setBounds(500,400,300,80);
        add(optBtn);
        // ボタン - 終了
        SidePositionedButton exitBtn = new SidePositionedButton("終了");
        exitBtn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ttl_DESC = "ゲームを終了します。";
                ttl_isSelectedBtn = true;
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ttl_isSelectedBtn = false;
            }
        });
        exitBtn.setBounds(500,500,300,80);
        exitBtn.setActionCommand("gotoExit");
        exitBtn.addActionListener(this);
        add(exitBtn);
    }
    private void postNameInputComponents() {
        // ボタン - 5ゲーム
        game5Btn = new GeneralButton("5");
        game5Btn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        game5Btn.setBounds(140,375,40,40);
        game5Btn.setActionCommand("toggle5Game");
        game5Btn.addActionListener(this);
        add(game5Btn);
        // ボタン - 10ゲーム
        game10Btn = new GeneralButton("10");
        game10Btn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        game10Btn.setBounds(185,375,40,40);
        game10Btn.setActionCommand("toggle10Game");
        game10Btn.addActionListener(this);
        add(game10Btn);
        // ボタン - エンドレス
        endlessBtn = new GeneralButton("エンドレス");
        endlessBtn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        endlessBtn.setBounds(230,375,110,40);
        endlessBtn.setActionCommand("toggleEndless");
        endlessBtn.addActionListener(this);
        add(endlessBtn);
        // テキストボックス - 名前入力
        nameInput = new RoundRectTextField();
        nameInput.setText("Player");
        nameInput.setBounds(200, 280, 400, 40);
        nameInput.setFont(new Font(mainFont, Font.PLAIN, 18));
        add(nameInput);
        // ボタン - 決定
        gotoBetBtn = new GeneralButton("開始！");
        gotoBetBtn.setBounds(350,345,300,80);
        gotoBetBtn.setActionCommand("gotoBet");
        gotoBetBtn.addActionListener(this);
        gotoBetBtn.setEnabled(false);
        add(gotoBetBtn);
    }
    private void postBetComponents() {
        // テキストボックス - 賭け金入力欄
        betInput = new RoundRectTextField();
        betInput.setFormatterFactory(new NumberFormatterFactory());
        betInput.setValue(0);
        betInput.setBounds(120,450,200,40);
        betInput.setFont(new Font(mainFont, Font.PLAIN,18));
        add(betInput);
        // ボタン - 全額
        GeneralButton allBetBtn = new GeneralButton("全額");
        allBetBtn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        allBetBtn.setBounds(80,520,100,40);
        allBetBtn.setActionCommand("gosubAllBet");
        allBetBtn.addActionListener(this);
        add(allBetBtn);
        // ボタン - 確定
        GeneralButton betBtn = new GeneralButton("確定");
        betBtn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        betBtn.setBounds(190,520,100,40);
        betBtn.setActionCommand("gosubGame");
        betBtn.addActionListener(this);
        add(betBtn);
    }
    private void postGameComponents() {
        // ヒット
        hitBtn = new GeneralButton("ヒット");
        hitBtn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        hitBtn.setBounds(50,520,120,60);
        hitBtn.setActionCommand("gosubHit");
        hitBtn.addActionListener(this);
        add(hitBtn);
        // スタンド
        standBtn = new GeneralButton("スタンド");
        standBtn.setInnerTextFont(new Font(mainFont, Font.PLAIN,18));
        standBtn.setBounds(200,520,120,60);
        standBtn.setActionCommand("gosubStand");
        standBtn.addActionListener(this);
        add(standBtn);
    }
    private void postResultComponents() {
        // 開始ボタン
        GeneralButton ttlBtn = new GeneralButton("タイトルへ");
        ttlBtn.setBounds(250,450,300,80);
        ttlBtn.setActionCommand("gotoTtl");
        ttlBtn.addActionListener(this);
        add(ttlBtn);
    }

    /* 動的要素描画メソッド */
    // タイトル
    private void ttlDraw(Graphics2D g2) {
        // 背景
        comp.bgDraw(g2);
        // スプライト - エースとジャックの背景
        comp.ttlBGDraw(g2);
        // テキスト背景 - タイトル
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(0,70,getWidth(),60);
        // テキスト - タイトル
        g2.setFont(new Font(mainFont, Font.PLAIN,80));
        comp.centeringText(g2, "BLACKJACK GAME", 400, 100, new Color(255, 255, 255, 255), 4, new Color(0,0,0,60));
        if (ttl_isSelectedBtn) {
            // テキスト背景 - 選択要素の説明
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRect(50,200,400,150);
            // テキスト - 選択要素の説明
            g2.setFont(new Font(mainFont, Font.PLAIN,20));
            g2.setColor(new Color(255, 255, 255, 255));
            comp.centeringText(g2, ttl_DESC, 250, 250);
        }
    }
    private void settingDraw(Graphics2D g2){
        // 背景
        comp.bgDraw(g2);
        // 矩形枠 - 文字入力
        RoundRectangle2D shape = new RoundRectangle2D.Double(100, 160, 600, 300, 80, 80);
        g2.setPaint(new GradientPaint(0,0,new Color(96,96,96,128),400, 460,new Color(64,64,64,128)));
        g2.fill(shape);
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        // テキスト - 文字入力説明見出し
        g2.setFont(new Font(mainFont, Font.PLAIN,28));
        comp.shadowedText(g2, "名前入力", 120,200, new Color(255, 255, 255, 255), 2, new Color(0,0,0,60));
        g2.setFont(new Font(mainFont, Font.PLAIN,18));
        comp.centeringText(g2, "ランキング入りしたときに保存される名前を入力してください。", 400, 250, new Color(255, 255, 255, 255), 2, new Color(0,0,0,60));
        comp.centeringText(g2, "ゲーム数", 230,360, new Color(255, 255, 255, 255), 2, new Color(0,0,0,60));
    }
    private void betDraw(Graphics2D g2) {
        // 背景
        comp.bgDraw(g2);
        // スプライト - 山札
        comp.deckDraw(g2, 360, 220, engine.getDeck().getAmount());
        // スプライト - 資金欄
        comp.moneyInfoBox(g2, 0, 160,engine.getPlayer().getMoneyValue(), engine.getPlayer().getBet());
        // 矩形枠 - ベット
        RoundRectangle2D shape = new RoundRectangle2D.Double(20, 420, 350, 150, 60, 60);
        g2.setPaint(new GradientPaint(0,0,new Color(96,96,96,128),370, 570,new Color(64,64,64,128)));
        g2.fill(shape);
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        // テキスト - ベット
        g2.setFont(new Font(mainFont, Font.PLAIN,20));
        comp.shadowedText(g2, "賭け金：",40,475, Color.WHITE, 2, new Color(255,255,255,60));
        // アニメーション - ゲーム開始フェード
        comp.textAndCenterLine(g2, (gameMax == -1) ? "GAME "+gameCount : "GAME "+gameCount+" / "+gameMax);
    }
    private void gameDraw(Graphics2D g2){
        int writtenHands = 0;
        // 背景
        comp.bgDraw(g2);
        // スプライト - 山札
        comp.deckDraw(g2, 360, 220, engine.getDeck().getAmount());
        // アニメーション - ゲーム開始フェード
        if (isAnimatedGameStarting) {
            comp.textAndCenterLine(g2, "GAME", "START");
            if (comp.getTextAndCenterLineMode().equals("sleep")) {
                isAnimatedGameStarting = false;
                comp.initTextAndCenterLine();
            }
            return;
        }
        // スプライト - 手札
        for(int i=0; i<engine.getCardAmount(engine.getPlayer()); ++i)
            if (i == 0) comp.myHandDraw(g2, i, engine.getPlayerCard(i));
            else {
                if (engine.getPlayerCard(i - 1).getAnimationPhase().equals("on hand"))
                    comp.myHandDraw(g2, i, engine.getPlayerCard(i));
                if (i == engine.getCardAmount(engine.getPlayer()) - 1 && engine.getPlayerCard(i).getAnimationPhase().equals("on hand")) {
                    // 手札最後まで描画完了か取得
                    ++writtenHands;
                }
            }
        for(int i=0; i<engine.getCardAmount(engine.getDealer()); ++i)
            if (i == 0) comp.opponentHandDraw(g2, i, engine.getDealerCard(i));
            else {
                if (engine.getDealerCard(i - 1).getAnimationPhase().equals("on hand"))
                    comp.opponentHandDraw(g2, i, engine.getDealerCard(i));
                if (i == engine.getCardAmount(engine.getDealer()) - 1 && engine.getDealerCard(i).getAnimationPhase().equals("on hand")) {
                    // 手札最後まで描画完了か取得
                    ++writtenHands;
                }
            }
        // スプライト - 資金
        comp.moneyInfoBox(g2, 0, 160,engine.getPlayer().getMoneyValue(), engine.getPlayer().getBet());
        // スプライト - 点数の背景
        comp.pointTableDraw(g2);
        // テキスト - 点数
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(mainFont ,Font.PLAIN,30));
        comp.centeringText(g2, String.valueOf(engine.getPlayerHandCalc()), 168, 332, (engine.getPlayerHandCalc() > 21) ?  Color.RED : Color.WHITE, 2, new Color(0,0,0,60));
        comp.centeringText(g2, String.valueOf((isMyTurn) ? "？" : engine.getDealerHandCalc()), 638, 192, (engine.getDealerHandCalc() > 21) ?  Color.RED : Color.WHITE, 2, new Color(0,0,0,60));
        // 手札が描画終了していなければ以後実行しない
        if (writtenHands < 2) return;
        // テキスト - バースト
        if (isBurstAnimated) {
            comp.fadeInCenterText(g2, "BURST", 400, 300, Color.RED, 2, Color.WHITE);
            if (comp.getTextAndCenterLineMode().equals("sleep")) {
                isBurstAnimated = false;
                comp.initTextAndCenterLine();
            }
            return;
        }
        // テキスト - 勝敗
        if (!isGameFinished && (gameResultJudge.equals("LOSE") || gameResultJudge.equals("DRAW")|| gameResultJudge.equals("WIN"))) {
            // テキスト
            comp.fadeInCenterText(g2, gameResultJudge, 400, 300, switch (gameResultJudge) {
                case "LOSE" -> Color.BLUE;
                case "DRAW" -> Color.ORANGE;
                case "WIN" -> Color.RED;
                default -> Color.BLACK;
            }, 2, Color.WHITE);
            if (comp.getTextAndCenterLineMode().equals("sleep")) {
                // 試合数カウントを進める
                ++gameCount;
                // 終了条件を満たしている場合終了フラグオン
                if (engine.getPlayer().getMoneyValue() <= 0 || (gameMax != -1 && gameCount > gameMax)) isGameFinished = true;
                // 続行可能な場合
                else nextGame();
                comp.initTextAndCenterLine();
            }
        }
        // テキスト - GAME OVER
        if (isGameFinished) {
            comp.fadeInCenterText(g2, "GAME OVER", 400, 300, Color.RED, 2, Color.BLACK);
            if (comp.getTextAndCenterLineMode().equals("sleep")) {
                endGame();
                comp.initTextAndCenterLine();

            }
        }
    }
    private void resultDraw(Graphics2D g2){
        // 背景
        comp.bgDraw(g2);
        // 矩形枠 - ランキング
        RoundRectangle2D shape = new RoundRectangle2D.Double(100, 60, 600, 350, 60, 60);
        g2.setPaint(new GradientPaint(0,0,new Color(96,96,96,128),700, 480,new Color(64,64,64,128)));
        g2.fill(shape);
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        // ランキング
        g2.setFont(new Font(mainFont ,Font.PLAIN,50));
        comp.centeringText(g2, "ランキング", 400, 100, Color.WHITE, 2, new Color(0,0,0,60));
        g2.setFont(new Font(mainFont ,Font.PLAIN,20));
        comp.shadowedText(g2, "RANK", 150, 170, Color.WHITE, 2, new Color(255, 255, 255, 60));
        comp.shadowedText(g2, "NAME", 150 + 80, 170, Color.WHITE, 2, new Color(255, 255, 255, 60));
        comp.shadowedText(g2, "WIN", 150 + 280, 170, Color.WHITE, 2, new Color(255, 255, 255, 60));
        comp.shadowedText(g2, "LOSE", 150 + 340, 170, Color.WHITE, 2, new Color(255, 255, 255, 60));
        comp.shadowedText(g2, "総獲得金額", 150 + 420, 170, Color.WHITE, 2, new Color(255, 255, 255, 60));
        for(int i=0; i<5; ++i) {
            comp.shadowedText(g2, String.valueOf(rankerList.get(i).getRank()),150 + 20,210 + (i * 40), Color.WHITE, 2, new Color(255,255,255,60));
            comp.shadowedText(g2, (rankerList.get(i).getName().length() > 6) ? rankerList.get(i).getName().substring(0, 7)+"..." : rankerList.get(i).getName(),150 + 80,210 + (i * 40), Color.WHITE, 2, new Color(255,255,255,60));
            comp.shadowedText(g2, String.valueOf(rankerList.get(i).getWin()),150 + 280 + 20,210 + (i * 40), Color.WHITE, 2, new Color(255,255,255,60));
            comp.shadowedText(g2, String.valueOf(rankerList.get(i).getLose()),150 + 340 + 20,210 + (i * 40), Color.WHITE, 2, new Color(255,255,255,60));
            comp.centeringText(g2, String.valueOf(rankerList.get(i).getMoney()),150 + 420 + 50,210 + (i * 40) - 10, Color.WHITE, 2, new Color(255,255,255,60));
            g2.setColor(Color.lightGray);
            g2.drawLine(120, 180 + (i * 40),680,180 + (i * 40));
        }
    }
    // 継続or終了 画面遷移処理
    private void nextGame() {
        betEngine();
        removeAll();
        scene = "bet";
        postBetComponents();
    }
    // 終了画面遷移処理
    private void endGame() {
        resultEngine();
        removeAll();
        scene = "result";
        postResultComponents();
    }

    /* 画面遷移*/
    @Override
    public void actionPerformed(ActionEvent e) {
        final String cmd = e.getActionCommand();

        switch (cmd) {
            case "gotoTtl" -> {
                ttlEngine();
                removeAll();
                scene = "ttl";
                postTtlComponents();
            }
            case "gotoSetting" -> {
                nameEngine();
                removeAll();
                scene = "setting";
                postNameInputComponents();
            }
            case "toggle5Game" -> {
                gameMax = 5;
                game5Btn.changeBgColor("RED");
                game10Btn.changeBgColor("");
                endlessBtn.changeBgColor("");
                gotoBetBtn.setEnabled(true);
            }
            case "toggle10Game" -> {
                gameMax = 10;
                game5Btn.changeBgColor("");
                game10Btn.changeBgColor("RED");
                endlessBtn.changeBgColor("");
                gotoBetBtn.setEnabled(true);
            }
            case "toggleEndless" -> {
                gameMax = -1;
                game5Btn.changeBgColor("");
                game10Btn.changeBgColor("");
                endlessBtn.changeBgColor("RED");
                gotoBetBtn.setEnabled(true);
            }
            case "gotoBet" -> {
                isInitializingGame = true;
                betEngine();
                removeAll();
                scene = "bet";
                postBetComponents();
            }
            case "gosubAllBet" -> betInput.setValue(engine.getPlayer().getMoneyValue());
            case "gosubGame" -> {
                // ベット適正か判断
                if(Integer.parseInt(betInput.getText()) > engine.getPlayer().getMoneyValue()) return;
                if(Integer.parseInt(betInput.getText()) <= 0) return;
                gameEngine();
                removeAll();
                scene = "game";
                postGameComponents();
            }
            case "gosubHit" -> hitEngine();
            case "gosubStand" -> standEngine();
            case "gotoExit" -> System.exit(0);
        }
    }

    private void saveDataToFile(){
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter("./java.txt");
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));

            //ファイルに書き込む
            for(Ranker rankers: rankerList) {
                pw.println(rankers.getRank());
                pw.println(rankers.getName());
                pw.println(rankers.getWin());
                pw.println(rankers.getLose());
                pw.println(rankers.getMoney());
            }

            //ファイルを閉じる
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importLocalSaveData() {
        try {
            FileReader fr = new FileReader("./java.txt");
            BufferedReader br = new BufferedReader(fr);
            for(int ri=0; ri<5; ++ri) {
                String[] rankerDat = new String[5];
                rankerDat[0] = br.readLine();
                if (rankerDat[0] != null) {
                    for (int i=1; i<5; ++i) rankerDat[i] = br.readLine();
                    rankerList.add(new Ranker(
                            Integer.parseInt(rankerDat[0]),
                            rankerDat[1],
                            Integer.parseInt(rankerDat[2]),
                            Integer.parseInt(rankerDat[3]),
                            Integer.parseInt(rankerDat[4])
                    ));
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


