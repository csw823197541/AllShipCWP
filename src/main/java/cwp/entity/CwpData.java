package cwp.entity;

import java.util.*;

/**
 * Created by csw on 2016/8/24 22:35.
 * Explain:
 */
public class CwpData {

    public Integer cwpBranchWidth = 1;
    public Integer cwpBranchLimit = 4;
    public Integer cabPosition = -1;

    public Long surplusMoveCount = 0L;
    public Long moveCostTime = 0L;
    public Integer passCabTime = 120;
    public Integer amount = 15;
    public Long keyHatchMoveCount = 100000L;
    public Long dividedHatchMoveCount = 10000L;
    public Long keepWorkingMoveCount = 2000000L;

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
