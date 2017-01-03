package view;


import datamodel.GlobalData;
import entity1.CraneInfo;
import entity1.VoyageInfo;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2016/12/15 10:55.
 * Explain:
 */
public class VoyageFrame extends BaseFrame {

    private List<VoyageInfo> voyageInfoList;

    public VoyageFrame(List<VoyageInfo> voyageInfoList) {
        super("航次信息", Arrays.asList("航次", "船舶Id", "开工时间", "完工时间", "船头位置", "船尾位置"));
        this.voyageInfoList = voyageInfoList;
        initComponents();
    }

    private void initComponents() {
        setSize(700, 300);
        setVisible(true);

        for (VoyageInfo voyage : voyageInfoList) {
            Object[] rowData = new Object[6];
            rowData[0] = voyage.getVOTVOYID();
            rowData[1] = voyage.getVESSELID();
            rowData[2] = super.sdf.format(voyage.getVOTPWKSTTM());
            rowData[3] = super.sdf.format(voyage.getVOTPWKENTM());
            rowData[4] = voyage.getSTARTPOSITION();
            rowData[5] = voyage.getENDPOSITION();
            tableModel.addRow(rowData);
        }
        table.setModel(tableModel);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        super.tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.DELETE) {
                    GlobalData.voyageMap.remove(selectRow);
                    System.out.println("改变全局变量航次信息,删除一条记录后：" + GlobalData.voyageMap.size());
                } else if (e.getType() == TableModelEvent.UPDATE) {
                    String newValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                    if (!newValue.equals(oldValue)) {
                        try {
                            setNewValue(table.getSelectedRow(), table.getSelectedColumn());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("改变全局变量航次信息：" + oldValue + "->" + newValue);
                    }
                }
            }
        });
    }

    private void setNewValue(int row, int column) throws ParseException {
        Object value = table.getValueAt(row, column);
        VoyageInfo voyageInfo = GlobalData.voyageMap.get(row);
        switch (column) {
            case 0:
                voyageInfo.setVOTVOYID(Integer.valueOf(value.toString()));
                break;
            case 1:
                voyageInfo.setVESSELID((String) value);
                break;
            case 2:
                voyageInfo.setVOTPWKSTTM(sdf.parse((String) value));
                break;
            case 3:
                voyageInfo.setVOTPWKENTM(sdf.parse((String) value));
                break;
            case 4:
                voyageInfo.setSTARTPOSITION(Integer.valueOf(value.toString()));
                break;
            case 5:
                voyageInfo.setENDPOSITION(Integer.valueOf(value.toString()));
                break;
        }
        System.out.println("更新后的记录：" + GlobalData.voyageMap.get(row));
    }

}
