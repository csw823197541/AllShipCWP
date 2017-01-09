package cwp.process;

import cwp.entity.*;

import java.util.*;

/**
 * Created by csw on 2017/1/7 10:43.
 * Explain:
 */
public class CWP {

    private CwpData cwpData;
    private DPResult dpResult;

    private List<HatchParameter> hatchParameterList;

    public CWP() {
        cwpData = new CwpData();
        dpResult = new DPResult();
        hatchParameterList = new ArrayList<>();
    }

    public void initData(String craneJsonStr, String hatchJsonStr, String moveJsonStr) {

        List<Crane> inputCranes = sortCraneByPosition(InitData.initCrane(craneJsonStr));
        int craneSize = inputCranes.size() > 4 ? 5 : inputCranes.size();
        for (int k = 0; k < craneSize; k++) {
            cwpData.cranes.add(inputCranes.get(k));
        }

        cwpData.hatches = sortHatchById(InitData.initHatch(hatchJsonStr));
        cwpData.moves = InitData.initMove(moveJsonStr);

        int i = 0;
        for (Hatch hatch : cwpData.hatches) {
            cwpData.hatchIdxMap.put(hatch.getHatchId(), i++);
        }

        sortMoveByMoveOrder(cwpData.moves);
        for (Move move : cwpData.moves) {
            cwpData.hatches.get(cwpData.hatchIdxMap.get(move.getHatchId())).mMoves.add(move);
        }

        //init hatchDynamic.mCurrentWorkPosition
        for (Hatch hatch : cwpData.hatches) {
            if (hatch.hatchDynamic.mMoveCount != 0) {
                hatch.hatchDynamic.mCurrentWorkPosition = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getHorizontalPosition();
            }
        }

        //init crane move in hatch from where to where
        for (int c = 0; c < cwpData.cranes.size(); c++) {
            cwpData.craneMoveFromToMap.put(c, new ArrayList<Hatch>());
            cwpData.craneIdxMap.put(cwpData.cranes.get(c).getCraneId(), c);
        }

        //init the moveTime that each crane selects every hatch
        int nc = cwpData.cranes.size();
        int nh = cwpData.hatches.size();
        cwpData.mt = new MoveTime[nc][nh];
        for (i = 0; i < nc; i++) {
            for (int j = 0; j < nh; j++) {
                cwpData.mt[i][j] = new MoveTime();
            }
        }

        //init hatch parameter
//        testParam();
    }

    //test
    private void testParam() {
        hatchParameterList.add(new HatchParameter("23219", 10000L));
        hatchParameterList.add(new HatchParameter("23209", 9000L));
        hatchParameterList.add(new HatchParameter("23208", 8000L));
        hatchParameterList.add(new HatchParameter("23207", 7000L));
        hatchParameterList.add(new HatchParameter("23206", 6000L));
        hatchParameterList.add(new HatchParameter("23205", 5000L));
        hatchParameterList.add(new HatchParameter("23204", 4000L));
        hatchParameterList.add(new HatchParameter("23203", 3000L));
        hatchParameterList.add(new HatchParameter("23202", 2000L));
        hatchParameterList.add(new HatchParameter("23201", 1000L));
    }

    private List<Hatch> sortHatchById(List<Hatch> hatches) {
        Collections.sort(hatches, new Comparator<Hatch>() {
            @Override
            public int compare(Hatch o1, Hatch o2) {
//                return o1.getHatchId().compareTo(o2.getHatchId());
                return o1.getHorizontalStartPosition().compareTo(o2.getHorizontalStartPosition());
            }
        });
        return hatches;
    }

    private List<Crane> sortCraneByPosition(List<Crane> cranes) {
        Collections.sort(cranes, new Comparator<Crane>() {
            @Override
            public int compare(Crane o1, Crane o2) {
                return o1.craneDynamic.mCurrentPosition > o2.craneDynamic.mCurrentPosition ? 1 :
                        (o1.craneDynamic.mCurrentPosition == o2.craneDynamic.mCurrentPosition ? 0 : -1);
            }
        });
        return cranes;
    }

