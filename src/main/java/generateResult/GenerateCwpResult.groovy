package generateResult

import cwp.CallCWP
import importDataInfo.CraneInfo
import importDataInfo.CwpResultInfo
import importDataInfo.HatchInfo
import importDataInfo.HatchPositionInfo
import importDataInfo.PreStowageData
import importDataInfo.VesselStructureInfo
import importDataInfo.VoyageInfo
import importDataInfo.WorkMoveInfo
import importDataInfo.WorkingTimeRange
import importDataProcess.CraneInfoProcess
import importDataProcess.CwpResultInfoProcess
import importDataProcess.ExceptionData
import importDataProcess.HatchInfoProcess
import importDataProcess.ImportData
import importDataProcess.WorkMoveInfoProcess
import utils.FileUtil

import java.text.DecimalFormat

/**
 * Created by csw on 2016/1/22.
 */
class GenerateCwpResult {

    public static DecimalFormat df = new DecimalFormat("#.00");

    public
    static List<CwpResultInfo> getCwpResult(Long batchNum,
                                            List<VoyageInfo> voyageInfoList,
                                            List<VesselStructureInfo> vesselStructureInfoList,
                                            List<CraneInfo> craneInfoList,
                                            List<PreStowageData> preStowageDataList) {
        ExceptionData.exceptionMap.put(batchNum, "接口方法没有执行。");
        List<CwpResultInfo> cwpResultInfoList = new ArrayList<>();
        try {

            List<HatchPositionInfo> hatchPositionInfoList = getHatchPositionInfoList(voyageInfoList, vesselStructureInfoList)

            List<HatchInfo> hatchInfoList = getHatchInfoList(voyageInfoList, hatchPositionInfoList, preStowageDataList);

            List<WorkMoveInfo> workMoveInfoList = getWorkMoveInfoList(preStowageDataList)

            //生成cwp算法要用的3个json串
            String craneJsonStr = CraneInfoProcess.getCraneInfoJsonStr(craneInfoList)
            String hatchJsonStr = HatchInfoProcess.getHatchInfoJsonStr(hatchInfoList)
            String moveJsonStr = WorkMoveInfoProcess.getWorkMoveInfoJsonStr(workMoveInfoList)

            try {
                FileUtil.writeToFile("toCwpData/hatch.txt", hatchJsonStr)
                FileUtil.writeToFile("toCwpData/crane.txt", craneJsonStr)
                FileUtil.writeToFile("toCwpData/moves.txt", moveJsonStr)
            } catch (Exception e) {
                e.printStackTrace()
            }
            //调用cwp算法
            if (craneJsonStr != null && hatchJsonStr != null && moveJsonStr != null) {
                String cwpResultStr = null

                try {
                    int craneSize = craneInfoList.size();
                    CraneInfo craneInfo0 = craneInfoList.get(0);
                    CraneInfo craneInfoI = craneInfoList.get(craneInfoList.size() - 1);
                    String increaseTime = String.valueOf((craneInfoI.getWORKINGTIMERANGES().get(0).getWORKSTARTTIME().time - craneInfo0.getWORKINGTIMERANGES().get(0).getWORKSTARTTIME().time) / 1000);
                    String decreaseTime = String.valueOf((craneInfoI.getWORKINGTIMERANGES().get(0).getWORKENDTIME().time - craneInfoI.getWORKINGTIMERANGES().get(0).getWORKSTARTTIME().time) / 1000);

                    //判断是否为驳船作业，1：船尾开始作业，卸；2：船头开始作业，装；0：普通船作业
                    boolean isAllL = true, isAllD = true, isLD = true;
                    for (PreStowageData preStowageData : preStowageDataList) {
                        if ("D".equals(preStowageData.getLDULD())) {
                            isAllL = false;
                        }
                        if ("L".equals(preStowageData.getLDULD())) {
                            isAllD = false;
                        }
                    }
                    String whatIsTheShip = "0"
                    if (isAllL) {
                        whatIsTheShip = "2"
                    } else if (isAllD) {
                        whatIsTheShip = "1"
                    }

//                    cwpResultStr = CallCwpTest.cwp(craneJsonStr, hatchJsonStr, moveJsonStr,
//                            craneSize + "", increaseTime, decreaseTime, whatIsTheShip);

                    //调用java版本的cwp
                    cwpResultStr = CallCWP.cwp(craneJsonStr, hatchJsonStr, moveJsonStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("cwp算法返回的json字符串:" + cwpResultStr);
                if (cwpResultStr != null) {
                    try {
                        FileUtil.writeToFile("toCwpData/cwpResult.txt", cwpResultStr)
                    } catch (Exception e) {
                        e.printStackTrace()
                    }

                    long voyageTime = voyageInfoList.get(0).getVOTPWKENTM().getTime() - voyageInfoList.get(0).getVOTPWKSTTM().getTime();

                    cwpResultInfoList = CwpResultInfoProcess.getCwpResultInfo(cwpResultStr, voyageInfoList, preStowageDataList);

                    int maxTime = 0;
                    for (CwpResultInfo cwpResultInfo : cwpResultInfoList) {
                        if (maxTime < cwpResultInfo.getWORKINGENDTIME()) {
                            maxTime = cwpResultInfo.getWORKINGENDTIME();
                        }
                    }

                    if (maxTime * 1000 > voyageTime) { //桥机资源不够，不能按船期完成
                        ExceptionData.exceptionMap.put(batchNum, "error! 桥机资源不够，cwp不能按船期完成。")
                    } else {
                        ExceptionData.exceptionMap.put(batchNum, "success! cwp按船期完成。")
                    }
                } else {
                    ExceptionData.exceptionMap.put(batchNum, "error! cwp发现未知问题，无法返回结果。")
                    System.out.println("cwp算法没有返回结果！")
                }
            } else {
                ExceptionData.exceptionMap.put(batchNum, "error! cwp算法需要的3个参数信息中有为空的，不能调用算法。")
                System.out.println("cwp算法需要的3个参数信息中有空的，不能调用算法！")
            }
            return cwpResultInfoList;
        } catch (Exception e) {
            ExceptionData.exceptionMap.put(batchNum, "error! cwp算法需要的3个参数信息中有为空的，不能调用算法。")
            System.out.println("cwp算法需要的3个参数信息中有空的，不能调用算法！")
            e.printStackTrace()
            return cwpResultInfoList;
        }

    }

    /**
     * 得到船舱的信息，用来生成cwp算法要用的json串
     * @param voyageInfoList
     * @param hatchPositionInfoList
     * @param movecounts
     * @return
     */
    public
    static List<HatchInfo> getHatchInfoList(List<VoyageInfo> voyageInfoList, List<HatchPositionInfo> hatchPositionInfoList, List<PreStowageData> preStowageDataList) throws Exception {
        System.out.println("开始生成船舱信息：");
        List<HatchInfo> hatchInfoList = new ArrayList<>()

        Map<String, Integer> moveCountQuery = new HashMap<>()
        //将数据放在不同的舱位里
        List<String> VHTIDs = new ArrayList<>()//存放舱位ID
        Map<String, List<PreStowageData>> stringListMap = new HashMap<>()//放在不同的舱位的数据
        for (PreStowageData preStowageData : preStowageDataList) {
            if (!VHTIDs.contains(preStowageData.getVHTID())) {
                VHTIDs.add(preStowageData.getVHTID())
            }
        }
        Collections.sort(VHTIDs)
        println "舱位数：" + VHTIDs.size()
        for (String str : VHTIDs) {//
            List<PreStowageData> dataList1 = new ArrayList<>()
            for (PreStowageData preStowageData : preStowageDataList) {
                if (str.equals(preStowageData.getVHTID())) {
                    dataList1.add(preStowageData)
                }
            }
            stringListMap.put(str, dataList1)
        }
        int t = 0
        for (String str : VHTIDs) {
            List<PreStowageData> dataList = stringListMap.get(str)
            List<Integer> orders = new ArrayList<>()
            for (PreStowageData preStowageData1 : dataList) {
                if (!orders.contains(preStowageData1.getMOVEORDER())) {
                    orders.add(preStowageData1.getMOVEORDER())
                }
            }
            println "舱id:" + str + "-moveCount数：" + orders.size()
            t += orders.size()
            moveCountQuery.put(str, orders.size())
        }
        println "总movecount数：" + t

        HatchInfo newHatchInfo;
        Date workingStartTime = voyageInfoList.get(0).getVOTPWKSTTM();
        Date workingEndTime = voyageInfoList.get(0).getVOTPWKENTM();
        String vesselID = voyageInfoList.get(0).getVESSELID() == null ? "" : String.valueOf(voyageInfoList.get(0).getVESSELID());
        WorkingTimeRange workingTimeRange = new WorkingTimeRange();
        workingTimeRange.setID(null);
        workingTimeRange.setWORKSTARTTIME(workingStartTime);
        workingTimeRange.setWORKENDTIME(workingEndTime);
        List<WorkingTimeRange> workingTimeRangeList = new ArrayList<WorkingTimeRange>();
        workingTimeRangeList.add(workingTimeRange);

        for (HatchPositionInfo hatchPositionInfo : hatchPositionInfoList) {
            newHatchInfo = new HatchInfo();
            newHatchInfo.setHORIZONTALSTARTPOSITION(hatchPositionInfo.getPOSITION());
            newHatchInfo.setID(hatchPositionInfo.getVHTID());
            newHatchInfo.setLENGTH(hatchPositionInfo.getLENGTH());
            newHatchInfo.setVESSELID(vesselID);
            int count = moveCountQuery.get(hatchPositionInfo.getVHTID()) != null ? moveCountQuery.get(hatchPositionInfo.getVHTID()) : 0;
            newHatchInfo.setMOVECOUNT(count);
            newHatchInfo.setNO(hatchPositionInfo.getVHTID());
            newHatchInfo.setSEQ(hatchPositionInfo.getVHTID());
            newHatchInfo.setWORKINGTIMERANGES(workingTimeRangeList);//工作时间

            hatchInfoList.add(newHatchInfo);
        }
        return hatchInfoList;
    }

    /**
     * 得到舱位置信息，生成船舱信息时要用到，顺便将倍位置信息保存在ImportData全局里面
     * @param voyageInfoList
     * @param vesselStructureInfoList
     * @return
     */
    public
    static List<HatchPositionInfo> getHatchPositionInfoList(List<VoyageInfo> voyageInfoList, List<VesselStructureInfo> vesselStructureInfoList) throws Exception {

        Set<String> hatchSet = new HashSet<String>();   //舱集合

        List<HatchPositionInfo> hatchPositionInfoList = new ArrayList<>();


        Integer startPosition = voyageInfoList.get(0).getSTARTPOSITION();//船头开始位置
        Integer endPosition = voyageInfoList.get(0).getENDPOSITION();
        boolean isPositive = true;
        if ("R".equals(voyageInfoList.get(0).getAnchorDirection())) {
            isPositive = false;
        }

        //计算舱开始相对于船头位置、倍位中心相对于船头位置
        Map<String, Double> hatchPositionMap = new HashMap<>();
        List<String> hatchIdList = new ArrayList<>();
        List<String> bayWeiIdList = new ArrayList<>();
        for (VesselStructureInfo vesselStructureInfo : vesselStructureInfoList) {
            if (!hatchIdList.contains(vesselStructureInfo.getVHTID())) {
                hatchIdList.add(vesselStructureInfo.getVHTID())
                if (vesselStructureInfo.getVHTPOSITION() != null) {
                    Double p = 0.0;
                    if (isPositive) {
                        p = startPosition + vesselStructureInfo.getVHTPOSITION();
                    } else {
                        p = endPosition - vesselStructureInfo.getVHTPOSITION();
                    }
                    hatchPositionMap.put(vesselStructureInfo.getVHTID(), p)
                }
            }
            if (!bayWeiIdList.contains(vesselStructureInfo.getVBYBAYID()))
                bayWeiIdList.add(vesselStructureInfo.getVBYBAYID())
        }//统计倍舱位数和倍位数

        //统计每个舱有多少个倍
        Map<String, Set<String>> hatchBayWeiMap = new HashMap<>()
        for (String hatchId : hatchIdList) {
            Set<String> bayWeiSet = new TreeSet<>()
            for (VesselStructureInfo vesselStructureInfo : vesselStructureInfoList) {
                if (hatchId.equals(vesselStructureInfo.getVHTID())) {
                    bayWeiSet.add(vesselStructureInfo.getVBYBAYID())
                }
            }
            hatchBayWeiMap.put(hatchId, bayWeiSet)
        }

        int length = vesselStructureInfoList.get(0).getLENGTH()//舱长度
        int cabL = vesselStructureInfoList.get(0).getCABLENGTH()   //驾驶室长度
        int cabPosition = vesselStructureInfoList.get(0).getCABPOSITION();//驾驶室在哪个倍位号后面

        boolean isAllHavePosition = true;
        if (hatchPositionMap.size() == 0) {
            isAllHavePosition = false;
        }
        for (Double d : hatchPositionMap.values()) {
            if (d == null) {
                isAllHavePosition = false
            }
        }
        if (!isAllHavePosition) {
            int i = 0;
            String cabBayWei = String.format("%02d", cabPosition);
            String cabHatchId = null;
            Collections.sort(hatchIdList)
            for (int j = 0; j < hatchIdList.size(); j++) {//查找到驾驶室在哪个舱
                List<String> bayWeiList = hatchBayWeiMap.get(hatchIdList.get(j)).toList()
                if (bayWeiList.contains(cabBayWei)) {//取后面一个舱号
                    if (j + 1 <= hatchIdList.size() - 1) {
                        cabHatchId = hatchIdList.get(j + 1)
                    }
                }
                if (bayWeiList.size() == 2) {
                    if (cabPosition == (Integer.valueOf(bayWeiList.get(0)) +
                            Integer.valueOf(bayWeiList.get(1))) / 2) {
                        if (j + 1 <= hatchIdList.size() - 1) {
                            cabHatchId = hatchIdList.get(j + 1)
                        }
                    }
                }
            }
            Double cjj = 1.0//舱间距1米
            Double cabLength = 0.0;
            for (String hatchId : hatchIdList) {
                if (hatchId.equals(cabHatchId)) {//当前舱前面有驾驶室
                    cabLength = cabL + cjj
                }
                Double p = 0.0;
                if (isPositive) {
                    p = startPosition + cabLength + i * (length + cjj);
                } else {
                    p = endPosition - (cabLength + i * (length + cjj));
                }
                hatchPositionMap.put(hatchId, Double.valueOf(df.format(p)));
//假设舱间距为2米，这个数据码头还没回复我是否合理
                i++
            }
        }

        //计算舱绝对位置坐标
//            Map<String, Double> hatchPositionMap = new HashMap<>();

        //计算倍位的中心绝对位置坐标
        Map<String, Double> bayWeiPositionMap = new HashMap<>();
        for (String hatchId : hatchIdList) {
            Set<String> bayWeiSet = hatchBayWeiMap.get(hatchId)
            Double hatchPosition = hatchPositionMap.get(hatchId)//舱的位置
            if (bayWeiSet.size() == 2) {//两个倍位
                Double position1 = hatchPosition + Double.valueOf(length) / 4
                Double position2 = hatchPosition + 3 * Double.valueOf(length) / 4
                Double position3 = hatchPosition + Double.valueOf(length) / 2
                List<String> bayWeiList = bayWeiSet.toList()
                String bayWei1 = bayWeiList.get(0)
                String bayWei2 = bayWeiList.get(1)
                bayWeiPositionMap.put(bayWei1, Double.valueOf(df.format(position1)))
                bayWeiPositionMap.put(bayWei2, Double.valueOf(df.format(position2)))
                bayWei1 = bayWei1.startsWith("0") ? bayWei1.replace("0", "") : bayWei1
                bayWei2 = bayWei2.startsWith("0") ? bayWei2.replace("0", "") : bayWei2
                Integer intBayWei3 = (Integer.valueOf(bayWei1) + Integer.valueOf(bayWei2)) / 2
                String bayWei3 = String.format("%02d", intBayWei3);
                bayWeiPositionMap.put(bayWei3, Double.valueOf(df.format(position3)))
            }
            if (bayWeiSet.size() == 1) {//一个倍位
                Double position = hatchPosition + Double.valueOf(length) / 2
                List<String> bayWeiList = bayWeiSet.toList()
                String bayWei = bayWeiList.get(0)
                bayWeiPositionMap.put(bayWei, Double.valueOf(df.format(position)))
            }
        }
        //结束

        ImportData.bayPositionMap = bayWeiPositionMap;//倍位中心绝对位置坐标，有大倍和小倍

        for (VesselStructureInfo vesselStructureInfo : vesselStructureInfoList) {
            String hatchId = vesselStructureInfo.getVHTID().toString();
            vesselStructureInfo.setVHTPOSITION(hatchPositionMap.get(hatchId))//将舱开始相对于船头位置赋值
            Integer Length = vesselStructureInfo.getLENGTH();
            Double hatchPosition = vesselStructureInfo.getVHTPOSITION()
            String bayWeiId = vesselStructureInfo.getVBYBAYID().toString();
            vesselStructureInfo.setVBYPOSITION(bayWeiPositionMap.get(bayWeiId))//将倍位中心相对于船头位置赋值

            if (!hatchSet.contains(hatchId)) {
                HatchPositionInfo hatchPositionInfo = new HatchPositionInfo();
                hatchPositionInfo.setVHTID(hatchId);
                hatchPositionInfo.setLENGTH(Length);
                hatchPositionInfo.setPOSITION(hatchPosition);
                hatchSet.add(hatchId);
                hatchPositionInfoList.add(hatchPositionInfo);
            }

        }

        //为了查看船舶结构两个坐标是否正确，
//        VesselStructureFrame vesselStructureFrame = new VesselStructureFrame(vesselStructureInfoList);
//        vesselStructureFrame.setVisible(true);
        //结束

        return hatchPositionInfoList;
    }

    /**
     * 得到舱内作业关信息，用来生成cwp算法要用的json串
     * @param preStowageInfoList
     * @return
     */
    private static List<WorkMoveInfo> getWorkMoveInfoList(List<PreStowageData> preStowageDataList) throws Exception {

        Map<String, List<String>> moveOrderRecords = new HashMap<>();

        List<WorkMoveInfo> workMoveInfoList = new ArrayList<WorkMoveInfo>();
        Map<String, Double> bayPositionQuery = ImportData.bayPositionMap//存放每个的绝对中心位置,以便生成作业关信息是查找


        System.out.println("开始生成舱内作业关信息：");
        //将数据放在不同的舱位里
        List<String> hatchIdList = new ArrayList<>()//存放舱位ID
        Map<String, List<PreStowageData>> stringListMap = new HashMap<>()//放在不同的舱位的数据
        for (PreStowageData preStowageData : preStowageDataList) {
            if (!hatchIdList.contains(preStowageData.getVHTID())) {
                hatchIdList.add(preStowageData.getVHTID())
            }
        }
        Collections.sort(hatchIdList)
        println "舱位数：" + hatchIdList.size()
        for (String str : hatchIdList) {//将数据存放在不同舱位里
            List<PreStowageData> dataList1 = new ArrayList<>()
            for (PreStowageData preStowageData : preStowageDataList) {
                if (str.equals(preStowageData.getVHTID())) {
                    dataList1.add(preStowageData)
                }
            }
            stringListMap.put(str, dataList1)
        }
        for (String hatchId : hatchIdList) {//逐舱生成舱内作业关信息
            List<PreStowageData> dataList = stringListMap.get(hatchId)
            List<Integer> orders = new ArrayList<>()//每个舱的作业序列
            for (PreStowageData preStowageData1 : dataList) {
                if (!orders.contains(preStowageData1.getMOVEORDER())) {
                    orders.add(preStowageData1.getMOVEORDER())
                }
            }
            for (Integer order : orders) {//按舱内的序列来生成舱内作业关信息
                WorkMoveInfo workMoveInfo = new WorkMoveInfo()
                List<PreStowageData> moveDataList = new ArrayList<>()
                for (PreStowageData preStowageData2 : dataList) {//将同一序列的数据保存下来
                    if (order == preStowageData2.getMOVEORDER()) {
                        moveDataList.add(preStowageData2)
                    }
                }
//                println "是否取到相同作业序列:"+str+"-"+order+"-"+moveDataList.size()
                if (moveDataList.size() == 2) {//作业序列相同,可能是双箱吊或者双吊具
                    if (moveDataList.get(0).getVRWROWNO().equals(
                            moveDataList.get(1).getVRWROWNO())) {//排号相同，为双箱吊
                        workMoveInfo.setCWPWORKMOVENUM(order)
                        Integer tier = Integer.valueOf(moveDataList.get(0).getVTRTIERNO());
                        String deck = tier >= 50 ? "H" : "D"//甲板上/下
                        workMoveInfo.setDECK(deck)
                        workMoveInfo.setGLOBALPRIORITY(2)
                        workMoveInfo.setHATCH(moveDataList.get(0).getVHTID())
                        workMoveInfo.setMOVETYPE(moveDataList.get(0).getWORKFLOW())
                        workMoveInfo.setLD(moveDataList.get(0).getLDULD())
                        //倍位中心的绝对位置
                        String bayStr0 = moveDataList.get(0).getVBYBAYID()//
                        String bayStr1 = moveDataList.get(1).getVBYBAYID()//
                        Double d = Double.valueOf(df.format((bayPositionQuery.get(bayStr0) + bayPositionQuery.get(bayStr1)) / 2))
                        workMoveInfo.setHORIZONTALPOSITION(d)

                        //舱.作业序列.作业工艺
                        String key = hatchId + "." + order + "." + moveDataList.get(0).getWORKFLOW()
                        String vesselPosition1 = hatchId + "." + moveDataList.get(0).getVBYBAYID() + "." + moveDataList.get(0).getVTRTIERNO() + "." + moveDataList.get(0).getVRWROWNO()
                        String vesselPosition2 = hatchId + "." + moveDataList.get(1).getVBYBAYID() + "." + moveDataList.get(1).getVTRTIERNO() + "." + moveDataList.get(1).getVRWROWNO()
                        List<String> positionList = new ArrayList<>()
                        positionList.add(vesselPosition1)
                        positionList.add(vesselPosition2)
                        moveOrderRecords.put(key, positionList)
                    }
                    if (moveDataList.get(0).getVBYBAYID().equals(
                            moveDataList.get(1).getVBYBAYID())) {//倍位号相同，为双吊具
                        workMoveInfo.setCWPWORKMOVENUM(order)
                        Integer tier = Integer.valueOf(moveDataList.get(0).getVTRTIERNO());
                        String deck = tier >= 50 ? "H" : "D"//甲板上/下
                        workMoveInfo.setDECK(deck)
                        workMoveInfo.setGLOBALPRIORITY(2)
                        workMoveInfo.setHATCH(moveDataList.get(0).getVHTID())
                        workMoveInfo.setMOVETYPE(moveDataList.get(0).getWORKFLOW())
                        workMoveInfo.setLD(moveDataList.get(0).getLDULD())
                        //倍位中心的绝对位置
                        String bayStr = moveDataList.get(0).getVBYBAYID()//
                        Double d = bayPositionQuery.get(bayStr)
                        workMoveInfo.setHORIZONTALPOSITION(d)

                        //舱.作业序列.作业工艺
                        String key = hatchId + "." + order + "." + moveDataList.get(0).getWORKFLOW()
                        String vesselPosition1 = hatchId + "." + moveDataList.get(0).getVBYBAYID() + "." + moveDataList.get(0).getVTRTIERNO() + "." + moveDataList.get(0).getVRWROWNO()
                        String vesselPosition2 = hatchId + "." + moveDataList.get(1).getVBYBAYID() + "." + moveDataList.get(1).getVTRTIERNO() + "." + moveDataList.get(1).getVRWROWNO()
                        List<String> positionList = new ArrayList<>()
                        positionList.add(vesselPosition1)
                        positionList.add(vesselPosition2)
                        moveOrderRecords.put(key, positionList)
                    }
                } else {//单吊具
                    workMoveInfo.setCWPWORKMOVENUM(order)
                    Integer tier = Integer.valueOf(moveDataList.get(0).getVTRTIERNO());
                    String deck = tier >= 50 ? "H" : "D"//甲板上/下
                    workMoveInfo.setDECK(deck)
                    workMoveInfo.setGLOBALPRIORITY(2)
                    workMoveInfo.setHATCH(moveDataList.get(0).getVHTID())
                    workMoveInfo.setMOVETYPE(moveDataList.get(0).getWORKFLOW())
                    workMoveInfo.setLD(moveDataList.get(0).getLDULD())
                    //倍位中心的绝对位置
                    String bayStr0 = moveDataList.get(0).getVBYBAYID()//去掉倍为号前面的0
                    Double d = bayPositionQuery.get(bayStr0)
                    workMoveInfo.setHORIZONTALPOSITION(d)

                    //舱.作业序列.作业工艺
                    String key = hatchId + "." + order + "." + moveDataList.get(0).getWORKFLOW()
                    String vesselPosition1 = hatchId + "." + moveDataList.get(0).getVBYBAYID() + "." + moveDataList.get(0).getVTRTIERNO() + "." + moveDataList.get(0).getVRWROWNO()
                    List<String> positionList = new ArrayList<>()
                    positionList.add(vesselPosition1)
                    moveOrderRecords.put(key, positionList)
                }
                workMoveInfoList.add(workMoveInfo)
            }
        }
        ImportData.moveOrderRecords = moveOrderRecords;
        return workMoveInfoList;
    }
}
