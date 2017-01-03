package view;

import datamodel.GlobalData;
import entity1.CraneInfo;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2016/12/15 9:32.
 * Explain:
 */
public class CraneFrame extends BaseFrame {

    private List<CraneInfo> craneInfoList;

    public CraneFrame(List<CraneInfo> craneInfoList) {
        super("桥机信息", Arrays.asList("桥吊ID", "当前位置", "卸20尺箱效率", "卸40尺箱效率", "卸船双吊具效率", "装20尺箱效率", "装40尺箱效率", "装船双吊具效率", "移动范围起始", "移动范围终止", "桥吊编号", "安全距离", "桥吊序列号", "移动速度", "桥吊宽度", "工作时间"));
        this.craneInfoList = craneInfoList;
        initComponents();
    }

    private void initComponents() {
        this.setSize(900, 400);
        this.setVisible(true);

        for (CraneInfo craneInfo : this.craneInfoList) {
            Object[] rowData = new Object[16];
            rowData[0] = craneInfo.getID();
            rowData[1] = craneInfo.getCURRENTPOSITION();
            rowData[2] = craneInfo.getDISCHARGEEFFICIENCY20();
            rowData[3] = craneInfo.getDISCHARGEEFFICIENCY40();
            rowData[4] = craneInfo.getDISCHARGEEFFICIENCYTWIN();
            rowData[5] = craneInfo.getLOADINGEFFICIENCY20();
            rowData[6] = craneInfo.getLOADINGEFFICIENCY40();
            rowData[7] = craneInfo.getLOADINGEFFICIENCYTWIN();
            rowData[8] = craneInfo.getMOVINGRANGEFROM();
            rowData[9] = craneInfo.getMOVINGRANGETO();
            rowData[10] = craneInfo.getNAME();
            rowData[11] = craneInfo.getSAFESPAN();
            rowData[12] = craneInfo.getSEQ();
            rowData[13] = craneInfo.getSPEED();
            rowData[14] = craneInfo.getWIDTH();
            rowData[15] = super.sdf.format(craneInfo.getWORKINGTIMERANGES().get(0).getWORKSTARTTIME()) + "--" + super.sdf.format(craneInfo.getWORKINGTIMERANGES().get(0).getWORKENDTIME());
            super.tableModel.addRow(rowData);
        }
        super.table.setModel(super.tableModel);
        super.table.getColumnModel().getColumn(15).setPreferredWidth(250);

        super.tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.DELETE) {
                    GlobalData.craneInfoMap.remove(selectRow);
                    System.out.println("改变全局变量桥机信息,删除一条记录后：" + GlobalData.craneInfoMap.size());
                } else if (e.getType() == TableModelEvent.UPDATE) {
                    String newValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                    if (!newValue.equals(oldValue)) {
                        setNewValue(table.getSelectedRow(), table.getSelectedColumn());
                        System.out.println("改变全局变量桥机信息：" + oldValue + "->" + newValue);
                    }
                }
            }
        });
    }

    private void setNewValue(int row, int column) {
        Object value = table.getValueAt(row, column);
        CraneInfo craneInfo = GlobalData.craneInfoMap.get(row);
        switch (column) {
            case 0:
                craneInfo.setID((String) value);
                break;
            case 1:
                craneInfo.setCURRENTPOSITION(Integer.valueOf(value.toString()));
                break;
        }
        System.out.println("更新后的记录：" + GlobalData.craneInfoMap.get(row));
    }
}
