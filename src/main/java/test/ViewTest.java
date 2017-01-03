package test;


import datamodel.GlobalData;
import entity.Voyage;
import entity1.CraneInfo;
import entity1.CraneInfoProcess;
import entity1.VoyageInfo;
import entity1.VoyageInfoProcess;
import utils.FileUtil;
import view.ImportDataFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2016/12/13 15:03.
 * Explain:
 */
public class ViewTest {

    public static void main(String[] args) {

        String filePath = "12.06data/";

        String cr = FileUtil.readFileToString(new File(filePath + "CwpCrane.txt")).toString();
        String vo = FileUtil.readFileToString(new File(filePath + "CwpVoyage.txt")).toString();

        List<CraneInfo> craneInfoList = CraneInfoProcess.getCraneInfo(cr);
        for (int i = 0; i < craneInfoList.size(); i++) {
            GlobalData.craneInfoMap.put(i, craneInfoList.get(i));
        }

        List<VoyageInfo> voyageInfoList = VoyageInfoProcess.getVoyageInfo(vo);
        for (int i = 0; i < voyageInfoList.size(); i++) {
            GlobalData.voyageMap.put(i, voyageInfoList.get(i));
        }

        ImportDataFrame importDataFrame = new ImportDataFrame();
        importDataFrame.setVisible(true);
    }
}
