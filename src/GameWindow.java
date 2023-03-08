import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;
    private final int windowX = 800;
    private final int windowY = 600;
    public GameWindow() {
        /* create window */
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Insets insets = getInsets();
        int width = windowX + insets.left + insets.right;
        int height = windowY + insets.top + insets.bottom;
        setSize(width, height);

        /* create panel */
        gamePanel = new GamePanel(windowX, windowY);
    }

    public void redraw(GameEngine engine) {

        gamePanel.repaint();
    }
}
