package cwp.entity;

import java.io.*;

/**
 * Created by csw on 2017/1/7 16:41.
 * Explain:
 */
public class MoveTime implements Serializable{

    public Long dpMoveCount;
    public Long dpMoveTime;
    public Double dpDistance;

    public MoveTime() {
        dpMoveCount = 0L;
        dpMoveTime = 0L;
        dpDistance = Double.MAX_VALUE;
    }

    public MoveTime deepCopy(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (MoveTime) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
