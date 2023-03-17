import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawComponents {
    private BufferedImage BG;
    private int fadePhase = 0;
    private int textAlpha = 0;
    private int lineBgPosition = 800;
    private int wait = 0;

    public DrawComponents() {
        fadePhase = 0;
    }

    /* 画像読み込み */
    public void bufferInclude() {
        try {
            BG = ImageIO.read(new File("./img/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 汎用描画系 */
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
    public void textCenterLineBack (Graphics2D g2, String line1, String line2) {
        g2.setColor(new Color(0,0,0,60));
        g2.fillRect(lineBgPosition,220, 800, 110);
        g2.setFont(new Font("メイリオ",Font.PLAIN,50));
        centeringText(g2,line1, 400, 245, new Color(255, 255, 255, textAlpha), 4, new Color(0,0,0,textAlpha / 4));
        centeringText(g2,line2, 400, 305, new Color(255, 255, 255, textAlpha), 4, new Color(0,0,0,textAlpha / 4));
        switch (fadePhase) {
            case 0 -> {
                if (textAlpha < 255) {
                    textAlpha += 20;
                    if (lineBgPosition != 0) lineBgPosition -= 160;
                }
                if (textAlpha >= 255) {
                    textAlpha = 255;
                    fadePhase = 1;
                    wait = 30;
                }
            }
            case 1 -> {
                if (wait <= 0) fadePhase = 2;
                --wait;
            }
            case 2 -> {
                if (textAlpha <= 255) {
                    textAlpha -= 20;
                    if (lineBgPosition != -800) lineBgPosition -= 160;
                }
                if (textAlpha <= 0) {
                    textAlpha = 0;
                    fadePhase = -1;
                }
            }
            /*
            case -1 -> {
                // reset
                textAlpha = 0;
                fadePhase = 0;
                wait = 0;
                lineBgPosition = 800;
            }
             */
        }
    }

    public int getFadePhase() { return fadePhase; }

    public void setFadePhase(int fadePhase) { this.fadePhase = fadePhase; }
}
