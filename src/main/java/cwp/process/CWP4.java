package cwp.process;

import cwp.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by csw on 2017/1/13 09:37.
 * Explain: process algorithm return result, add time concept, add incoming parameters
 */
public class CWP4 {

    private CwpData cwpData;
    private DPResult dpResult;

    private String hatchId = "";
    private int breakdown = 100;

    private List<HatchParameter> hatchParameterList;

    public CWP4() {
        cwpData = new CwpData();
        dpResult = new DPResult();
        hatchParameterList = new ArrayList<>();
    }

    public void initData(String craneJsonStr, String hatchJsonStr, String moveJsonStr) throws Exception {

        List<Crane> inputCranes = sortCraneByPosition(InitData.initCrane(craneJsonStr));
        int craneSize = inputCranes.size() > 4 ? 5 : inputCranes.size();
        for (int k = 0; k < craneSize; k++) {
            cwpData.cranes.add(inputCranes.get(k));
        }

        cwpData.hatches = sortHatchById(InitData.initHatch(hatchJsonStr));
        cwpData.moves = InitData.initMove(moveJsonStr);

        //init hatch index map
        int i = 0;
        for (Hatch hatch : cwpData.hatches) {
            cwpData.hatchIdxMap.put(hatch.getHatchId(), i++);
        }

        //init hatch's moves
        sortMoveByMoveOrder(cwpData.moves);
        for (Move move : cwpData.moves) {
            cwpData.hatches.get(cwpData.hatchIdxMap.get(move.getHatchId())).mMoves.add(move);
        }

        //init hatchDynamic.mCurrentWorkPosition
        cwpData.hatches.forEach(hatch -> {
            if (hatch.hatchDynamic.mMoveCount != 0) {
                hatch.hatchDynamic.mCurrentWorkPosition = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getHorizontalPosition();
            }
        });

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

        //init cab's position
        cwpData.cabPosition = cwpData.hatchIdxMap.get(cwpData.hatches.get(0).getCabPosition());
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
//        hatchParameterList.add(new HatchParameter("36247", 100000L));
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
        int allMoveCount = 0;
        int realHatchNum = 0;
        for (Hatch hatch : cwpData.hatches) {
            if (hatch.hatchDynamic.mMoveCount > 0) {
                allMoveCount += hatch.hatchDynamic.mMoveCount;
                realHatchNum++;
            }
        }
        int mean = realHatchNum < nc ? allMoveCount / realHatchNum : allMoveCount / nc;
        System.out.println("all: " + allMoveCount + " mean: " + mean);
        int c = 0;
        long tmpMoveCount = 0;
        int amount = cwpData.amount;
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

        for (int i = 0; i < nc; i++) {
            List<Hatch> hatchList = cwpData.craneMoveFromToMap.get(i);
            sortHatchById(hatchList);
            if (hatchList.size() > 0) {
                long left = hatchList.get(0).hatchDynamic.mMoveCountR;
                long right = hatchList.get(hatchList.size() - 1).hatchDynamic.mMoveCountL;
                if (left != hatchList.get(0).hatchDynamic.mMoveCountL.longValue()) {
                    cwpData.cranes.get(i).craneDynamic.mMoveCountL = left;
                }
                if (right != hatchList.get(hatchList.size() - 1).hatchDynamic.mMoveCountR) {
                    cwpData.cranes.get(i).craneDynamic.mMoveCountR = right;
                }
            }
        }
        //log
        String str3 = "divided hatch: ";
        for (Hatch hatch : cwpData.hatches) {
            if (hatch.hatchDynamic.isDividedHatch == 1) {
                str3 += cwpData.hatchIdxMap.get(hatch.getHatchId()) + "  ";
            }
        }
        System.out.println(str3);
    }

