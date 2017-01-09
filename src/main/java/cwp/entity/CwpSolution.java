package cwp.entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2016/8/25 11:47.
 * Explain:
 */
public class CwpSolution implements Serializable {

    private Integer cwpWorkTime;
    private List<List<CwpBlock>> cwpResult;

    public CwpSolution() {
        cwpWorkTime = 0;
        cwpResult = new ArrayList<>();
    }

    public Integer getCwpWorkTime() {
        return cwpWorkTime;
    }

    public void setCwpWorkTime(Integer cwpWorkTime) {
        this.cwpWorkTime = cwpWorkTime;
    }

    public List<List<CwpBlock>> getCwpResult() {
        return cwpResult;
    }

    public void setCwpResult(List<List<CwpBlock>> cwpResult) {
        this.cwpResult = cwpResult;
    }

    public CwpSolution deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (CwpSolution) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
