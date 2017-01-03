package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2016/8/24 21:44.
 * Explain:
 */
public class WorkTimeRange implements Serializable {

    private Integer id;
    private Date workStartTime;
    private Date workEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(Date workStartTime) {
        this.workStartTime = workStartTime;
    }

    public Date getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(Date workEndTime) {
        this.workEndTime = workEndTime;
    }
}
