package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * Created by csw on 2016/12/15 15:30.
 * Explain:
 */
public class ShipPanel extends JPanel {

    private int x, y, width;

    private int shipH = 50;
    private int bold = 3;

    private int[] shipX = new int[4];
    private int[] shipY = new int[4];

    private Ship ship;

    public ShipPanel(Ship ship) {
        this.ship = ship;
        this.x = ship.x;
        this.y = ship.y;
        this.width = ship.width;
        shipX = new int[]{0, 0 + width, 0 + shipH, 0 + width};
        shipY = new int[]{0, 0, 0 + shipH, 0 + shipH};
        this.setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(bold));

        g2d.drawLine(shipX[0], shipY[0], shipX[1], shipY[1]);
        g2d.drawLine(shipX[2], shipY[2], shipX[3], shipY[3]);
        QuadCurve2D curveLeft = new QuadCurve2D.Double(shipX[0], shipY[0], shipX[2] + 10, shipY[2] - 10, shipX[2], shipY[2]);
        g2d.draw(curveLeft);
        g2d.drawLine(shipX[1], shipY[1], shipX[3], shipY[3]);

        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(shipX[0], ship.planY, width, ship.planHeight);
//        g2d.fillRect(shipX[0], ship.planY, width, ship.planHeight);

        this.setOpaque(false);  //透明
        this.setBounds(x, y, width + bold / 2, ship.planHeight + ship.planY + shipH + bold / 2);

    }
}
