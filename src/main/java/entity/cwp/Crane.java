package entity.cwp;

import java.util.List;

/**
 * Created by csw on 2016/8/24 15:04.
 * Explain:
 */
public class Crane {

    private Integer currentPosition;
    private Integer disEff20;
    private Integer disEff40;
    private Integer disEffTwin;
    private Integer disEffTdm;
    private String craneId;
    private Integer loadEff20;
    private Integer loadEff40;
    private Integer loadEffTwin;
    private Integer loadEffTdm;
    private Integer moveRangeFrom;
    private Integer moveRangeTo;
    private String craneName;
    private Integer safeSpan;
    private Integer craneSeq;
    private Integer speed;
    private Integer width;
    private List<WorkTimeRange> workTimeRanges;

    public CraneDynamic craneDynamic;

    public Crane() {
        craneDynamic = new CraneDynamic();
    }

    public CraneDynamic getCraneDynamic() {
        return craneDynamic;
    }

    public void setCraneDynamic(CraneDynamic craneDynamic) {
        this.craneDynamic = craneDynamic;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Integer getDisEff20() {
        return disEff20;
    }

    public void setDisEff20(Integer disEff20) {
        this.disEff20 = disEff20;
    }

    public Integer getDisEff40() {
        return disEff40;
    }

    public void setDisEff40(Integer disEff40) {
        this.disEff40 = disEff40;
    }

    public Integer getDisEffTwin() {
        return disEffTwin;
    }

    public void setDisEffTwin(Integer disEffTwin) {
        this.disEffTwin = disEffTwin;
    }

    public Integer getDisEffTdm() {
        return disEffTdm;
    }

    public void setDisEffTdm(Integer disEffTdm) {
        this.disEffTdm = disEffTdm;
    }

    public String getCraneId() {
        return craneId;
    }

    public void setCraneId(String craneId) {
        this.craneId = craneId;
    }

    public Integer getLoadEff20() {
        return loadEff20;
    }

    public void setLoadEff20(Integer loadEff20) {
        this.loadEff20 = loadEff20;
    }

    public Integer getLoadEff40() {
        return loadEff40;
    }

    public void setLoadEff40(Integer loadEff40) {
        this.loadEff40 = loadEff40;
    }

    public Integer getLoadEffTwin() {
        return loadEffTwin;
    }

    public void setLoadEffTwin(Integer loadEffTwin) {
        this.loadEffTwin = loadEffTwin;
    }

    public Integer getLoadEffTdm() {
        return loadEffTdm;
    }

    public void setLoadEffTdm(Integer loadEffTdm) {
        this.loadEffTdm = loadEffTdm;
    }

    public Integer getMoveRangeFrom() {
        return moveRangeFrom;
    }

    public void setMoveRangeFrom(Integer moveRangeFrom) {
        this.moveRangeFrom = moveRangeFrom;
    }

    public Integer getMoveRangeTo() {
        return moveRangeTo;
    }

    public void setMoveRangeTo(Integer moveRangeTo) {
        this.moveRangeTo = moveRangeTo;
    }

    public String getCraneName() {
        return craneName;
    }

    public void setCraneName(String craneName) {
        this.craneName = craneName;
    }

    public Integer getSafeSpan() {
        return safeSpan;
    }

    public void setSafeSpan(Integer safeSpan) {
        this.safeSpan = safeSpan;
    }

    public Integer getCraneSeq() {
        return craneSeq;
    }

    public void setCraneSeq(Integer craneSeq) {
        this.craneSeq = craneSeq;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public List<WorkTimeRange> getWorkTimeRanges() {
        return workTimeRanges;
    }

    public void setWorkTimeRanges(List<WorkTimeRange> workTimeRanges) {
        this.workTimeRanges = workTimeRanges;
    }
}
