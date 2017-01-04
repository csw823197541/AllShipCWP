package entity.cwp;

import java.io.*;

/**
 * Created by csw on 2016/8/25 8:23.
 * Explain:
 */
public class CraneDynamic implements Serializable {

    public Double mCurrentPosition;
    public Integer mCurrentWorkST;

    public Integer mMoveRangeFrom;
    public Integer mMoveRangeTo;

    public CraneDynamic() {
        mCurrentWorkST = 0;
    }

    public Integer getmCurrentWorkST() {
        return mCurrentWorkST;
    }

    public void setmCurrentWorkST(Integer mCurrentWorkST) {
        this.mCurrentWorkST = mCurrentWorkST;
    }

    public Double getmCurrentPosition() {
        return mCurrentPosition;
    }

    public void setmCurrentPosition(Double mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }

    public CraneDynamic deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (CraneDynamic) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
