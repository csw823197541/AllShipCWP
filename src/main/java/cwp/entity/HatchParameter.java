package cwp.entity;

/**
 * Created by csw on 2017/1/8 14:38.
 * Explain:
 */
public class HatchParameter {

    private String hatchId;
    private Long moveCount;

    public HatchParameter(){

    }

    public HatchParameter(String hatchId, Long moveCount) {
        this.hatchId = hatchId;
        this.moveCount = moveCount;
    }

    public String getHatchId() {
        return hatchId;
    }

    public void setHatchId(String hatchId) {
        this.hatchId = hatchId;
    }

    public Long getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(Long moveCount) {
        this.moveCount = moveCount;
    }
}
