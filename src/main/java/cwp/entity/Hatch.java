package cwp.entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2016/8/24 15:04.
 * Explain:
 */
public class Hatch implements Serializable {

    private Double horizontalStartPosition;
    private String hatchId;
    private String vesselId;
    private Integer length;
    private Integer moveCount;
    private String hatchNo;
    private String hatchSeq;
    private List<WorkTimeRange> workTimeRanges;

    public HatchDynamic hatchDynamic;
    public List<Move> mMoves;

    public Hatch() {
        hatchDynamic = new HatchDynamic();
        mMoves = new ArrayList<>();
    }

    public List<Move> getmMoves() {
        return mMoves;
    }

    public void setmMoves(List<Move> mMoves) {
        this.mMoves = mMoves;
    }

    public HatchDynamic getHatchDynamic() {
        return hatchDynamic;
    }

    public void setHatchDynamic(HatchDynamic hatchDynamic) {
        this.hatchDynamic = hatchDynamic;
    }

    public Double getHorizontalStartPosition() {
        return horizontalStartPosition;
    }

    public void setHorizontalStartPosition(Double horizontalStartPosition) {
        this.horizontalStartPosition = horizontalStartPosition;
    }

    public String getHatchId() {
        return hatchId;
    }

    public void setHatchId(String hatchId) {
        this.hatchId = hatchId;
    }

    public String getVesselId() {
        return vesselId;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(Integer moveCount) {
        this.moveCount = moveCount;
    }

    public String getHatchNo() {
        return hatchNo;
    }

    public void setHatchNo(String hatchNo) {
        this.hatchNo = hatchNo;
    }

    public String getHatchSeq() {
        return hatchSeq;
    }

    public void setHatchSeq(String hatchSeq) {
        this.hatchSeq = hatchSeq;
    }

    public List<WorkTimeRange> getWorkTimeRanges() {
        return workTimeRanges;
    }

    public void setWorkTimeRanges(List<WorkTimeRange> workTimeRanges) {
        this.workTimeRanges = workTimeRanges;
    }

    public Hatch deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (Hatch) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
