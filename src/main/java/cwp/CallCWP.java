package cwp;

import cwp.process.*;

/**
 * Created by csw on 2017/1/6 20:25.
 * Explain:
 */
public class CallCWP {

    public static String cwp(String craneJsonStr, String hatchJsonStr, String moveJsonStr) {

        long dyStartTime = System.currentTimeMillis();
        CWP4 cwp = new CWP4();
        String resultStr = null;
        try {
            cwp.initData(craneJsonStr, hatchJsonStr, moveJsonStr);
            cwp.whoIsKeyHatch();
            cwp.divideCraneMoveRange();
            cwp.cwpSearch(0);
            resultStr = cwp.writeResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long dyEndTime = System.currentTimeMillis();

        System.out.println("The time of the cwp algorithm is: " + (dyEndTime - dyStartTime) + " ms");
        return resultStr;
    }
}
