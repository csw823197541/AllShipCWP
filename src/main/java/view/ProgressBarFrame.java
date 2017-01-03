package view;

import datamodel.GlobalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by csw on 2016/12/15 11:38.
 * Explain:
 */
public class ProgressBarFrame extends JInternalFrame {

    private static final String STR = "Completed: ";
    private JProgressBar progressBar = new JProgressBar();
    private JTextField text = new JTextField(10);
    private boolean flag = true;
    private int count = 0;

    public GoThread t = new GoThread();

    private Runnable run = null;//更新组件的线程

    public ProgressBarFrame() {
        super("程序处理进度", true ,true);
        this.setBounds(GlobalData.width - 350, GlobalData.height - 180, 300, 80);
        this.setVisible(true);
        this.setLayout(new FlowLayout());
        add(progressBar);
        text.setEditable(false);
        add(text);

        run = new Runnable(){//实例化更新组件的线程
            public void run() {
                progressBar.setValue(count);
                text.setText(STR + String.valueOf(count) + "%");
            }
        };
    }

    class GoThread extends Thread{
        public void run() {

            while (count < 100) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag) {
                    count++;
                    SwingUtilities.invokeLater(run);//将对象排到事件派发线程的队列中
                }
            }
            if (count == 100) {
                flag = false;
            }
        }
    }
}
