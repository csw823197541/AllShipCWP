package entity;

import java.util.List;

/**
 * Created by csw on 2016/8/24 15:04.
 * Explain:
 */
public class Crane {

    private double currentPosition;//桥机当前位置坐标信息
    private String craneId;//桥吊唯一编号
    private Integer disEff20;//卸20尺箱子的效率
    private Integer disEff40;//卸40尺箱子的效率
    private Integer disEffTwin;//卸20尺箱子双箱吊的效率
    private Integer disEffTdm;//卸40尺箱子双吊具的效率
    private Integer loadEff20;//装20尺箱子的效率
    private Integer loadEff40;//装40尺箱子的效率
    private Integer loadEffTwin;//装20尺箱子双箱吊的效率
    private Integer loadEffTdm;//装40尺箱子双吊具的效率
    private double moveRangeFrom;//桥机移动范围开始
    private double moveRangeTo;//桥机移动范围结束
    private Integer safeSpan;//桥吊安全距离
    private Integer craneSeq;//桥吊顺序号
    private Integer speed;//桥吊移动速度
    private Integer width;//桥吊宽度
    private List<WorkTimeRange> workTimeRanges;//桥吊工作时间范围

    public double getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(double currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCraneId() {
        return craneId;
    }

    public void setCraneId(String craneId) {
        this.craneId = craneId;
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

    public double getMoveRangeFrom() {
        return moveRangeFrom;
    }

    public void setMoveRangeFrom(double moveRangeFrom) {
        this.moveRangeFrom = moveRangeFrom;
    }

    public double getMoveRangeTo() {
        return moveRangeTo;
    }

    public void setMoveRangeTo(double moveRangeTo) {
        this.moveRangeTo = moveRangeTo;
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
