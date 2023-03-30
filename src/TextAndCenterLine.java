import java.awt.*;

public class TextAndCenterLine {
    private String mode = "fadeIn";
    private int textAlpha = 0;
    private int lineBgPosition = 800;
    private int wait = 30;

    public void animate() {
        switch (mode) {
            case "fadeIn" -> {
                if (textAlpha < 255) {
                    textAlpha += 20;
                    if (lineBgPosition != 0) lineBgPosition -= 160;
                }
                if (textAlpha >= 255) {
                    textAlpha = 255;
                    mode = "stay";
                }
            }
            case "stay" -> {
                if (wait <= 0) mode = "fadeOut";
                --wait;
            }
            case "fadeOut" -> {
                if (textAlpha <= 255) {
                    textAlpha -= 20;
                    if (lineBgPosition != -800) lineBgPosition -= 160;
                }
                if (textAlpha <= 0) {
                    textAlpha = 0;
                    mode = "sleep";
                }
            }
        }
    }
    public void reSet() {
        mode = "fadeIn";
        textAlpha = 0;
        lineBgPosition = 800;
        wait = 30;
    }

    public int getLineBgPosition() { return lineBgPosition; }

    public int getTextAlpha() { return textAlpha; }
    public String getMode() { return mode; }
}
