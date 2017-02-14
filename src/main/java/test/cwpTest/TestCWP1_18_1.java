package test.cwpTest;

import generateResult.GenerateCwpResult;
import generateResult.GenerateMoveOrder;
import importDataInfo.*;
import importDataProcess.*;
import utils.FileUtil;
import viewFrme.CwpResultFrame;
import viewFrme.PreStowageDataFrame;

import java.io.File;
import java.util.*;

/**
 * Created by csw on 2016/1/21.
 */
public class TestCWP1_18_1 {
    public static void main(String[] args) {

        String filePath = "data/cwp/1.18CWP1/";

        String vo = FileUtil.readFileToString(new File(filePath + "CwpVoyage.txt")).toString();

        String sh = FileUtil.readFileToString(new File(filePath + "CwpVslStr.txt")).toString();

        String cr = FileUtil.readFileToString(new File(filePath + "CwpCrane.txt")).toString();

        String pr = FileUtil.readFileToString(new File(filePath + "CwpPerStowage.txt")).toString();

        //航次
        List<VoyageInfo> voyageInfoList = VoyageInfoProcess.getVoyageInfo(vo);
//        VoyageFrame voyageFrame = new VoyageFrame(voyageInfoList);
//        voyageFrame.setVisible(true);

        //船舶结构
        List<VesselStructureInfo> vesselStructureInfoList = VesselStructureInfoProcess.getVesselStructureInfo(sh);
        ImportData.vesselStructureInfoList = vesselStructureInfoList;
//        VesselStructureFrame vesselStructureFrame = new VesselStructureFrame(vesselStructureInfoList);
//        vesselStructureFrame.setVisible(true);

        //桥机
        List<CraneInfo> craneInfoList = CraneInfoProcess.getCraneInfo(cr);
//        CraneFrame craneFrame = new CraneFrame(craneInfoList);
//        craneFrame.setVisible(true);

        //实配图
        List<PreStowageData> preStowageDataList = PreStowageDataProcess.getPreStowageInfo(pr);
//        PreStowageDataFrame preStowageFrame = new PreStowageDataFrame(preStowageDataList);
//        preStowageFrame.setVisible(true);

        //将数据放在不同的舱位里
        List<String> VHTIDs = new ArrayList<>();//存放舱位ID
        for(PreStowageData preStowageData : preStowageDataList) {
            if(!VHTIDs.contains(preStowageData.getVHTID())) {
                VHTIDs.add(preStowageData.getVHTID());
            }
        }
        Collections.sort(VHTIDs);
        System.out.println( "舱位数：" + VHTIDs.size());
        Map<String, List<Integer>> workFlowMap = new HashMap<>();
        for (String str : VHTIDs) {
            workFlowMap.put(str, Arrays.asList(1, 2));
        }

        //生成作业工艺和moveOrder
        Long moveOrderAndWorkFlowNo = 1L;
        List<PreStowageData> resultList = GenerateMoveOrder.getMoveOrderAndWorkFlow(moveOrderAndWorkFlowNo, voyageInfoList, preStowageDataList, vesselStructureInfoList, workFlowMap);
        System.out.println("提示信息：" + ExceptionProcess.getExceptionInfo(moveOrderAndWorkFlowNo));
//        PreStowageDataFrame preStowageFrame2 = new PreStowageDataFrame(resultList);
//        preStowageFrame2.setVisible(true);

        //调用cwp算法得到结果
        Long cwpNo = 2L;
        List<CwpResultInfo> cwpResultInfoList = GenerateCwpResult.getCwpResult(cwpNo, voyageInfoList, vesselStructureInfoList, craneInfoList, resultList);
        System.out.println("提示信息：" + ExceptionProcess.getExceptionInfo(cwpNo));

        //对cwp结果进行处理，将连续作业的cwp块放到一起，以及对作业于某个舱所有的桥机进行编顺序，和某桥机作业舱的顺序
        Long cwpNo1 = 21L;
        List<CwpResultInfo> cwpResultInfoTransformList =  CwpResultInfoTransform.getTransformResult(cwpNo1, cwpResultInfoList);
        System.out.println("提示信息：" + ExceptionProcess.getExceptionInfo(cwpNo1));
        CwpResultFrame cwpResultFrame = new CwpResultFrame(cwpResultInfoTransformList, craneInfoList, vesselStructureInfoList);
        cwpResultFrame.setVisible(true);

        //目前现对cwp结果进行处理，得到每一个Move的输出对象，即对现在算法结果进行拆分
        Long cwpNo2 = 22L;
        List<CwpResultMoveInfo> cwpResultInfoToMoveList = CwpResultInfoToMove.getCwpMoveInfoResult(cwpNo2, cwpResultInfoList, preStowageDataList);
        System.out.println("提示信息：" + ExceptionProcess.getExceptionInfo(cwpNo2));
//        CwpResultMoveInfoFrame cwpResultMoveInfoFrame = new CwpResultMoveInfoFrame(cwpResultInfoToMoveList);
//        cwpResultMoveInfoFrame.setVisible(true);

    }
}
