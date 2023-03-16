import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

public class GamePanel extends JPanel implements Runnable, ActionListener {
    // 画像バッファ
    private BufferedImage BG;
    // ゲームモデル
    private BjEngine engine;
    // シーン
    private String scene = "ttl";
    // その他汎用
    private boolean ttl_isSelectedBtn = false;
    private String ttl_DESC = "";
    private String ni_inputedName = "";
    public GamePanel() {
        super();
        bufferInclude();
        postTtlComponents();
        ttlEngine();
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
            case "nameInput" -> nameDraw(g2);
            case "bet" -> betDraw(g2);
            case "game" -> gameDraw(g2);
            case "result" -> resultDraw(g2);
            default -> {
            }
        }
    }

    /* 処理メソッド */
    // 画像読み込み
    private void bufferInclude() {
        try {
            BG = ImageIO.read(new File ("./img/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // タイトル
    private void ttlEngine(){ /* none*/ }
    // 名前入力
    private void nameEngine(){ engine = new BjEngine(); }
    // 賭け金入力
    private void betEngine(){
        // ディーラーの参加
        engine.joinDealer();
        // プレイヤーの参加
        engine.joinPlayer(ni_inputedName);
    }
    private void gameEngine(){}
    private void resultEngine(){}

    /* コンポーネント描画メソッド */
    private void postTtlComponents() {
        // ボタン
        // 開始ボタン
        JButton startBtn = new JButton("ゲームスタート");
        startBtn.setBounds(250,400,300,80);
        startBtn.setActionCommand("gotoName");
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
        JTextField nameInput = new JTextField("名前を入力してください",0);
        nameInput.setBounds(200,120,400,40);
        nameInput.setFont(new Font("メイリオ",Font.PLAIN,30));
        add(nameInput);
        // ボタン
        // 戻るボタン
        JButton nextBtn = new JButton("進む");
        nextBtn.setBounds(430,520,150,60);
        nextBtn.setActionCommand("gotoTtl");
        nextBtn.addActionListener(this);
        add(nextBtn);
        // 戻るボタン
        JButton prevBtn = new JButton("タイトルへ戻る");
        prevBtn.setBounds(600,520,150,60);
        prevBtn.setActionCommand("gotoTtl");
        prevBtn.addActionListener(this);
        add(prevBtn);
    }
    private void postBetComponents() {}
    private void postGameComponents() {}
    private void postResultComponents() {}

    /* 動的要素描画メソッド */
    // タイトル
    private void ttlDraw(Graphics2D g2) {
        // 背景
        bgDraw(g2);
        // テキスト背景 - タイトル
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(0,70,getWidth(),60);
        // テキスト - タイトル
        g2.setFont(new Font("メイリオ",Font.PLAIN,50));
        centeringText(g2, "BLACKJACK GAME", 400, 100, new Color(255, 255, 255, 255), 4, new Color(0,0,0,60));
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
    private void nameDraw(Graphics2D g2){
        // 背景
        bgDraw(g2);
        // テキスト - 文字入力説明
        g2.setFont(new Font("メイリオ",Font.PLAIN,30));
        g2.drawString("名前", 120,150);
    }
    private void betDraw(Graphics2D g2){}
    private void gameDraw(Graphics2D g2){}
    private void resultDraw(Graphics2D g2){}

    /* 汎用描画系 */
    // 背景
    private void bgDraw(Graphics2D g2) { g2.drawImage(BG, 0, 0, null); }
    // センタリング
    private void centeringText(Graphics2D g2, String text, int x, int y){
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
        x=x-rectText.width/2;
        y=y-rectText.height/2+fm.getMaxAscent();

        g2.drawString(text, x, y);
    }
    private void centeringText(Graphics2D g2, String text, int x, int y, Color color, int shadowPositon, Color shadowColor){
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
        x=x-rectText.width/2;
        y=y-rectText.height/2+fm.getMaxAscent();
        // 影
        g2.setColor(shadowColor);
        g2.drawString(text, x+shadowPositon, y+shadowPositon);
        // 本文
        g2.setColor(color);
        g2.drawString(text, x, y);
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
            case "gotoName" -> {
                nameEngine();
                removeAll();
                scene = "nameInput";
                postNameInputComponents();
            }
            case "gotoBet" -> {
                betEngine();
                removeAll();
                postBetComponents();
            }
            case "gotoGame" -> {
                gameEngine();
                removeAll();
                postGameComponents();
            }
            case "gosubHit" -> {}
            case "gosubStand" -> {}
            case "gotoResult" -> {
                resultEngine();
                removeAll();
                postResultComponents();
            }
            case "gotoExit" -> System.exit(0);
        }
    }
}


