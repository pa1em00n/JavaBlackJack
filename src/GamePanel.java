import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

public class GamePanel extends JPanel implements Runnable, ActionListener {
    // 画像バッファ

    // ゲームモデル
    private BjEngine engine;
    // シーン
    private String scene = "ttl";
    // その他汎用
    /* タイトル画面 */
    private boolean ttl_isSelectedBtn = false;
    private String ttl_DESC = "";
    /* 設定 */
    private boolean setting_isSelectedBtn = false;
    private String setting_name = "";
    private String setting_DESC = "";
    private JButton game5Btn, game10Btn, endlessBtn, gotoBetBtn;
    private final DrawComponents comp = new DrawComponents();
    private JTextField nameInput;
    private boolean isInitializingGame;
    private boolean isMyTurn;
    private int betValue;
    private int gameResultJudge = -2;
    private int burstAnimation = 0;
    private int gamecount = 1;
    private int gameMax = 0;
    private JButton hitBtn, standBtn;

    public GamePanel() {
        super();
        setLayout(null);
        try {
            comp.bufferInclude();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e);
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
                System.out.println(e);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        switch (scene) {
            case "ttl" -> ttlDraw(g2);
            case "setting" -> settingDraw(g2);
            case "game" -> gameDraw(g2);
            case "result" -> resultDraw(g2);
            default -> {
            }
        }
    }

    /* 処理メソッド */

    // タイトル
    private void ttlEngine(){ /* none*/ }
    // 名前入力
    private void nameEngine(){ engine = new BjEngine(); }
    // 賭け金入力
    private void betEngine(){ engine.bet(betValue); }
    // ゲームエンジン
    private void gameEngine(){
        // 開始処理
        if (isInitializingGame) {
            // ディーラーの参加
            engine.joinDealer();
            // プレイヤーの参加
            setting_name = nameInput.getText();
            engine.joinPlayer(setting_name);
            isInitializingGame = false;
        }
        // 初手配置
        engine.gameInit();
        // 自分ターン
        isMyTurn = true;
    }
    private void hitEngine() {
        engine.hit(engine.player());
        if (engine.player().getHandTotal() >= 21) {
            standEngine();
            if (engine.player().getHandTotal() > 21) burstAnimation = 1;
        }
    }
    private void standEngine() {
        hitBtn.setEnabled(false);
        standBtn.setEnabled(false);
        isMyTurn = false;
        if (engine.player().getHandTotal() <= 21) engine.dealerAi();
        gameResultJudge = engine.compare();
    }
    private void resultEngine(){}

