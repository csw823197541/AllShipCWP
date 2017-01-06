package viewFrme;

import importDataInfo.CraneInfo;
import importDataInfo.CwpResultInfo;
import importDataInfo.VesselStructureInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by csw on 2016/3/13.
 */
public class CwpResultFrame extends JFrame {

    private List<CwpResultInfo> cwpResultInfoList;
    private List<CraneInfo> craneInfoList;
    private List<VesselStructureInfo> vesselStructureInfoList;

    public CwpResultFrame(List<CwpResultInfo> cwpResultInfoList, List<CraneInfo> craneInfoList, List<VesselStructureInfo> vesselStructureInfoList) {
        this.cwpResultInfoList = cwpResultInfoList;
        this.craneInfoList = craneInfoList;
        this.vesselStructureInfoList = vesselStructureInfoList;
        initComponents();
    }

    private void initComponents() {
        this.setTitle("cwp计划结果图");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 600);
        this.setResizable(true);
//        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);// 居中显示
        this.getContentPane().setLayout(new BorderLayout());
        CwpResultPanel2 cwpResultPanel = new CwpResultPanel2(cwpResultInfoList, craneInfoList, vesselStructureInfoList);
        JScrollPane scrollPane = new JScrollPane(cwpResultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
