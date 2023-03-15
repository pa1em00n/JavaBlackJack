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
    private BufferedImage readImage;
    private final BjEngine engine;
    public GamePanel(int x, int y) {
        setSize(x, y);
        engine = new BjEngine();
        placeTtlComponents();
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

        g.drawString("ブラックジャック", 0, 0);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(readImage, 0, 0, null);
    }

    /* 遷移*/
    @Override
    public void actionPerformed(ActionEvent e) {
        final String cmd = e.getActionCommand();

        switch (cmd) {
            case "gotoTtl" -> {}
            case "gotoName" -> {}
            case "gotoBet" -> {}
            case "gotoGame" -> {}
            case "gosubHit" -> {}
            case "gosubStand" -> {}
            case "gotoExit" -> System.exit(0);
        }
    }

    private void placeTtlComponents() {
        // init
        removeAll();

        try {
            readImage = ImageIO.read(new File ("./img/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
