package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by csw on 2016/12/13 14:21.
 * Explain:
 */
public class SwingThreadTest3 extends JFrame {

    private static final String STR = "Completed : ";
    private JProgressBar progressBar = new JProgressBar();
    private JTextField text = new JTextField(10);
    private JButton start = new JButton("Start");
    private JButton end = new JButton("End");
    private boolean flag = false;
    private int count = 0;

    private GoThread t = null;

    private Runnable run = null;//更新组件的线程

    public SwingThreadTest3() {
        this.setLayout(new FlowLayout());
        add(progressBar);
        text.setEditable(false);
        add(text);
        add(start);
        add(end);
        start.addActionListener(new Start());
        end.addActionListener(new End());

        run = new Runnable(){//实例化更新组件的线程
            public void run() {
                progressBar.setValue(count);
                text.setText(STR + String.valueOf(count) + "%");
            }
        };
    }

    private void go() {
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
    }

    private class Start implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            flag = true;
            if(t == null){
                t = new GoThread();
                t.start();
            }
        }
    }

    private class End implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            flag = false;
        }
    }

    class GoThread extends Thread{
        public void run() {
            //do something...
            go();
        }
    }

    public static void main(String[] args) {
        SwingThreadTest3 fg = new SwingThreadTest3();
        fg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fg.setSize(300, 100);
        fg.setVisible(true);
    }
}
