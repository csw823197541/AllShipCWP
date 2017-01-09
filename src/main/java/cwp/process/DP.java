package cwp.process;

import cwp.entity.*;

import java.util.List;

/**
 * Created by csw on 2017/1/7 10:43.
 * Explain:
 */
public class DP {

    public DPResult cwpKernel(List<Crane> cranes, List<Hatch> hatches, MoveTime[][] mt) {

        int nc = cranes.size();
        int nh = hatches.size();
        if (nc <= 0 || nh <= 0) {
            return new DPResult();
        }

        DPResult[][] dp = new DPResult[nc][nh];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nh; j++) {
                dp[i][j] = new DPResult();
            }
        }

        if (hatches.get(0).hatchDynamic.mMoveCountDY != 0) {
            dp[0][0].dpMoveCount = hatches.get(0).hatchDynamic.mMoveCountDY - mt[0][0].dpMoveTime;
            dp[0][0].dpDistance = Math.abs(cranes.get(0).craneDynamic.mCurrentPosition - hatches.get(0).hatchDynamic.mCurrentWorkPosition);
            dp[0][0].dpTraceBack.add(new Pair(0, 0, cranes.get(0).getCraneId()));
        } else {
            dp[0][0].dpDistance = 0.0;
        }
        for (int i = 1; i < nc; i++) {
            DPResult cur_dp = new DPResult();
            cur_dp.dpMoveCount = hatches.get(0).hatchDynamic.mMoveCountDY - mt[i][0].dpMoveTime;
            cur_dp.dpDistance = Math.abs(cranes.get(i).craneDynamic.mCurrentPosition - hatches.get(0).hatchDynamic.mCurrentWorkPosition);
//            if (better(cur_dp, dp[i - 1][0])) {
//                dp[i][0] = cur_dp.deepCopy();
//                dp[i][0].dpTraceBack.add(new Pair(i, 0, cranes.get(i).getCraneId()));
//            } else {
//                dp[i][0] = dp[i - 1][0].deepCopy();
//            }
            dp[i][0] = cur_dp.deepCopy();
            dp[i][0].dpTraceBack.add(new Pair(i, 0, cranes.get(i).getCraneId()));
        }
        for (int j = 1; j < nh; j++) {
            DPResult cur_dp = new DPResult();
            cur_dp.dpMoveCount = hatches.get(j).hatchDynamic.mMoveCountDY - mt[0][j].dpMoveTime;
            cur_dp.dpDistance = Math.abs(cranes.get(0).craneDynamic.mCurrentPosition - hatches.get(j).hatchDynamic.mCurrentWorkPosition);
            if (better(cur_dp, dp[0][j - 1])) {
                dp[0][j] = cur_dp.deepCopy();
                dp[0][j].dpTraceBack.add(new Pair(0, j, cranes.get(0).getCraneId()));
            } else {
                dp[0][j] = dp[0][j - 1].deepCopy();
            }
        }

        for (int i = 1; i < nc; i++) {
            for (int j = 1; j < nh; j++) {
                DPResult cur_dp = new DPResult();
                int k = j;
                for (; k >= 0 && hatches.get(j).hatchDynamic.mCurrentWorkPosition -  hatches.get(k).hatchDynamic.mCurrentWorkPosition < 2 * cranes.get(i).getSafeSpan(); k--)
                    ;
                if (k < 0) {
                    cur_dp.dpMoveCount = hatches.get(j).hatchDynamic.mMoveCountDY - mt[i][j].dpMoveTime;
                    cur_dp.dpDistance = Math.abs(cranes.get(i).craneDynamic.mCurrentPosition - hatches.get(j).hatchDynamic.mCurrentWorkPosition);

                } else {
                    cur_dp.dpMoveCount = hatches.get(j).hatchDynamic.mMoveCountDY + dp[i - 1][k].dpMoveCount - mt[i][j].dpMoveTime;
                    cur_dp.dpDistance = Math.abs(cranes.get(i).craneDynamic.mCurrentPosition - hatches.get(j).hatchDynamic.mCurrentWorkPosition) + dp[i - 1][k].dpDistance;
                    cur_dp.dpTraceBack = dp[i - 1][k].dpTraceBack;
                }

                DPResult tmp_dp = new DPResult();

//                if (better(dp[i][j - 1], dp[i - 1][j])) {
//                    tmp_dp = dp[i][j - 1].deepCopy();
//                } else {
//                    tmp_dp = dp[i - 1][j].deepCopy();
//                }
                tmp_dp = dp[i][j - 1].deepCopy();
                if (better(cur_dp, tmp_dp)) {
                    dp[i][j] = cur_dp.deepCopy();
                    if (hatches.get(j).hatchDynamic.mMoveCountDY - mt[i][j].dpMoveTime > 0) {
                        dp[i][j].dpTraceBack.add(new Pair(i, j, cranes.get(i).getCraneId()));
                    }
//                    dp[i][j].dpTraceBack.add(new Pair(i, j, cranes.get(i).getCraneId()));
                } else {
                    dp[i][j] = tmp_dp.deepCopy();
                }
            }
        }

        for (int i = 0; i < nc; i++) {
            String str = "----";
            for (int j = 0; j < nh; j++) {
                str += dp[i][j].dpMoveCount + "-";
            }
            System.out.println(str);
        }

        return dp[nc - 1][nh - 1].deepCopy();
    }

    private boolean better(DPResult cur_dp, DPResult dpResult) {
//        if (cur_dp.dpMoveCount.longValue() > dpResult.dpMoveCount.longValue()) {
//            return true;
//        } else if (cur_dp.dpMoveCount.longValue() == dpResult.dpMoveCount.longValue()) {
//            if (cur_dp.dpDistance.doubleValue() < dpResult.dpDistance.doubleValue()) {
//                return true;
//            } else if (cur_dp.dpDistance.doubleValue() == dpResult.dpDistance.doubleValue()){
//                return cur_dp.dpMoveCount.longValue() > 0;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
        return cur_dp.dpMoveCount.longValue() > dpResult.dpMoveCount.longValue()
                ? true : (cur_dp.dpMoveCount.longValue() == dpResult.dpMoveCount.longValue()
                ? cur_dp.dpDistance.doubleValue() < dpResult.dpDistance.doubleValue() : false);
    }
}
