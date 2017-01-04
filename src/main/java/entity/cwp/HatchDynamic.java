package entity.cwp;

import java.io.*;

/**
 * Created by csw on 2016/8/25 8:21.
 * Explain:
 */
public class HatchDynamic implements Serializable {

    public Double mCurrentWorkPosition;
    public Integer mMoveCount;
    public Integer mCurrentMoveIdx;

    public Integer mMoveCountL;
    public Integer mMoveCountR;

    public Integer mMoveCountDY;
    public Integer mMoveCountD;

    public Integer isKeyHatch;

    public HatchDynamic() {
        mMoveCount = 0;
        mCurrentMoveIdx = 0;
        mCurrentWorkPosition = 0.0;
        mMoveCountL = 0;
        mMoveCountR = 0;
        mMoveCountDY = 0;
        isKeyHatch = 0;//not key
        mMoveCountD = 0;
    }

    public Double getmCurrentWorkPosition() {
        return mCurrentWorkPosition;
    }

    public void setmCurrentWorkPosition(Double mCurrentWorkPosition) {
        this.mCurrentWorkPosition = mCurrentWorkPosition;
    }

    public Integer getmMoveCount() {
        return mMoveCount;
    }

    public void setmMoveCount(Integer mMoveCount) {
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
