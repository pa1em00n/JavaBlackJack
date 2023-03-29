import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class nazokurasu extends JButton {
    public nazokurasu() {
        super();
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int x=0;
        int y=0;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // フィールド
        RoundRectangle2D shape = new RoundRectangle2D.Double(x,y,300,80,80,80);
        g2.setPaint(new GradientPaint(0,0,new Color(128,128,128,128),0,100,new Color(0,0,0,128)));
        g2.fill(shape);
        // 枠線
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
    }
}
