import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

public class GeneralButton extends JButton implements MouseListener {
    private final String innerText;
    private Font innerTextFont = new Font("メイリオ",Font.PLAIN,30);
    private Color lightColor = new Color(96, 96, 96, 255);
    private Color cursoredColor = new Color(128, 128, 128, 255);
    private Color shadowColor = new Color(64, 64, 64, 255);
    private boolean isCursored;
    private final int edgeBold;
    public GeneralButton(String text) {
        super();
        innerText = text;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(this);
        edgeBold = 2;
    }

    public void setInnerTextFont(Font innerTextFont) { this.innerTextFont = innerTextFont; }
    public void changeBgColor(String theme) {
        switch (theme) {
            case "RED" -> {
                lightColor = new Color(255, 96, 96, 255);
                cursoredColor = new Color(255, 128, 128, 255);
                shadowColor = new Color(255, 64, 64, 255);
            }
            case "BLUE" -> {
                lightColor = new Color(96, 96, 255, 255);
                cursoredColor = new Color(96, 128, 255, 255);
                shadowColor = new Color(96, 64, 255, 255);
            }
            case "GREEN" -> {
                lightColor = new Color(96, 255, 96, 255);
                cursoredColor = new Color(96, 255, 128, 255);
                shadowColor = new Color(96, 255, 64, 255);
            }
            default -> {
                lightColor = new Color(96, 96, 96, 255);
                cursoredColor = new Color(128, 128, 128, 255);
                shadowColor = new Color(64, 64, 64, 255);
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // フィールド
        RoundRectangle2D shape = new RoundRectangle2D.Double(edgeBold, edgeBold, getWidth()-(2*edgeBold), getHeight()-(2*edgeBold), (getHeight()/2.0)-(2*edgeBold), (getHeight()/2.0)-(2*edgeBold));
        if (!isCursored) g2.setPaint(new GradientPaint(0,0, lightColor,0, getHeight(), shadowColor));
        if (isCursored) g2.setPaint(new GradientPaint(0,0, cursoredColor,0, getHeight(), shadowColor));
        g2.fill(shape);
        // 枠線
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(edgeBold));
        g2.draw(shape);
        // テキスト
        g2.setFont(innerTextFont);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(innerText, g2).getBounds();
        g2.drawString(innerText, (getWidth()/2)-rectText.width/2, (getHeight()/2)-rectText.height/2+fm.getMaxAscent());
    }
    @Override
    public void mouseEntered(MouseEvent e) { isCursored = true; }
    @Override
    public void mouseExited(MouseEvent e) { isCursored = false; }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