    //who is the key hatch
    public void whoIsKeyHatch() {
        List<Hatch> hatchList = cwpData.hatches;
        int twoKeyMoveCount = 0;
        int max = 0;
        int maxJ1 = -1, maxJ2 = -1;
        if (hatchList.size() > 1) {
            for (int j = 0; j < hatchList.size() - 1; j++) {
                twoKeyMoveCount = hatchList.get(j).getMoveCount() + hatchList.get(j + 1).getMoveCount();
                if (twoKeyMoveCount > max) {
                    max = twoKeyMoveCount;
                    maxJ1 = j;
                    maxJ2 = j + 1;
                }
            }
        } else {
            System.out.println("one hatch!!!");
            maxJ1 = maxJ2 = 0;
        }

        if (maxJ1 != -1 && maxJ2 != -1) {
            cwpData.hatches.get(maxJ1).hatchDynamic.isKeyHatch = 1;
            cwpData.hatches.get(maxJ2).hatchDynamic.isKeyHatch = 1;
        }
        //log
        System.out.println("key hatch: " + maxJ1 + "--" + maxJ2 + ", moveCount: " +
                cwpData.hatches.get(maxJ1).getMoveCount() + " + " +
                "" + cwpData.hatches.get(maxJ2).getMoveCount() + " = " + max);
    }

