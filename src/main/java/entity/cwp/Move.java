package entity.cwp;

import java.io.Serializable;

/**
 * Created by csw on 2016/8/24 15:04.
 * Explain:
 */
public class Move implements Serializable {

    private Integer moveOrder;
    private String deck;
    private Integer globalPriority;
    private String hatchId;
    private Double horizontalPosition;
    private String LD;
    private String moveType;

    public Integer getMoveOrder() {
        return moveOrder;
    }

    public void setMoveOrder(Integer moveOrder) {
        this.moveOrder = moveOrder;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public Integer getGlobalPriority() {
        return globalPriority;
    }

    public void setGlobalPriority(Integer globalPriority) {
        this.globalPriority = globalPriority;
    }

    public String getHatchId() {
        return hatchId;
    }

    public void setHatchId(String hatchId) {
        this.hatchId = hatchId;
    }

    public Double getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(Double horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public String getLD() {
        return LD;
    }

    public void setLD(String LD) {
        this.LD = LD;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }
}
