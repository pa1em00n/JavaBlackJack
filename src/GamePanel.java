import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;

public class GamePanel extends JPanel implements Runnable, ActionListener {
    int n=0;
    private BufferedImage BG;
    private final BjEngine engine = new BjEngine();
    private String scene = "ttl";
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
                ++n;
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
    private void ttlEngine(){}
    private void nameEngine(){}
    private void betEngine(){}
    private void gameEngine(){}
    private void resultEngine(){}

    /* コンポーネント描画メソッド */
    private void postTtlComponents() {
        // ボタン
        JButton btn = new JButton("ゲームスタート");
        btn.setBounds(250,300,300,80);
        add(btn);
    }
    private void postNameInputComponents() {}
    private void postBetComponents() {}
    private void postGameComponents() {}
    private void postResultComponents() {}

    /* 動的要素描画メソッド */
    // タイトル
    private void ttlDraw(Graphics2D g2) {
        // 背景
        bgDraw(g2);
        // テキスト背景
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(0,70,getWidth(),60);
        // テキスト
        g2.setFont(new Font("メイリオ",Font.PLAIN,50));
        centeringText(g2, "BLACKJACK GAME", 400, 100, new Color(255, 255, 255, 255), 4, new Color(0,0,0,60));

    }
    private void nameDraw(Graphics2D g2){}
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
                postTtlComponents();
            }
            case "gotoName" -> {
                nameEngine();
                removeAll();
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