    //change crane move range, when the mMoveCount of the hatch reach the value of mMoveCountL
    private void changeCraneMoveRange(DPResult dpResult) {
        boolean isChange = false;
        int nc = cwpData.cranes.size();
        for (Integer i : cwpData.craneMoveFromToMap.keySet()) {
            List<Hatch> hatchList = cwpData.craneMoveFromToMap.get(i);
            Crane crane = cwpData.cranes.get(i);
            if (hatchList.size() > 0) {
                Hatch hatchLeft = hatchList.get(0);
                Hatch hatchRight = hatchList.get(hatchList.size() - 1);
                if (findSelectHatchIdx(dpResult, i) != -1) {
                    Hatch selectHatch = cwpData.hatches.get(findSelectHatchIdx(dpResult, i));
                    if (selectHatch.getHatchId().equals(hatchLeft.getHatchId()) && hatchLeft.hatchDynamic.mCurrentMoveIdx == crane.craneDynamic.mMoveCountL.intValue()) {
                        if (hatchLeft.hatchDynamic.mMoveCountL > 0) {
                            isChange = true;
                            System.out.println("reach the right mMoveCount: " + hatchLeft.hatchDynamic.mCurrentMoveIdx);
                            crane.craneDynamic.mMoveRangeFrom += 1;
                            crane.craneDynamic.mMoveCountL = -1L;
                            hatchLeft.hatchDynamic.mMoveCountR = 0L;
                        }
                    } else if (selectHatch.getHatchId().equals(hatchRight.getHatchId()) && hatchRight.hatchDynamic.mCurrentMoveIdx == crane.craneDynamic.mMoveCountR.intValue()) {
                        if (hatchRight.hatchDynamic.mMoveCountR > 0) {
                            isChange = true;
                            System.out.println("reach the left mMoveCount: " + hatchRight.hatchDynamic.mCurrentMoveIdx);
                            crane.craneDynamic.mMoveRangeTo -= 1;
                            crane.craneDynamic.mMoveCountR = -1L;
                            hatchRight.hatchDynamic.mMoveCountL = 0L;
                        }
                    } else {
                        //test crane breakdown
                    }
                }

            }
        }
        if (isChange) {//if crane's moveRange had changed
            for (int i = 0; i < nc; i++) {
                Crane crane = cwpData.cranes.get(i);
                cwpData.craneMoveFromToMap.get(i).clear();
                for (int j = crane.craneDynamic.mMoveRangeFrom; j <= crane.craneDynamic.mMoveRangeTo; j++) {
                    cwpData.craneMoveFromToMap.get(i).add(cwpData.hatches.get(j));
                }
            }
        }
    }
    private Integer findSelectHatchIdx(DPResult dpResult, Integer craneIdx) {
        Integer hatchIdx = -1;
        for (int i = 0; i < dpResult.dpTraceBack.size(); i++) {
            if ((int) dpResult.dpTraceBack.get(i).first == craneIdx) {
                hatchIdx = (Integer) dpResult.dpTraceBack.get(i).second;
            }
        }
        return hatchIdx;
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
            if (hatch.hatchDynamic.mMoveCount > 0) {
                if (hatch.hatchDynamic.isKeyHatch == 1) {//is key hatch
                    if (hatch.hatchDynamic.isDividedHatch == 1) {//10000
                        hatch.hatchDynamic.mMoveCountD = cwpData.keyHatchMoveCount + cwpData.dividedHatchMoveCount;
                    } else {//11000
                        hatch.hatchDynamic.mMoveCountD = cwpData.keyHatchMoveCount;
                    }
                } else {
                    if (hatch.hatchDynamic.isDividedHatch == 1) {//is divided hatch
                        hatch.hatchDynamic.mMoveCountD = cwpData.dividedHatchMoveCount;//1000
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

    //keep working in last selected hatch
    private void keepSelectedHatchWork(DPResult dpResultLast) {
        List<Pair> trace_back = dpResultLast.dpTraceBack;
        if (!trace_back.isEmpty()) {
            int nr = trace_back.size();
            for (int t = 0; t < nr; t++) {
                Hatch hatch = cwpData.hatches.get((int) trace_back.get(t).second);
                if (hatch.hatchDynamic.mMoveCount > 0) {//100000, 50000
//                    hatch.hatchDynamic.mMoveCountDY = hatch.hatchDynamic.isKeyHatch == 1
//                            ? cwpData.keepWorkingMoveCount : cwpData.keepWorkingMoveCount / 2;
                    hatch.hatchDynamic.mMoveCountDY = cwpData.keepWorkingMoveCount;
                }
            }
        }
    }

    //change the key hatch's moveCountDY, ensure key road
    private void keepWorkingInKeyHatch() {
        List<Hatch> hatches = cwpData.hatches;
        hatches.forEach(hatch -> {
            if (hatch.hatchDynamic.mMoveCount > 0) {
                if (hatch.hatchDynamic.isKeyHatch == 1) {
                    hatch.hatchDynamic.mMoveCountDY = 100000L;
                }
            }
        });
    }

    //calculate the cost of moveTime that each crane selects every hatch
    private void calculateMoveTime(MoveTime[][] mt, DPResult dpResultLast) {
        for (int i = 0; i < mt.length; i++) {
            for (int j = 0; j < mt[i].length; j++) {
                mt[i][j].dpDistance = Math.abs(cwpData.cranes.get(i).craneDynamic.mCurrentPosition - cwpData.hatches.get(j).hatchDynamic.mCurrentWorkPosition);
                if (j < cwpData.cranes.get(i).craneDynamic.mMoveRangeFrom
                        || j > cwpData.cranes.get(i).craneDynamic.mMoveRangeTo) {
                    if (cwpData.hatches.get(j).hatchDynamic.mMoveCountDY > 0) {
                        mt[i][j].dpMoveTime = cwpData.hatches.get(j).hatchDynamic.mMoveCountDY - 1L;
                    } else {
                        mt[i][j].dpMoveTime = cwpData.hatches.get(j).hatchDynamic.mMoveCountDY;
                    }
//                    mt[i][j].dpMoveTime = cwpData.hatches.get(j).hatchDynamic.mMoveCountDY;
                } else {
                    if (dpResultLast.dpTraceBack.size() > 0) {
                        Hatch hatch = cwpData.hatches.get(j);
                        if (hatch.hatchDynamic.isDividedHatch == 1) {
                            int lastCraneIdx = -1;
                            for (int t = 0; t < dpResultLast.dpTraceBack.size(); t++) {
                                if (j == (int) dpResultLast.dpTraceBack.get(t).second) {
                                    lastCraneIdx = (int) dpResultLast.dpTraceBack.get(t).first;
                                }
                            }
                            if (lastCraneIdx != -1) {
                                if (i != lastCraneIdx) {
                                    if (hatch.hatchDynamic.mMoveCountL.longValue() > 0 && hatch.hatchDynamic.mMoveCountR.longValue() > 0) {
                                        mt[i][j].dpMoveTime = cwpData.hatches.get(j).hatchDynamic.mMoveCountDY;
                                    } else {
                                        mt[i][j].dpMoveTime = 0L;
                                    }
                                }
                            } else {
                                mt[i][j].dpMoveTime = 0L;
                            }
                        } else {
                            mt[i][j].dpMoveTime = 0L;
                        }
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
            if (cwpData.hatches.get(j).hatchDynamic.mMoveCount > 0) {
                isFinish = false;
            }
        }
        int d = 100;
        isFinish = depth > d ? true : isFinish;
        if (isFinish) {
            if (cwpData.cwpBestSolution.getCwpWorkTime() == 0 ||
                    cwpData.cwpCurSolution.getCwpWorkTime() < cwpData.cwpBestSolution.getCwpWorkTime()) {
                cwpData.cwpBestSolution = cwpData.cwpCurSolution.deepCopy();
            }
        }
        int branch_width = depth < cwpData.cwpBranchLimit ? cwpData.cwpBranchWidth : 1;

        List<DPResult> dp_Results = new ArrayList<>();
        DPResult dpResultLast = dpResult.deepCopy();

        //before every dynamic plan, must check the crane's moveRange
        changeCraneMoveRange(dpResultLast);
        //log
        String str2 = "crane move range: ";
        for (Crane crane : cwpData.cranes) {
            str2 += crane.craneDynamic.mMoveRangeFrom + "-" + crane.craneDynamic.mMoveRangeTo + "  ";
        }
        System.out.println(str2);

        //change divided and key hatch's mMoveCountD
        if (hatchParameterList.size() > 0) {
            changeMoveCountDByParameter();
        } else {
            changeMoveCountDByKeyAndDividedHatch();
        }

        //change the hatch's dynamic mMoveCountD
        changeDynamicMoveCount();

        //keep the last select hatch
        keepSelectedHatchWork(dpResultLast);

        //keep working in key hatch
//        keepWorkingInKeyHatch();

        //according to the last choices selected by cranes, calculate the cost of moveTime that each crane selects every hatch
        calculateMoveTime(cwpData.mt, dpResultLast);

        //log
        String str1 = depth + "-dy(moveCount:L:R): ";
        for (Hatch hatch : cwpData.hatches) {
            str1 += hatch.hatchDynamic.mMoveCountDY + "(" + hatch.hatchDynamic.mMoveCount + ":" + hatch.hatchDynamic.mMoveCountL + ":" + hatch.hatchDynamic.mMoveCountR + ")  ";
        }
        System.out.println(str1);

        DP1 dp = new DP1();
        dpResult = dp.cwpKernel(cwpData.cranes, cwpData.hatches, cwpData.mt);

        DPResult dpResultCur = dpResult.deepCopy();

        dp_Results.add(dpResultCur);
        dpResult = dpResultCur.deepCopy();

        branch_width = Math.min(branch_width, dp_Results.get(0).dpTraceBack.size());
        branch_width = depth > d ? 0 : branch_width;

        for (int i = 0; i < branch_width; i++) {
            List<CwpBlock> cwp_block = new ArrayList<>();
            Integer time = cwpRealWork(dp_Results.get(i), cwpData.cwpCurSolution.getCwpWorkTime(),
                    cwpData.cranes, cwpData.hatches, cwp_block);
            cwpData.cwpCurSolution.getCwpResult().add(cwp_block);
            cwpData.cwpCurSolution.setCwpWorkTime(cwpData.cwpCurSolution.getCwpWorkTime() + time);
            cwpSearch(depth + 1);
        }
    }

    private Integer cwpRealWork(DPResult dpResult, Integer start_time, List<Crane> cranes,
                                List<Hatch> hatches, List<CwpBlock> cwp_block) {

        List<Pair> trace_back = dpResult.dpTraceBack;
        if (trace_back.isEmpty()) {
            return 0;
        }
        int nr = trace_back.size();

        //init cwp block & find min work time
        for (int i = 0; i < nr; i++) {
            cwp_block.add(new CwpBlock());
        }
        int min_time = Integer.MAX_VALUE;
        for (int t = 0; t < nr; t++) {
            int craneIdx = (Integer) trace_back.get(t).first;
            Crane crane = cranes.get(craneIdx);
            Hatch hatch = hatches.get((Integer) trace_back.get(t).second);
            if (hatch.hatchDynamic.mMoveCount == 0) {
                cwp_block.get(t).setmTrueBlock(false);
                continue;
            }
            Double last_position = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getHorizontalPosition();
            for (int k = hatch.hatchDynamic.mCurrentMoveIdx; k < hatch.getMoveCount(); k++) {
                if (hatch.getmMoves().get(k).getHorizontalPosition().doubleValue() != last_position.doubleValue()) {
                    break;
                }
                if (hatch.hatchDynamic.mMoveCountR.longValue() == crane.craneDynamic.mMoveCountL.longValue()) {
                    if (k == crane.craneDynamic.mMoveCountL && hatch.hatchDynamic.mMoveCountL.longValue() > 0) {
                        break;
                    }
                }
                if (hatch.hatchDynamic.mMoveCountL.longValue() == crane.craneDynamic.mMoveCountR.longValue()) {
                    if (k == crane.craneDynamic.mMoveCountR && hatch.hatchDynamic.mMoveCountR.longValue() > 0) {
                        break;
                    }
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
            Crane crane = cranes.get((Integer) trace_back.get(t).first);
            Hatch hatch = hatches.get((Integer) trace_back.get(t).second);

            cwpBlock.setmCraneId(crane.getCraneId());
            cwpBlock.setmHatchId(hatch.getHatchId());
            cwpBlock.setmWorkStartTime(start_time); //crane work startTime
            cwpBlock.setmWorkCostTime(0);

            int startT = start_time;
            Move move;
            for (; hatch.hatchDynamic.mCurrentMoveIdx < hatch.getMoveCount(); hatch.hatchDynamic.mCurrentMoveIdx++) {
                int cur_cost = cost(crane, hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx).getMoveType());
                if (cwpBlock.getmWorkCostTime() + cur_cost > min_time) {
                    break;
                }
                cwpBlock.setmWorkCostTime(cwpBlock.getmWorkCostTime() + cur_cost);
                cwpBlock.setmMoveCount(cwpBlock.getmMoveCount() + 1);
                move = hatch.getmMoves().get(hatch.hatchDynamic.mCurrentMoveIdx);
                move.setmWorkStartTime(startT);
                move.setmWorkEndTime(startT + cur_cost);
                //
                double moveDistance = Math.abs(move.getHorizontalPosition() - crane.craneDynamic.mCurrentPosition);
                int moveT = (int) (moveDistance * 60 / crane.getSpeed());

                move.setmRealWorkStartTime(startT);
                cwpBlock.getmMoves().add(move);
                crane.craneDynamic.mCurrentPosition = move.getHorizontalPosition();
//                hatch.hatchDynamic.mCurrentWorkPosition = move.getHorizontalPosition();
                startT = move.getmWorkEndTime();
            }
            cwpBlock.setmWorkEndTime(cwpBlock.getmWorkStartTime() + cwpBlock.getmWorkCostTime());
            cwpBlock.setmVesselId(hatch.getVesselId());
            cwpBlock.setmCraneId(crane.getCraneId());
            cwpBlock.setmHatchId(hatch.getHatchId());

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
        List<CwpBlock> cwpBlockList = new ArrayList<>();
        List<List<CwpBlock>> cwpBlockResult = cwpData.cwpBestSolution.getCwpResult();
        for (List<CwpBlock> cwpBlocks : cwpBlockResult) {
            for (CwpBlock cwpBlock : cwpBlocks) {
                for (Move move : cwpBlock.getmMoves()) {
                    CwpBlock cb = new CwpBlock();
                    cb.setmCraneId(cwpBlock.getmCraneId());
                    cb.setmHatchId(cwpBlock.getmHatchId());
                    cb.setmVesselId(cwpBlock.getmVesselId());
                    cb.setmMoveCount(1);
                    cb.setmCranePosition(move.getHorizontalPosition());
                    cb.setmWorkStartTime(move.getmWorkStartTime());
                    cb.setmWorkEndTime(move.getmWorkEndTime());
                    cb.setmRealWorkStartTime(move.getmRealWorkStartTime());
                    cb.setmLD(move.getLD());
                    cb.setmMoveType(move.getMoveType());
                    cb.setmStartMoveId(move.getMoveOrder());
                    cwpBlockList.add(cb);
                }
            }
        }
        List<List<CwpBlock>> blocks = new ArrayList<>();
        blocks.add(cwpBlockList);
        return WriteResult.write(blocks);
    }

    public CwpData getCwpData() {
        return cwpData;
    }
}
