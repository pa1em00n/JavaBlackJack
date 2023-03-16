import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    //private final int innerWindowX = 800;
    //private final int innerWindowY = 600;
    private int windowX;
    private int windowY;
    public GameWindow() {
        /* create window */
        super();

        expandFrameSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        /* create panel */
        GamePanel p = new GamePanel();
        p.setBounds(0, 0, windowX, windowY);
        add(p);
        new Thread(p).start();
    }

    private void expandFrameSize() {
        setVisible(true);
        Insets insets = getInsets();
        windowX = 800 + insets.left + insets.right;
        windowY = 600 + insets.top + insets.bottom;
        setSize(windowX, windowY);
    }
}
