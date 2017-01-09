package cwp.entity;

import java.io.*;

/**
 * Created by csw on 2016/8/25 8:21.
 * Explain:
 */
public class HatchDynamic implements Serializable {

    public Double mCurrentWorkPosition;
    public Long mMoveCount;
    public Integer mCurrentMoveIdx;

    public Long mMoveCountL;
    public Long mMoveCountR;

    public Long mMoveCountDY;
    public Long mMoveCountD;

    public Integer mMoveCountD1;
    public Integer isDividedHatch;

    public Integer isKeyHatch;

    public HatchDynamic() {
        mMoveCount = 0l;
        mCurrentMoveIdx = 0;
        mCurrentWorkPosition = 0.0;
        mMoveCountL = 0l;
        mMoveCountR = 0l;
        mMoveCountDY = 0l;
        isKeyHatch = 0;//not key
        mMoveCountD = 0l;
        mMoveCountD1 = 0;
        isDividedHatch = 0;//not divided hatch
    }

    public Double getmCurrentWorkPosition() {
        return mCurrentWorkPosition;
    }

    public void setmCurrentWorkPosition(Double mCurrentWorkPosition) {
        this.mCurrentWorkPosition = mCurrentWorkPosition;
    }

    public Long getmMoveCount() {
        return mMoveCount;
    }

    public void setmMoveCount(Long mMoveCount) {
        this.mMoveCount = mMoveCount;
    }

    public Integer getmCurrentMoveIdx() {
        return mCurrentMoveIdx;
    }

    public void setmCurrentMoveIdx(Integer mCurrentMoveIdx) {
        this.mCurrentMoveIdx = mCurrentMoveIdx;
    }

    public HatchDynamic deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (HatchDynamic) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
