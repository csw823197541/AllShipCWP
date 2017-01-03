package entity;

import java.util.Date;

/**
 * Created by csw on 2016/12/13 12:22.
 * Explain:
 */
public class Voyage {

    private long voyId;
    private String vesselId;
    private Date planStartTime;
    private Date planEndTime;
    private double startPo;
    private double endPo;

    public Voyage() {}

    public Voyage(long voyId, String vesselId, Date planStartTime, Date planEndTime, double startPo, double endPo) {
        this.voyId = voyId;
        this.vesselId = vesselId;
        this.planStartTime = planStartTime;
        this.planEndTime = planEndTime;
        this.startPo = startPo;
        this.endPo = endPo;
    }

    public long getVoyId() {
        return voyId;
    }

    public void setVoyId(long voyId) {
        this.voyId = voyId;
    }

    public String getVesselId() {
        return vesselId;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public double getStartPo() {
        return startPo;
    }

    public void setStartPo(double startPo) {
        this.startPo = startPo;
    }

    public double getEndPo() {
        return endPo;
    }

    public void setEndPo(double endPo) {
        this.endPo = endPo;
    }
}
