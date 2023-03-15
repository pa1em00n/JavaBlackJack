import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        /* create window */
        super();
        final int windowX = 800;
        final int windowY = 600;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Insets insets = getInsets();
        int width = windowX + insets.left + insets.right;
        int height = windowY + insets.top + insets.bottom;
        setSize(width, height);

        /* create panel */
        add(new GamePanel(windowX, windowY));
    }
}
