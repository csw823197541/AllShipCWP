package cwp.entity;

import java.util.*;

/**
 * Created by csw on 2016/8/24 22:35.
 * Explain:
 */
public class CwpData {

    public Integer cwpBranchWidth = 1;
    public Integer cwpBranchLimit = 4;

    public static Long surplusMoveCount = 0L;
    public static Long moveCostTime = 0L;

    public List<Crane> cranes;
    public List<Hatch> hatches;
    public List<Move> moves;
    public CwpSolution cwpCurSolution;
    public CwpSolution cwpBestSolution;

    public Map<String, Integer> hatchIdxMap;
    public Map<String, Integer> craneIdxMap;
    public Map<Integer, Set<Double>> hatchIdxPosMap;

    public Map<Integer, List<Hatch>> craneMoveFromToMap;

    public MoveTime[][] mt;

    public CwpData() {
        cranes = new ArrayList<>();
        hatches = new ArrayList<>();
        moves = new ArrayList<>();
        cwpCurSolution = new CwpSolution();
        cwpBestSolution = new CwpSolution();
        hatchIdxMap = new HashMap<>();
        hatchIdxPosMap = new HashMap<>();
        craneIdxMap = new HashMap<>();
        craneMoveFromToMap = new HashMap<>();
    }
}