    /* コンポーネント描画メソッド */
    private void postTtlComponents() {
        // ボタン
        // 開始ボタン
        JButton startBtn = new JButton("ゲームスタート");
        startBtn.setBounds(250,400,300,80);
        startBtn.setActionCommand("gotoSetting");
        startBtn.addActionListener(this);
        startBtn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e) {
               ttl_DESC = "ゲームを開始します。";
               ttl_isSelectedBtn = true;
            }
            public void mouseExited(MouseEvent e) { ttl_isSelectedBtn = false; }
        });
        add(startBtn);
        // 終了ボタン
        JButton exitBtn = new JButton("終了");
        exitBtn.setBounds(250,500,300,80);
        exitBtn.setActionCommand("gotoExit");
        exitBtn.addActionListener(this);
        exitBtn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e) {
                ttl_DESC = "ゲームを終了します。";
                ttl_isSelectedBtn = true;
            }
            public void mouseExited(MouseEvent e) { ttl_isSelectedBtn = false; }
        });
        add(exitBtn);
    }
    private void postNameInputComponents() {
        // テキストボックス
        // 名前入力
        nameInput = new JTextField("名前を入力してください",0) {
            /* 角丸にするメソッド */
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    int w = getWidth() - 1;
                    int h = getHeight() - 1;
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(UIManager.getColor("TextField.background"));
                    g2.fillRoundRect(0, 0, w, h, h, h);
                    g2.setPaint(Color.GRAY);
                    g2.drawRoundRect(0, 0, w, h, h, h);
                    g2.dispose();
                }
                super.paintComponent(g);
            }

            @Override
            public void updateUI() {
                super.updateUI();
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }
        };;
        nameInput.setBounds(200,110,400,40);
        nameInput.setFont(new Font("メイリオ",Font.PLAIN,18));
        add(nameInput);
        // ボタン
        // 5ゲーム
        game5Btn = new JButton("5ゲーム");
        game5Btn.setBounds(105,300,190,80);
        game5Btn.setActionCommand("toggle5Game");
        game5Btn.addActionListener(this);
        add(game5Btn);
        // 10ゲーム
        game10Btn = new JButton("10ゲーム");
        game10Btn.setBounds(305,300,190,80);
        game10Btn.setActionCommand("toggle10Game");
        game10Btn.addActionListener(this);
        add(game10Btn);
        // エンドレス
        endlessBtn = new JButton("エンドレス");
        endlessBtn.setBounds(505,300,190,80);
        endlessBtn.setActionCommand("toggleEndless");
        endlessBtn.addActionListener(this);
        add(endlessBtn);
        // 開始ボタン
        gotoBetBtn = new JButton("開始！");
        gotoBetBtn.setBounds(250,500,300,80);
        gotoBetBtn.setActionCommand("gotoGame");
        gotoBetBtn.addActionListener(this);
        gotoBetBtn.setEnabled(false);
        add(gotoBetBtn);
    }
    private void postBetComponents() {}
    private void postGameComponents() {
        // ヒット
        hitBtn = new JButton("ヒット");
        hitBtn.setBounds(50,520,120,60);
        hitBtn.setActionCommand("gosubHit");
        hitBtn.addActionListener(this);
        add(hitBtn);
        // スタンド
        standBtn = new JButton("スタンド");
        standBtn.setBounds(200,520,120,60);
        standBtn.setActionCommand("gosubStand");
        standBtn.addActionListener(this);
        add(standBtn);
    }
    private void postResultComponents() {}

    /* 動的要素描画メソッド */
    // タイトル
    private void ttlDraw(Graphics2D g2) {
        // 背景
        comp.bgDraw(g2);
        // テキスト背景 - タイトル
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(0,70,getWidth(),60);
        // テキスト - タイトル
        g2.setFont(new Font("メイリオ",Font.PLAIN,50));
        comp.centeringText(g2, "BLACKJACK GAME", 400, 100, new Color(255, 255, 255, 255), 4, new Color(0,0,0,60));
        if (ttl_isSelectedBtn) {
            // テキスト背景 - 選択要素の説明
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRect(150,200,500,150);
            // テキスト - 選択要素の説明
            g2.setFont(new Font("メイリオ",Font.PLAIN,20));
            g2.setColor(new Color(255, 255, 255, 255));
            g2.drawString(ttl_DESC, 204, 250);
        }
    }
    private void settingDraw(Graphics2D g2){
        // 背景
        comp.bgDraw(g2);
        // テキスト - 文字入力説明見出し
        g2.setFont(new Font("メイリオ",Font.PLAIN,18));
        comp.shadowedText(g2, "名前設定", 120,60, new Color(255, 255, 255, 255), 2, new Color(0,0,0,60));
        // 矩形枠 - 文字入力説明
        g2.drawRoundRect(100, 80, 600, 100, 10,10);

        // テキスト - ゲームモード見出し
        comp.shadowedText(g2, "ゲーム数設定", 120,230, new Color(255, 255, 255, 255), 2, new Color(0,0,0,60));
        // 矩形 - ゲームモード
        g2.drawRoundRect(100,250,600,200, 10, 10);

        if (setting_isSelectedBtn) {
            // テキスト - 選択要素の説明
            g2.setFont(new Font("メイリオ",Font.PLAIN,20));
            comp.centeringText(g2,setting_DESC, 400, 420);
        }
    }
    private void gameDraw(Graphics2D g2){
        boolean writtenHands = true;
        // 背景
        comp.bgDraw(g2);
        // スプライト - 山札
        for(int i=0; i<engine.getDeckNumber(); ++i) comp.deckDraw(g2, 360, 220 - i/2);
        // アニメーション - ゲーム開始フェード
        comp.textCenterLineBack(g2, (gameMax == -1) ? "GAME "+gamecount : "GAME "+gamecount+" / "+gameMax);
        if (comp.getLFadePhase() != -1) return;
        // スプライト - 手札
        for(int i=0; i<engine.getPlayerHandAmount(); ++i) {
            if (i != 0) {
                if (engine.getPlayerCard(i - 1).getAnimationPhase() == 2) comp.myHandDraw(g2, i, engine.getPlayerCard(i));
                if (i == engine.getPlayerHandAmount()-1 && engine.getPlayerCard(i).getAnimationPhase() != 2) {
                    // 手札最後まで描画完了か取得
                    writtenHands = false;
                }
            } else comp.myHandDraw(g2, i, engine.getPlayerCard(i));
        }
        for(int i=0; i<engine.getDealerHandAmount(); ++i) {
            if (i != 0) {
                if (engine.getDealerCard(i-1).getAnimationPhase() == 2) comp.opponentHandDraw(g2, i, engine.getDealerCard(i));
                if (i == engine.getDealerHandAmount()-1 && engine.getDealerCard(i).getAnimationPhase() != 2) {
                    // 手札最後まで描画完了か取得
                    writtenHands = false;
                }
            } else comp.opponentHandDraw(g2, i, engine.getDealerCard(i));
        }
        comp.pointTableDraw(g2);
        // テキスト - 点数
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("メイリオ",Font.PLAIN,30));
        comp.centeringText(g2, String.valueOf(engine.getPlayerHandCalc()), 168, 332, (engine.getPlayerHandCalc() > 21) ?  Color.RED : Color.WHITE, 2, new Color(0,0,0,60));
        comp.centeringText(g2, String.valueOf((isMyTurn) ? "？" : engine.getDealerHandCalc()), 638, 192, (engine.getDealerHandCalc() > 21) ?  Color.RED : Color.WHITE, 2, new Color(0,0,0,60));
        // テキスト - バースト
        if (!writtenHands) return;
        if (burstAnimation == 1) {
            comp.fadeInCenterText(g2, "BURST", 400, 300, 255, 0, 0, 2, 255, 255, 255);
            if (comp.getTFadePhase() == -1) {
                burstAnimation = 0;
                comp.resetTFade();
            }
        } else {
            // テキスト - 勝敗
            switch(gameResultJudge) {
                case -1 -> comp.fadeInCenterText(g2, "LOSE", 400, 300, 0, 0, 255, 2, 255, 255, 255);
                case 0 -> comp.fadeInCenterText(g2, "DRAW", 400, 300, 255, 255, 0, 2, 255, 255, 255);
                case 1 -> comp.fadeInCenterText(g2, "WIN", 400, 300, 255, 0, 0, 2, 255, 255, 255);
            }
            if (comp.getTFadePhase() == -1) {
                gameResultJudge = -2;
                comp.resetTFade();
                comp.resetLFade();
                nextGame();
                ++gamecount;
                if (gameMax != -1 && gamecount > gameMax) endGame();
            }
        }
    }
    private void resultDraw(Graphics2D g2){

    }
    private void nextGame() {
        gameEngine();
        removeAll();
        postGameComponents();
        hitBtn.setEnabled(true);
        standBtn.setEnabled(true);
    }
    private void endGame() {

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
                setting_DESC = "5ゲーム：運に左右されやすいゲーム";
                gameMax = 5;
                setting_isSelectedBtn = true;
                game5Btn.setForeground(Color.RED);
                game10Btn.setForeground(Color.BLACK);
                endlessBtn.setForeground(Color.BLACK);
                gotoBetBtn.setEnabled(true);
            }
            case "toggle10Game" -> {
                setting_DESC = "10ゲーム：実力が決めるゲーム";
                gameMax = 10;
                setting_isSelectedBtn = true;
                game5Btn.setForeground(Color.BLACK);
                game10Btn.setForeground(Color.RED);
                endlessBtn.setForeground(Color.BLACK);
                gotoBetBtn.setEnabled(true);
            }
            case "toggleEndless" -> {
                setting_DESC = "エンドレス：引き際の駆け引きが肝心";
                gameMax = -1;
                setting_isSelectedBtn = true;
                game5Btn.setForeground(Color.BLACK);
                game10Btn.setForeground(Color.BLACK);
                endlessBtn.setForeground(Color.RED);
                gotoBetBtn.setEnabled(true);
            }
            case "gosubBet" -> {
                betEngine();
                postBetComponents();
            }
            case "gotoGame" -> {
                isInitializingGame = true;
                gameEngine();
                removeAll();
                scene = "game";
                postGameComponents();
            }
            case "gosubHit" -> {
                hitEngine();
            }
            case "gosubStand" -> {
                standEngine();
            }
            case "gotoResult" -> {
                resultEngine();
                removeAll();
                postResultComponents();
            }
            case "gotoExit" -> System.exit(0);
        }
    }
}


