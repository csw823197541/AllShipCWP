package cwp;

import cwp.process.CWP;
import cwp.process.CWP1;
import cwp.process.CWP2;
import cwp.process.CWP3;

/**
 * Created by csw on 2017/1/6 20:25.
 * Explain:
 */
public class CallCWP {

    public static String cwp(String craneJsonStr, String hatchJsonStr, String moveJsonStr) {

        long dyStartTime = System.currentTimeMillis();
        CWP1 cwp = new CWP1();
        cwp.initData(craneJsonStr, hatchJsonStr, moveJsonStr);
        cwp.whoIsKeyHatch();
        cwp.divideCraneMoveRange();
        cwp.cwpSearch(0);
        long dyEndTime = System.currentTimeMillis();
        String str = cwp.writeResult();

        System.out.println("The time of the cwp algorithm is: " + (dyEndTime - dyStartTime) + " ms");
        return str;
    }
}
