import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundRectTextField extends JFormattedTextField {
    public RoundRectTextField() { super(); }
    @Override
    public void setFormatterFactory(AbstractFormatterFactory tf) { super.setFormatterFactory(tf); }
    /* 角丸にするメソッド */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1, getWidth()-2, getHeight()-2, getHeight()-2, getHeight()-2);
        g2.setPaint(UIManager.getColor("TextField.background"));
        g2.fill(shape);
        g2.setPaint(Color.GRAY);
        g2.draw(shape);
        super.paintComponent(g);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }
}
