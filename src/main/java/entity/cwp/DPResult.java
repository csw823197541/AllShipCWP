package entity.cwp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2016/8/24 16:15.
 * Explain:
 */
public class DPResult implements Serializable {

    public Long dpMoveCount;
    public Double dpDistance;
    public List<Pair> dpTraceBack;

    public DPResult() {
        dpMoveCount = 0L;
        dpDistance = Double.MAX_VALUE;
        dpTraceBack = new ArrayList<>();
    }

    public Long getDpMoveCount() {
        return dpMoveCount;
    }

    public void setDpMoveCount(Long dpMoveCount) {
        this.dpMoveCount = dpMoveCount;
    }

    public Double getDpDistance() {
        return dpDistance;
    }

    public void setDpDistance(Double dpDistance) {
        this.dpDistance = dpDistance;
    }

    public List<Pair> getDpTraceBack() {
        return dpTraceBack;
    }

    public void setDpTraceBack(List<Pair> dpTraceBack) {
        this.dpTraceBack = dpTraceBack;
    }

    public DPResult deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (DPResult) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}


