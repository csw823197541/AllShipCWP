package view;

import datamodel.GlobalData;
import entity1.VoyageInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Created by csw on 2016/12/15 14:36.
 * Explain:
 */
public class ResultPanel extends JPanel {

    private int width = GlobalData.reWidth;
    private int height = GlobalData.reHeight;

    private DecimalFormat df = new DecimalFormat("#.000");

    private int costHeight = height / 3;

    private int leftMargin = 80;

    private java.util.List<VoyageInfo> voyageInfoList;
    private long maxTime = 0, minTime = Long.MAX_VALUE;
    private Date stTime;

    private int hatchWidth = 50;
    private int cwpBlock = costHeight;

    private int m = 7200;

    private java.util.List<Ship> shipList;

    public ResultPanel() {
        voyageInfoList = new ArrayList<>(GlobalData.voyageMap.values());
        shipList = new ArrayList<>();
        initTime();
        initShip();
        initComponents();
    }

    private void initTime() {
        for (VoyageInfo voyageInfo : voyageInfoList) {
            long st = voyageInfo.getVOTPWKSTTM().getTime() / 1000;
            long ed = voyageInfo.getVOTPWKENTM().getTime() / 1000;
            if (st < minTime) {
                minTime = st;
                stTime = voyageInfo.getVOTPWKSTTM();
            }
            if (ed > maxTime) {
                maxTime = ed;
            }
        }
        maxTime = maxTime - minTime;
    }

    private void initShip() {
        for (VoyageInfo voyageInfo : voyageInfoList) {
            int sp = voyageInfo.getSTARTPOSITION();
            int ep = voyageInfo.getENDPOSITION();
            long st = voyageInfo.getVOTPWKSTTM().getTime() / 1000;
            long ed = voyageInfo.getVOTPWKENTM().getTime() / 1000;
            int t = (int) (ed - st);
            int s = (int) (st - minTime);
            Ship ship = new Ship(leftMargin + sp, 0, ep - sp);
            double h = (double) t / maxTime;
            int height = (int) ((cwpBlock - hatchWidth) * h);
            int sx = leftMargin + sp;
            double d = (double) s / maxTime;
            int sy = (int) ((cwpBlock - hatchWidth) * d) + hatchWidth;
            ship.setPlanX(sx);
            ship.setPlanY(sy);
            ship.setPlanHeight(height);
            shipList.add(ship);
        }
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(width, height);
        this.setOpaque(true);

        for (Ship ship : shipList) {
            ShipPanel shipPanel = new ShipPanel(ship);
//            shipPanel.setBackground(Color.CYAN);
            this.add(shipPanel);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, costHeight, width, costHeight);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(leftMargin, 0, leftMargin, height);

        g2d.setStroke(new BasicStroke(1));
        g2d.drawString("总时间:", leftMargin - 50, (float) (hatchWidth / 4));
        g2d.drawString(secToTime((int) maxTime), leftMargin - 50, hatchWidth / 2);
        int timeStep = (int) maxTime % m == 0 ? (int) maxTime / m : (int) maxTime / m + 1;
        for (int j = 0; j <= timeStep; j++) {
            String tStr = secToTime(j * m);
            g2d.drawString(tStr, leftMargin - 50, j * (cwpBlock - hatchWidth) / timeStep + hatchWidth + 5);
            g2d.drawLine(leftMargin - 5, j * (cwpBlock - hatchWidth) / timeStep + hatchWidth,
                    leftMargin, j * (cwpBlock - hatchWidth) / timeStep + hatchWidth);
        }
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            hour = minute / 60;
            minute = minute % 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute);
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        return i >= 0 && i < 10 ? "0" + Integer.toString(i) : "" + i;
    }
}
