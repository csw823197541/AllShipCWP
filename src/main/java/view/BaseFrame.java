package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by csw on 2016/12/15 9:18.
 * Explain:
 */
public abstract class BaseFrame extends JInternalFrame {

    public JPanel centerPanel;
    public JPanel contentPanel;
    public JScrollPane scrollPane;
    public JTable table;
    public DefaultTableModel tableModel;

    public int selectRow = -1;
    public String oldValue;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BaseFrame(String title, java.util.List<String> columnList) {
        super(title, true, false, true, true);
        centerPanel = new JPanel();
        centerPanel.setBorder(new TitledBorder(null, title,
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        this.setLayout(new BorderLayout(0, 0));
        this.add(contentPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));
        scrollPane = new JScrollPane();
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);
        tableModel = new DefaultTableModel();
        columnList.forEach(tableModel::addColumn);

        JPopupMenu popup = new JPopupMenu();
        JMenuItem jm_menu = new JMenuItem("删除");
        jm_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("删除行: " + selectRow);
                if (selectRow != -1) {
                    tableModel.removeRow(selectRow);
                }
                selectRow = -1;
            }
        });
        popup.add(jm_menu);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    selectRow = table.getSelectedRow();
                    oldValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                }
                if (selectRow != -1) {
                    if (e.isMetaDown()) {
                        popup.show(table, e.getX(), e.getY());
                    }
                }
            }
        });
    }

}
