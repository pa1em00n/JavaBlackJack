import java.awt.*;

public class FadeInCenterText {
    private int fadePhase = 0;
    private int textAlpha = 0;
    private int wait = 0;
    private int fontSize = 100;
    public void call(Graphics2D g2) {
        g2.setColor(new Color(0,0,0,60));
        g2.setFont(new Font("メイリオ",Font.BOLD,fontSize));

        switch (fadePhase) {
            case 0 -> {
                if (textAlpha < 255) {
                    textAlpha += 20;
                    --fontSize;
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
                if (textAlpha <= 255) textAlpha -= 20;
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
    public void reSet() {
        fontSize = 100;
        fadePhase = 0;
        textAlpha = 0;
        wait = 0;
    }

    public int getTextAlpha() { return textAlpha; }
    public int getFadePhase() { return fadePhase; }
}
