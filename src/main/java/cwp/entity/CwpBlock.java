package cwp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2016/8/25 11:49.
 * Explain:
 */
public class CwpBlock implements Serializable {

    private boolean mTrueBlock;
    private String mCraneId;
    private String mHatchId;
    private Integer mWorkStartTime;
    private Integer mWorkEndTime;
    private Integer mWorkCostTime;
    private Integer mMoveCount;
    List<Move> mMoves;

    private Integer mStartMoveId;
    private String mVesselId;
    private String mMoveType;
    private String mLD;
    private Double mCranePosition;
    private String mHatchBayId;
    private Date mWorkST;
    private Date mWorkET;
    private Integer mRealWorkStartTime;



    public CwpBlock() {
        mTrueBlock = true;
        mWorkStartTime = 0;
        mWorkEndTime = 0;
        mWorkCostTime = 0;
        mMoveCount = 0;
        mMoves = new ArrayList<>();
    }

    public boolean ismTrueBlock() {
        return mTrueBlock;
    }

    public void setmTrueBlock(boolean mTrueBlock) {
        this.mTrueBlock = mTrueBlock;
    }

    public String getmCraneId() {
        return mCraneId;
    }

    public void setmCraneId(String mCraneId) {
        this.mCraneId = mCraneId;
    }

    public String getmHatchId() {
        return mHatchId;
    }

    public void setmHatchId(String mHatchId) {
        this.mHatchId = mHatchId;
    }


    public Integer getmMoveCount() {
        return mMoveCount;
    }

    public void setmMoveCount(Integer mMoveCount) {
        this.mMoveCount = mMoveCount;
    }

    public List<Move> getmMoves() {
        return mMoves;
    }

    public void setmMoves(List<Move> mMoves) {
        this.mMoves = mMoves;
    }


    public Integer getmStartMoveId() {
        return mStartMoveId;
    }

    public void setmStartMoveId(Integer mStartMoveId) {
        this.mStartMoveId = mStartMoveId;
    }

    public String getmVesselId() {
        return mVesselId;
    }

    public void setmVesselId(String mVesselId) {
        this.mVesselId = mVesselId;
    }

    public String getmMoveType() {
        return mMoveType;
    }

    public void setmMoveType(String mMoveType) {
        this.mMoveType = mMoveType;
    }

    public String getmLD() {
        return mLD;
    }

    public void setmLD(String mLD) {
        this.mLD = mLD;
    }

    public Double getmCranePosition() {
        return mCranePosition;
    }

    public void setmCranePosition(Double mCranePosition) {
        this.mCranePosition = mCranePosition;
    }

    public String getmHatchBayId() {
        return mHatchBayId;
    }

    public void setmHatchBayId(String mHatchBayId) {
        this.mHatchBayId = mHatchBayId;
    }

    public Date getmWorkST() {
        return mWorkST;
    }

    public void setmWorkST(Date mWorkST) {
        this.mWorkST = mWorkST;
    }

    public Date getmWorkET() {
        return mWorkET;
    }

    public void setmWorkET(Date mWorkET) {
        this.mWorkET = mWorkET;
    }

    public Integer getmRealWorkStartTime() {
        return mRealWorkStartTime;
    }

    public void setmRealWorkStartTime(Integer mRealWorkStartTime) {
        this.mRealWorkStartTime = mRealWorkStartTime;
    }

    public Integer getmWorkStartTime() {
        return mWorkStartTime;
    }

    public void setmWorkStartTime(Integer mWorkStartTime) {
        this.mWorkStartTime = mWorkStartTime;
    }

    public Integer getmWorkEndTime() {
        return mWorkEndTime;
    }

    public void setmWorkEndTime(Integer mWorkEndTime) {
        this.mWorkEndTime = mWorkEndTime;
    }

    public Integer getmWorkCostTime() {
        return mWorkCostTime;
    }

    public void setmWorkCostTime(Integer mWorkCostTime) {
        this.mWorkCostTime = mWorkCostTime;
    }
}
