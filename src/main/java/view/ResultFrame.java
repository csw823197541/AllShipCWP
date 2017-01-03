package view;

import datamodel.GlobalData;

import javax.swing.*;
import java.awt.*;

/**
 * Created by csw on 2016/12/15 12:29.
 * Explain:
 */
public class ResultFrame extends JFrame {

    private JScrollPane scrollPane;

    public ResultFrame() {
        initComponents();
    }

    private void initComponents() {
        this.setTitle("算法结果页面");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(GlobalData.reWidth, GlobalData.reHeight);
        this.setResizable(true);
        this.setLocationRelativeTo(null);// 居中显示

        ResultPanel panel = new ResultPanel();

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.getContentPane().setLayout(new BorderLayout(0, 0));
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        ResultFrame resultFrame = new ResultFrame();
        resultFrame.setVisible(true);
    }
}
