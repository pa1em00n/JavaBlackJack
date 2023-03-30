import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

public class SidePositionedButton extends JButton implements MouseListener {
    private final String innerText;
    private boolean isCursored;
    private final int edgeBold;
    public SidePositionedButton(String text) {
        super();
        innerText = text;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(this);
        edgeBold = 4;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // フィールド
        RoundRectangle2D shape = new RoundRectangle2D.Double(edgeBold, edgeBold, getWidth()+getHeight()-(2*edgeBold), getHeight()-(2*edgeBold), getHeight()-(2*edgeBold), getHeight()-(2*edgeBold));
        if (!isCursored) g2.setPaint(new GradientPaint(0,0,new Color(128,128,128,192),0,getHeight(),new Color(0,0,0,192)));
        if (isCursored) g2.setPaint(new GradientPaint(0,0,new Color(128,128,128,255),0,getHeight(),new Color(0,0,0,255)));
        g2.fill(shape);
        // 枠線
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(edgeBold));
        g2.draw(shape);
        // テキスト
        g2.setFont(new Font("メイリオ",Font.PLAIN,30));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(innerText, g2).getBounds();
        g2.drawString(innerText, (getWidth()/2)-rectText.width/2, (getHeight()/2)-rectText.height/2+fm.getMaxAscent());
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        setBounds(getX()-20, getY(), getWidth()+20, getHeight());
        isCursored = true;
    }
    @Override
    public void mouseExited(MouseEvent e) {
        setBounds(getX()+20, getY(), getWidth()-20, getHeight());
        isCursored = false;
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
