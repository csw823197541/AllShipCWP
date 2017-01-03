package view;

import datamodel.GlobalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by csw on 2016/12/13 14:48.
 * Explain:
 */
public class ImportDataFrame extends JFrame {

    private JPanel mainPanel = null;
    private JPanel btnPanel = null;
    private CardLayout card = null;
    private JButton voyCraneBnt = null, vesselStructBnt = null, preStowBnt = null;
    private JDesktopPane vesselStructPane = null, preStowPane = null;
    private JDesktopPane voyCranePane = null;

    private JButton executeBnt;

    public ImportDataFrame() {
        initComponents();
    }

    private void initComponents() {

        this.setTitle("算法数据导入界面");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GlobalData.width, GlobalData.height);
        this.setResizable(true);
        this.setLocationRelativeTo(null);// 居中显示

        executeBnt = new JButton("全岸CWP规划");
        executeBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultFrame resultFrame = new ResultFrame();
                resultFrame.setVisible(true);
            }
        });

        voyCraneBnt = new JButton("航次桥机");
        vesselStructBnt = new JButton("船舶结构");
        preStowBnt = new JButton("预配数据");

        voyCraneBnt.setMargin(new Insets(2, 2, 2, 2));
        vesselStructBnt.setMargin(new Insets(2, 2, 2, 2));
        preStowBnt.setMargin(new Insets(2, 2, 2, 2));

        btnPanel = new JPanel();
        btnPanel.setBackground(Color.LIGHT_GRAY);
        btnPanel.add(voyCraneBnt);
        btnPanel.add(vesselStructBnt);
        btnPanel.add(preStowBnt);
        btnPanel.add(executeBnt);

        card = new CardLayout(0, 0);
        mainPanel = new JPanel(card);
        voyCranePane = new JDesktopPane();
        vesselStructPane = new JDesktopPane();
        preStowPane = new JDesktopPane();
        voyCranePane.setBackground(Color.LIGHT_GRAY);
        vesselStructPane.setBackground(Color.CYAN);
        preStowPane.setBackground(Color.lightGray);
        mainPanel.add(voyCranePane, "p1");
        mainPanel.add(vesselStructPane, "p2");
        mainPanel.add(preStowPane, "p3");
        voyCraneBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p1");
            }
        });
        vesselStructBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p2");
            }
        });
        preStowBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p3");
            }
        });

        VoyageFrame voyageFrame = new VoyageFrame(new ArrayList<>(GlobalData.voyageMap.values()));
        CraneFrame craneFrame = new CraneFrame(new ArrayList<>(GlobalData.craneInfoMap.values()));
        voyCranePane.add(voyageFrame);
        voyCranePane.add(craneFrame);

        this.getContentPane().add(mainPanel);
        this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }
}
