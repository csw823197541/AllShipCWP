package entity.cwp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2016/8/24 16:15.
 * Explain:
 */
public class CwpCSH {

    public int dpMoveCount;
    public int dpDistance;
    public List<Pair> dpTraceBack;

    public CwpCSH() {
        dpMoveCount = 0;
        dpDistance = Integer.MAX_VALUE;
        dpTraceBack = new ArrayList<>();
    }

    public int getDpMoveCount() {
        return dpMoveCount;
    }

    public void setDpMoveCount(int dpMoveCount) {
        this.dpMoveCount = dpMoveCount;
    }

    public int getDpDistance() {
        return dpDistance;
    }

    public void setDpDistance(int dpDistance) {
        this.dpDistance = dpDistance;
    }

    public List<Pair> getDpTraceBack() {
        return dpTraceBack;
    }

    public void setDpTraceBack(List<Pair> dpTraceBack) {
        this.dpTraceBack = dpTraceBack;
    }
}