    private List<Move> sortMoveByMoveOrder(List<Move> moves) {
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o1.getMoveOrder().compareTo(o2.getMoveOrder());
            }
        });
        return moves;
    }

    //divide the crane move range
    public void divideCraneMoveRange() {
        int nc = cwpData.cranes.size();
        int nh = cwpData.hatches.size();
        if (nc <= 0 || nh <= 0) {
            return;
        }

        int mean = 0;
        int allMoveCount = 0;
        int realHatchNum = 0;
        for (Hatch hatch : cwpData.hatches) {
            if (hatch.hatchDynamic.mMoveCount > 0) {
                allMoveCount += hatch.hatchDynamic.mMoveCount;
                realHatchNum++;
            }
        }
        if (realHatchNum < nc) {
            mean = allMoveCount / realHatchNum;
        } else {
            mean = allMoveCount / nc;
        }
        System.out.println("all: " + allMoveCount + " mean: " + mean);
        int c = 0;
        long tmpMoveCount = 0;
        int amount = 0;
        for (int j = 0; j < nh; j++) {
            Hatch hatch = cwpData.hatches.get(j);
            tmpMoveCount += hatch.hatchDynamic.mMoveCount;
            int meanL = mean - amount;
            int meanR = mean + amount;
            c = c == nc ? nc - 1 : c;
            if (tmpMoveCount >= meanL && tmpMoveCount <= meanR) {
                cwpData.craneMoveFromToMap.get(c).add(hatch);
                Crane crane = cwpData.cranes.get(c);
                crane.craneDynamic.mMoveRangeTo = j;
                int cSize = cwpData.craneMoveFromToMap.get(c).size();
                crane.craneDynamic.mMoveRangeFrom = j + 1 - cSize;
                c++;
                tmpMoveCount = 0;
            } else if (tmpMoveCount > meanR) {
                hatch.hatchDynamic.mMoveCountL = hatch.hatchDynamic.mMoveCount - (tmpMoveCount - mean);
                hatch.hatchDynamic.mMoveCountR = tmpMoveCount - mean;
                hatch.hatchDynamic.isDividedHatch = 1;
                Crane crane = cwpData.cranes.get(c);
                if (c == nc - 1) {//current crane is the last one, maybe the hatch is also the last one, so should be done by the last crane
                    hatch.hatchDynamic.mMoveCountL = hatch.hatchDynamic.mMoveCount;
                    hatch.hatchDynamic.mMoveCountR = hatch.hatchDynamic.mMoveCount;
                    hatch.hatchDynamic.isDividedHatch = 0;
                    cwpData.craneMoveFromToMap.get(c).add(hatch);
                    crane.craneDynamic.mMoveRangeTo = j;
                    int cSize = cwpData.craneMoveFromToMap.get(c).size();
                    crane.craneDynamic.mMoveRangeFrom = j + 1 - cSize;
                } else {//the next crane do it, the current crane also can do it
                    //the current crane also can do it
                    cwpData.craneMoveFromToMap.get(c).add(hatch);
                    crane.craneDynamic.mMoveRangeTo = j;
                    int cSize = cwpData.craneMoveFromToMap.get(c).size();
                    crane.craneDynamic.mMoveRangeFrom = j + 1 - cSize;
                    //the next crane do it
                    cwpData.craneMoveFromToMap.get(c + 1).add(hatch);
                    crane.craneDynamic.mMoveRangeTo = j;
                    int ccSize = cwpData.craneMoveFromToMap.get(c).size();
                    crane.craneDynamic.mMoveRangeFrom = j + 1 - ccSize;
                }
                c++;
                tmpMoveCount = hatch.hatchDynamic.mMoveCountR;
            } else {
                cwpData.craneMoveFromToMap.get(c).add(hatch);
                Crane crane = cwpData.cranes.get(c);
                crane.craneDynamic.mMoveRangeTo = j;
                int cSize = cwpData.craneMoveFromToMap.get(c).size();
                crane.craneDynamic.mMoveRangeFrom = j + 1 - cSize;
            }
        }
        //log
        String str2 = "crane move range: ";
        for (Crane crane : cwpData.cranes) {
            str2 += crane.craneDynamic.mMoveRangeFrom + "-" + crane.craneDynamic.mMoveRangeTo + "  ";
        }
        System.out.println(str2);
        String str3 = "divided hatch: ";
        for (Hatch hatch : cwpData.hatches) {
            if (hatch.hatchDynamic.isDividedHatch == 1) {
                str3 += cwpData.hatchIdxMap.get(hatch.getHatchId()) + "  ";
            }
        }
        System.out.println(str3);
        //log
    }

    //who is the key hatch
    public void whoIsKeyHatch() {
        List<Hatch> hatchList = cwpData.hatches;
        int twoKeyMoveCount = 0;
        int max = 0;
        int maxJ1 = -1, maxJ2 = -1;
        for (int j = 0; j < hatchList.size() - 1; j++) {
            twoKeyMoveCount = hatchList.get(j).getMoveCount() + hatchList.get(j + 1).getMoveCount();
            if (twoKeyMoveCount > max) {
                max = twoKeyMoveCount;
                maxJ1 = j;
                maxJ2 = j + 1;
            }
        }
        if (maxJ1 != -1 && maxJ2 != -1) {
            cwpData.hatches.get(maxJ1).hatchDynamic.isKeyHatch = 1;
            cwpData.hatches.get(maxJ2).hatchDynamic.isKeyHatch = 1;
        }
        //log
        System.out.println("key hatch: " + maxJ1 + "--" + maxJ2 + ", moveCount: " + max);
    }

    //
    private void changeMoveCountDByParameter() {
        for (HatchParameter hatchParameter : hatchParameterList) {
            int j = cwpData.hatchIdxMap.get(hatchParameter.getHatchId());
            cwpData.hatches.get(j).hatchDynamic.mMoveCountD = hatchParameter.getMoveCount();
        }
    }

    //
    private void changeMoveCountDByKeyAndDividedHatch() {
        List<Hatch> hatches = cwpData.hatches;
        //change the key hatch's moveCountD
        for (int j = 0; j < hatches.size(); j++) {
            Hatch hatch = hatches.get(j);
            if (hatch.hatchDynamic.isKeyHatch == 1) {//is key hatch
                if (hatch.hatchDynamic.mMoveCount > 0) {
                    if (hatch.hatchDynamic.mMoveCountL < hatch.getMoveCount()) {
                        hatch.hatchDynamic.mMoveCountD = 11000L;
                    } else {
                        hatch.hatchDynamic.mMoveCountD = 10000L;
                    }
                }
            } else {
                if (hatch.hatchDynamic.mMoveCountL < hatch.getMoveCount()) {//is divided hatch
                    if (hatch.hatchDynamic.mMoveCount > 0) {
                        hatch.hatchDynamic.mMoveCountD = 1000L;
                    }
                }
            }
        }
    }

    private void notChangeMoveCountDByKeyAndDividedHatch() {
        List<Hatch> hatches = cwpData.hatches;
        //change the key hatch's moveCountD
        for (int j = 0; j < hatches.size(); j++) {
            Hatch hatch = hatches.get(j);
            if (hatch.hatchDynamic.isKeyHatch == 1) {//is key hatch
                if (hatch.hatchDynamic.mMoveCount > 0) {
                    if (hatch.hatchDynamic.mMoveCountL < hatch.getMoveCount()) {
                        hatch.hatchDynamic.mMoveCountD = 10000L;
                    } else {
                        hatch.hatchDynamic.mMoveCountD = hatch.hatchDynamic.mMoveCount;
                    }
                }
            } else {
                if (hatch.hatchDynamic.mMoveCountL < hatch.getMoveCount()) {//is divided hatch
                    if (hatch.hatchDynamic.mMoveCount > 0) {
                        hatch.hatchDynamic.mMoveCountD = hatch.hatchDynamic.mMoveCount;
                    }
                }
            }
        }
    }

    //change the hatch's dynamic mMoveCountD to mMoveCountDY, before call dynamic algorithm
    private void changeDynamicMoveCount() {
        List<Hatch> hatches = cwpData.hatches;
        for (int j = 0; j < hatches.size(); j++) {
            Hatch hatch = hatches.get(j);
            if (hatch.hatchDynamic.mMoveCount != 0) {
                if (j == 0 && j + 1 < hatches.size()) {//the first hatch and the after hatch is not null
                    hatch.hatchDynamic.mMoveCountDY = 2 * hatch.hatchDynamic.mMoveCountD + hatches.get(j + 1).hatchDynamic.mMoveCountD;
                } else if (j == hatches.size() - 1 && j - 1 > 0) {//the last hatch and the before hatch is not null
                    hatch.hatchDynamic.mMoveCountDY = 2 * hatch.hatchDynamic.mMoveCountD + hatches.get(j - 1).hatchDynamic.mMoveCountD;
                } else if (j + 1 < hatches.size() && j > 0) {//
                    hatch.hatchDynamic.mMoveCountDY = hatches.get(j - 1).hatchDynamic.mMoveCountD + 2 * hatch.hatchDynamic.mMoveCountD + hatches.get(j + 1).hatchDynamic.mMoveCountD;
                } else {// one hatch
                    System.out.println("one hatch!!!!!!!");
                    hatch.hatchDynamic.mMoveCountDY = 2 * hatch.hatchDynamic.mMoveCountD;
                }
            }
        }
    }

    private void keepSelectedHatchWork(DPResult dpResultLast) {
        List<Pair> trace_back = dpResultLast.dpTraceBack;
        if (!trace_back.isEmpty()) {
            int nr = trace_back.size();
            for (int t = 0; t < nr; t++) {
                Hatch hatch = cwpData.hatches.get((int) trace_back.get(t).second);
                if (hatch.hatchDynamic.mMoveCount > 0) {
                    hatch.hatchDynamic.mMoveCountDY = 100000L;
                }
            }
        }
    }

    //calculate the cost of moveTime that each crane selects every hatch
    private void calculateMoveTime(MoveTime[][] mt, DPResult dpResultLast) {
        if (dpResultLast.dpTraceBack.size() > 0) {
            for (int i = 0; i < mt.length; i++) {
                for (int j = 0; j < mt[i].length; j++) {
                    mt[i][j].dpDistance = Math.abs(cwpData.cranes.get(i).craneDynamic.mCurrentPosition - cwpData.hatches.get(j).hatchDynamic.mCurrentWorkPosition);
                    if (j < cwpData.cranes.get(i).craneDynamic.mMoveRangeFrom
                            || j > cwpData.cranes.get(i).craneDynamic.mMoveRangeTo) {
                        mt[i][j].dpMoveTime = cwpData.hatches.get(j).hatchDynamic.mMoveCountDY;
                    } else {
                        mt[i][j].dpMoveTime = 0L;
                    }
                }
            }
        }
    }

    public void cwpSearch(int depth) {
        boolean isFinish = true;
        for (int j = 0; j < cwpData.hatches.size(); j++) {
            if (cwpData.hatches.get(j).hatchDynamic.mMoveCount != 0) {
                isFinish = false;
            }
        }
        int d = 10;
        if (depth > d) {
            isFinish = true;
        }
        if (isFinish) {
            if (cwpData.cwpBestSolution.getCwpWorkTime() == 0 ||
                    cwpData.cwpCurSolution.getCwpWorkTime() < cwpData.cwpBestSolution.getCwpWorkTime()) {
                cwpData.cwpBestSolution = cwpData.cwpCurSolution.deepCopy();
            }
        }
        int branch_width = 1;
        if (depth < cwpData.cwpBranchLimit) {
            branch_width = cwpData.cwpBranchWidth;
        }
        List<DPResult> dp_Results = new ArrayList<>();

        //change divided and key hatch's mMoveCountD
        if (hatchParameterList.size() > 0) {
            changeMoveCountDByParameter();
        } else {
            changeMoveCountDByKeyAndDividedHatch();
        }

        //change the hatch's dynamic mMoveCountD
        changeDynamicMoveCount();

        DPResult dpResultLast = dpResult.deepCopy();

        //keep the last select hatch
        keepSelectedHatchWork(dpResultLast);

        //according to the last choices selected by cranes,
        //calculate the cost of moveTime that each crane selects every hatch
        calculateMoveTime(cwpData.mt, dpResultLast);

        //log
        String str1 = "1-dy(moveCount:L:R): ";
        for (Hatch hatch : cwpData.hatches) {
            str1 += hatch.hatchDynamic.mMoveCountDY + "(" + hatch.hatchDynamic.mMoveCount + ":" + hatch.hatchDynamic.mMoveCountL + ":" + hatch.hatchDynamic.mMoveCountR + ")  ";
        }
        System.out.println(str1);
        //log

        DP dp = new DP();
        dpResult = dp.cwpKernel(cwpData.cranes, cwpData.hatches, cwpData.mt);

        DPResult dpResultCur = dpResult.deepCopy();

        //evaluateCurDPResult(dpResultCur);
//        if (dpResultCur.dpTraceBack.size() < cwpData.cranes.size()) {
//            changeMoveCountDByKeyAndDividedHatch();
//            changeDynamicMoveCount();
//            calculateMoveTime(cwpData.mt, dpResultLast);
//            //log
//            String str2 = "2-dy(moveCount:L:R): ";
//            for (Hatch hatch : cwpData.hatches) {
//                str2 += hatch.hatchDynamic.mMoveCountDY + "(" + hatch.hatchDynamic.mMoveCount + ":" + hatch.hatchDynamic.mMoveCountL + ":" + hatch.hatchDynamic.mMoveCountR + ")  ";
//            }
//            System.out.println(str2);
//            //log
//            dpResult = dp.cwpKernel(cwpData.cranes, cwpData.hatches, cwpData.mt);
//            dpResultCur = dpResult.deepCopy();
//
//            if (dpResultCur.dpTraceBack.size() < cwpData.cranes.size()) {
//                notChangeMoveCountDByKeyAndDividedHatch();
//                changeDynamicMoveCount();
//                calculateMoveTime(cwpData.mt, dpResultLast);
//                //log
//                String str3 = "3-dy(moveCount:L:R): ";
//                for (Hatch hatch : cwpData.hatches) {
//                    str3 += hatch.hatchDynamic.mMoveCountDY + "(" + hatch.hatchDynamic.mMoveCount + ":" + hatch.hatchDynamic.mMoveCountL + ":" + hatch.hatchDynamic.mMoveCountR + ")  ";
//                }
//                System.out.println(str3);
//                //log
//                dpResult = dp.cwpKernel(cwpData.cranes, cwpData.hatches, cwpData.mt);
//                dpResultCur = dpResult.deepCopy();
//            }
//        }

        dp_Results.add(dpResultCur);
        dpResult = dpResultCur.deepCopy();

        branch_width = Math.min(branch_width, dp_Results.get(0).dpTraceBack.size());
        if (depth > d) {
            branch_width = 0;
        }
        for (int i = 0; i < branch_width; i++) {
            List<CwpBlock> cwp_block = new ArrayList<>();
            Integer time = cwpRealWork(dp_Results.get(i), cwpData.cwpCurSolution.getCwpWorkTime(),
                    cwpData.cranes, cwpData.hatches, cwp_block);
            cwpData.cwpCurSolution.getCwpResult().add(cwp_block);//?
            cwpData.cwpCurSolution.setCwpWorkTime(cwpData.cwpCurSolution.getCwpWorkTime() + time);
            cwpSearch(depth + 1);
        }
    }

    private Integer cwpRealWork(DPResult dpResult, Integer start_time, List<Crane> cranes,
                                List<Hatch> hatches, List<CwpBlock> cwp_block) {

        List<Pair> trace_back = dpResult.dpTraceBack;
        if (trace_back.isEmpty()) {
            //get out method
            return 0;
        }
        int nr = trace_back.size();

        //init cwp block & find min work time
        for (int i = 0; i < nr; i++) {
            cwp_block.add(new CwpBlock());
        }
        int min_time = Integer.MAX_VALUE;
        for (int t = 0; t < nr; t++) {
//            int craneIdx = cwpData.craneIdxMap.get(trace_back.get(t).realCraneId);
//            Crane crane = cranes.get(craneIdx);
            Crane crane = cranes.get((Integer) trace_back.get(t).first);
            Hatch hatch = hatches.get((Integer) trace_back.get(t).second);

            cwp_block.get(t).setmCraneId(crane.getCraneId());
            cwp_block.get(t).setmHatchId(hatch.getHatchId());
            cwp_block.get(t).setmWorkStartTime(start_time); //crane work startTime

            if (hatch.hatchDynamic.mMoveCount == 0) {
                cwp_block.get(t).setmTrueBlock(false);
                continue;
            }
            Double last_position = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).
                    getHorizontalPosition();
            for (int k = hatch.hatchDynamic.mCurrentMoveIdx; k < hatch.getMoveCount(); k++) {
                if (hatch.getmMoves().get(k).getHorizontalPosition().doubleValue() != last_position.doubleValue()) {
                    break;
                }
                cwp_block.get(t).setmWorkCostTime(cwp_block.get(t).getmWorkCostTime() + cost(crane, hatch.getmMoves().get(k).getMoveType()));
            }

            min_time = Math.min(min_time, cwp_block.get(t).getmWorkCostTime());
        }

        //work
        for (int t = 0; t < nr; t++) {
            CwpBlock cwpBlock = cwp_block.get(t);

            if (!cwpBlock.ismTrueBlock()) {
                continue;
            }
//            int craneIdx = cwpData.craneIdxMap.get(trace_back.get(t).realCraneId);
//            Crane crane = cranes.get(craneIdx);
            Crane crane = cranes.get((Integer) trace_back.get(t).first);
            Hatch hatch = hatches.get((Integer) trace_back.get(t).second);
            crane.craneDynamic.mCurrentPosition = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getHorizontalPosition();

            Move move = null;
            cwpBlock.setmWorkCostTime(0);
            for (; hatch.hatchDynamic.mCurrentMoveIdx < hatch.getMoveCount(); hatch.hatchDynamic.mCurrentMoveIdx++) {
                int cur_cost = cost(crane, hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getMoveType());
                if (cwpBlock.getmWorkCostTime() + cur_cost > min_time) {
                    break;
                }
                cwpBlock.setmWorkCostTime(cwpBlock.getmWorkCostTime() + cur_cost);
                cwpBlock.setmMoveCount(cwpBlock.getmMoveCount() + 1);
                cwpBlock.getmMoves().add(hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx));
                move = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx);
            }
            cwpBlock.setmWorkEndTime(cwpBlock.getmWorkStartTime() + cwpBlock.getmWorkCostTime());

            cwpBlock.setmVesselId(hatch.getVesselId());
            cwpBlock.setmCraneId(crane.getCraneId());
            cwpBlock.setmHatchId(hatch.getHatchId());
            String moveType = move.getMoveType();
            String bayId = "";
            int j = (int) trace_back.get(t).second;
            if ("2".equals(moveType) || "3".equals(moveType)) {
                bayId = String.valueOf((j + 1) * 4 - 2);
            } else {
                if (move.getHorizontalPosition().doubleValue() - hatch.getHorizontalStartPosition().doubleValue() == Double.valueOf(hatch.getLength()) / 4) {
                    bayId = String.valueOf((j + 1) * 4 - 3);
                } else if (move.getHorizontalPosition().doubleValue() - hatch.getHorizontalStartPosition().doubleValue() == Double.valueOf(hatch.getLength()) * 3 / 4) {
                    bayId = String.valueOf((j + 1) * 4 - 1);
                } else {
                    bayId = String.valueOf((j + 1) * 4 - 2);
                }
            }
            cwpBlock.setmHatchBayId(bayId);
            cwpBlock.setmStartMoveId(move.getMoveOrder() - cwpBlock.getmMoveCount() + 1);
            cwpBlock.setmRealWorkStartTime(start_time);//
            cwpBlock.setmMoveType(moveType);
            cwpBlock.setmLD(move.getLD());
            cwpBlock.setmCranePosition(move.getHorizontalPosition());

            hatch.hatchDynamic.mMoveCount = hatch.getMoveCount() - hatch.hatchDynamic.mCurrentMoveIdx.longValue();
            hatch.hatchDynamic.mMoveCountDY = hatch.hatchDynamic.mMoveCount;
            hatch.hatchDynamic.mMoveCountD = hatch.hatchDynamic.mMoveCount;
            if (hatch.hatchDynamic.mMoveCount != 0) {
                hatch.hatchDynamic.mCurrentWorkPosition = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getHorizontalPosition();
            }
        }
        return min_time;
    }

    private Integer cost(Crane crane, String moveType) {
        return 3600 / 32;
    }

    public String writeResult() {
        return WriteResult.write1(cwpData.cwpBestSolution.getCwpResult());
    }

    public CwpData getCwpData() {
        return cwpData;
    }
}
