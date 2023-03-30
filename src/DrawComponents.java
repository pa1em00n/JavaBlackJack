import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawComponents {
    private BufferedImage BG;
    private final String[] cardSrcList = {
            "./img/card/card_back.png",
            "./img/card/card_spade_01.png",
            "./img/card/card_spade_02.png",
            "./img/card/card_spade_03.png",
            "./img/card/card_spade_04.png",
            "./img/card/card_spade_05.png",
            "./img/card/card_spade_06.png",
            "./img/card/card_spade_07.png",
            "./img/card/card_spade_08.png",
            "./img/card/card_spade_09.png",
            "./img/card/card_spade_10.png",
            "./img/card/card_spade_11.png",
            "./img/card/card_spade_12.png",
            "./img/card/card_spade_13.png",
            "./img/card/card_heart_01.png",
            "./img/card/card_heart_02.png",
            "./img/card/card_heart_03.png",
            "./img/card/card_heart_04.png",
            "./img/card/card_heart_05.png",
            "./img/card/card_heart_06.png",
            "./img/card/card_heart_07.png",
            "./img/card/card_heart_08.png",
            "./img/card/card_heart_09.png",
            "./img/card/card_heart_10.png",
            "./img/card/card_heart_11.png",
            "./img/card/card_heart_12.png",
            "./img/card/card_heart_13.png",
            "./img/card/card_diamond_01.png",
            "./img/card/card_diamond_02.png",
            "./img/card/card_diamond_03.png",
            "./img/card/card_diamond_04.png",
            "./img/card/card_diamond_05.png",
            "./img/card/card_diamond_06.png",
            "./img/card/card_diamond_07.png",
            "./img/card/card_diamond_08.png",
            "./img/card/card_diamond_09.png",
            "./img/card/card_diamond_10.png",
            "./img/card/card_diamond_11.png",
            "./img/card/card_diamond_12.png",
            "./img/card/card_diamond_13.png",
            "./img/card/card_club_01.png",
            "./img/card/card_club_02.png",
            "./img/card/card_club_03.png",
            "./img/card/card_club_04.png",
            "./img/card/card_club_05.png",
            "./img/card/card_club_06.png",
            "./img/card/card_club_07.png",
            "./img/card/card_club_08.png",
            "./img/card/card_club_09.png",
            "./img/card/card_club_10.png",
            "./img/card/card_club_11.png",
            "./img/card/card_club_12.png",
            "./img/card/card_club_13.png",
            "./img/card/card_joker.png"
    };
    private final BufferedImage[] card = new BufferedImage[cardSrcList.length];
    private final String[] chipSrcList = {
            "./img/chip/chip_vertical.png",
            "./img/chip/chip_perspective.png"
    };
    private final BufferedImage[] chip = new BufferedImage[chipSrcList.length];
    private final int CARD_BACK = 0;
    private int deckTopX = 0;
    private int deckTopY = 0;
    private final TextCenterLineBack textCenterLineBack;
    private final FadeInCenterText fadeInCenterText;

    public DrawComponents() {
        textCenterLineBack = new TextCenterLineBack();
        fadeInCenterText = new FadeInCenterText();
    }

    /* 画像読み込み */
    public void bufferInclude() {
        try {
            BG = ImageIO.read(new File("./img/bg.png"));
            for(int i=0; i<cardSrcList.length; ++i) { card[i] = ImageIO.read(new File(cardSrcList[i])); }
            for(int i=0; i<chipSrcList.length; ++i) { chip[i] = ImageIO.read(new File(chipSrcList[i])); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 汎用描画 */

    // 背景
    public void bgDraw(Graphics2D g2) { g2.drawImage(BG, 0, 0, null); }
    // 影つけ
    public void shadowedText(Graphics2D g2, String text, int x, int y, Color color, int shadowPosition, Color shadowColor){
        // 影
        g2.setColor(shadowColor);
        g2.drawString(text, x+shadowPosition, y+shadowPosition);
        // 本文
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
    // センタリング
    public void centeringText(Graphics2D g2, String text, int x, int y){
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
        x=x-rectText.width/2;
        y=y-rectText.height/2+fm.getMaxAscent();

        g2.drawString(text, x, y);
    }
    // センタリング・影付き
    public void centeringText(Graphics2D g2, String text, int x, int y, Color color, int shadowPosition, Color shadowColor){
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
        x=x-rectText.width/2;
        y=y-rectText.height/2+fm.getMaxAscent();
        // 影
        g2.setColor(shadowColor);
        g2.drawString(text, x+shadowPosition, y+shadowPosition);
        // 本文
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
    // センタリング・縁付き
    public void centeringTextWithEdge(Graphics2D g2, String text, int x, int y, Color color, int edgeDepth, Color shadowColor){
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
        x=x-rectText.width/2;
        y=y-rectText.height/2+fm.getMaxAscent();
        // ふち
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setColor(shadowColor);
        g2.drawString(text, x-edgeDepth, y-edgeDepth);
        g2.drawString(text, x-edgeDepth, y+edgeDepth);
        g2.drawString(text, x+edgeDepth, y-edgeDepth);
        g2.drawString(text, x+edgeDepth, y+edgeDepth);
        // 本文
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
    // センタリング1行・帯付き
    public void textCenterLineBack (Graphics2D g2, String line) {
        textCenterLineBack.call(g2);
        centeringText(g2, line, 400, 275, new Color(255, 255, 255, textCenterLineBack.getTextAlpha()), 4, new Color(0,0,0,textCenterLineBack.getTextAlpha() / 4));
    }
    // センタリング2行・帯付き
    public void textCenterLineBack (Graphics2D g2, String line1, String line2) {
        textCenterLineBack.call(g2);
        centeringText(g2,line1, 400, 245, new Color(255, 255, 255, textCenterLineBack.getTextAlpha()), 4, new Color(0,0,0,textCenterLineBack.getTextAlpha() / 4));
        centeringText(g2,line2, 400, 305, new Color(255, 255, 255, textCenterLineBack.getTextAlpha()), 4, new Color(0,0,0,textCenterLineBack.getTextAlpha() / 4));
    }

    /* スプライト */

    // タイトル背景のカードたち
    public void ttlBGDraw(Graphics2D g2) {
        g2.drawImage(card[11], 100, 350, null);
        g2.drawImage(card[1], 130, 320, null);
    }
    // デッキ
    public void deckDraw(Graphics2D g2 ,int x, int y, int amount) {
        for (int i=0; i<amount; ++i) g2.drawImage(card[CARD_BACK], x, y - (i/2), 80,120,null);
        deckTopX = x;
        if (y > deckTopY) deckTopY = y;
    }
    // 手札
    public void myHandDraw(Graphics2D g2, int order, Card myCard) {
        switch (myCard.getAnimationPhase()) {
            case "in deck" -> {
                myCard.setPosX(deckTopX);
                myCard.setPosY(deckTopY);
                myCard.setAnimationPhase("moving to hand");
            }
            case "moving to hand" -> {
                final int destinationX = 100+(order*90);
                final int destinationY = deckTopY+160;
                // 合計移動距離を算出
                final int xMoveLength = (Math.abs(deckTopX - destinationX) == 0) ? 1 : Math.abs(deckTopX - destinationX);
                final int yMoveLength = Math.abs(deckTopY - destinationY);
                // 移動速度
                final float v = 15;
                // x, yの速度を算出
                final double d = Math.sqrt( (Math.pow(xMoveLength, 2) + Math.pow(yMoveLength, 2)) );
                final double flame = (d/v);
                final int vx = (int)Math.ceil((double)xMoveLength/flame);
                final int vy = (int)Math.ceil((double)yMoveLength/flame);
                // 移動
                if (Math.abs(deckTopX - destinationX) > Math.abs(deckTopX - myCard.getPosX())) myCard.modPosX((deckTopX - destinationX >= 0) ? -vx : vx);
                if (Math.abs(deckTopX - myCard.getPosX()) >= Math.abs(deckTopX - destinationX)) myCard.setPosX(destinationX);
                if (myCard.getPosY() < destinationY) myCard.modPosY(vy);
                if (myCard.getPosY() >= destinationY) myCard.setPosY(destinationY);
                g2.drawImage(card[CARD_BACK], myCard.getPosX(), myCard.getPosY(), 80,120,null);
                if (myCard.getPosX() == destinationX && myCard.getPosY() == destinationY) myCard.setAnimationPhase("on hand");
            }
            case "on hand" -> g2.drawImage(card[(myCard.isFace()) ? myCard.getNumber() + (13*
                switch (myCard.getSuit()) {
                    case "Spade" -> 0;
                    case "Heart" -> 1;
                    case "Diamond" -> 2;
                    case "Club" -> 3;
                    default -> 4;
                }
            ) : CARD_BACK], 100 + (order*90), deckTopY+160, 80,120,null);
        }
    }
    public void opponentHandDraw(Graphics2D g2, int order, Card myCard) {
        switch (myCard.getAnimationPhase()) {
            case "in deck" -> {
                myCard.setPosX(deckTopX);
                myCard.setPosY(deckTopY);
                myCard.setAnimationPhase("moving to hand");
            }
            case "moving to hand" -> {
                final int destinationX = 620-(order*90);
                final int destinationY = deckTopY-200;
                // 合計移動距離を算出
                final int xMoveLength = (Math.abs(deckTopX - destinationX) == 0) ? 1 : Math.abs(deckTopX - destinationX);
                final int yMoveLength = Math.abs(deckTopY - destinationY);
                // 移動速度
                final float v = 15;
                // x, yの速度を算出
                final double d = Math.sqrt( (Math.pow(xMoveLength, 2) + Math.pow(yMoveLength, 2)) );
                final double flame = (d/v);
                final int vx = (int)Math.ceil((double)xMoveLength/flame);
                final int vy = (int)Math.ceil((double)yMoveLength/flame);
                // 移動
                if (Math.abs(deckTopX - destinationX) > Math.abs(deckTopX - myCard.getPosX())) myCard.modPosX((deckTopX - destinationX >= 0) ? -vx : vx);
                if (Math.abs(deckTopX - myCard.getPosX()) >= Math.abs(deckTopX - destinationX)) myCard.setPosX(destinationX);
                if (myCard.getPosY() > destinationY) myCard.modPosY(-vy);
                if (destinationY >= myCard.getPosY()) myCard.setPosY(destinationY);
                // 描画
                g2.drawImage(card[CARD_BACK], myCard.getPosX(), myCard.getPosY(), 80,120,null);
                // 手札なら
                if (myCard.getPosX() == destinationX && myCard.getPosY() == destinationY) myCard.setAnimationPhase("on hand");
            }
            case "on hand" -> g2.drawImage(card[(myCard.isFace()) ? myCard.getNumber() + (13*
                switch (myCard.getSuit()) {
                    case "Spade" -> 0;
                    case "Heart" -> 1;
                    case "Diamond" -> 2;
                    case "Club" -> 3;
                    default -> 4;
                }
            ) : CARD_BACK], 620 - (order*90), deckTopY-200, 80,120,null);
        }
    }

    // 賭け金・所持金閲覧ウィンドウ
    public void moneyInfoBox(Graphics2D g2, int x, int y, int havingMoney, int betMoney) {
        // フィールド
        RoundRectangle2D shape = new RoundRectangle2D.Double(x-80,y,300,80,80,80);
        g2.setPaint(new GradientPaint(0,0,new Color(128,128,128,128),0,100,new Color(0,0,0,128)));
        g2.fill(shape);
        // 枠線
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        // テキストのフォント
        g2.setFont(new Font("メイリオ",Font.PLAIN,20));
        // テキスト - 所持金
        shadowedText(g2, "総資金："+havingMoney,x+15,y+30, (havingMoney == 0) ? Color.RED : Color.WHITE, 2, Color.BLACK);
        // バー
        g2.setColor(Color.WHITE);
        g2.drawLine(x+5, y+40, (x+220)-10, y+40);
        // テキスト - 賭け金
        shadowedText(g2, "賭け金："+betMoney,x+15,y+65, Color.WHITE, 2, Color.BLACK);
    }

    // ポイント台
    public void pointTableDraw(Graphics2D g2) {
        g2.drawImage(chip[0], 116, 265,216, 373, (1328 / 4) * 2, (708 / 2), (1328 / 4) * 3, (708 / 2) * 2, null);
        g2.drawImage(chip[0], 587, 125, 687, 233, (1328 / 4) * 2, (708 / 2), (1328 / 4) * 3, (708 / 2) * 2, null);
        /* 画像サイズ
        0.93 : 1
        (332, 354)
         */
    }

    // 勝敗
    public void fadeInCenterText(Graphics2D g2, String text, int x, int y, Color color, int edgeDepth, Color shadow){
        fadeInCenterText.call(g2);
        centeringTextWithEdge(g2, text, x,y, new Color(color.getRed(), color.getGreen(), color.getBlue(),fadeInCenterText.getTextAlpha()), edgeDepth, new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(),fadeInCenterText.getTextAlpha()));
    }

    public int getLFadePhase() { return textCenterLineBack.getFadePhase(); }
    public void resetLFade() {textCenterLineBack.reSet();}
    public int getTFadePhase() { return fadeInCenterText.getFadePhase(); }
    public void resetTFade() {fadeInCenterText.reSet();}
}
