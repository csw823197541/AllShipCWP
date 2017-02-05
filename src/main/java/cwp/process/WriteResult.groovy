package cwp.process

import cwp.entity.CwpBlock
import groovy.json.JsonBuilder

/**
 * Created by csw on 2016/8/26 16:28.
 * Explain: 
 */
public class WriteResult {

    public static String write(List<List<CwpBlock>> cwpResult) {
        boolean isError = false;
        String result = null;

        List<CwpBlock> cwpBlockList = new ArrayList<>();
        for (List<CwpBlock> cwpBlocks : cwpResult) {
            cwpBlockList.addAll(cwpBlocks)
        }

        if (cwpBlockList != null) {
            try {
                List<Map<String, Object>> list = new ArrayList<>()
                assert cwpBlockList instanceof List
                cwpBlockList.each { it ->
                    if (it.mMoveCount != 0) {
                        Map<String, Object> map = new HashMap<String, Object>()
                        map.put("VESSELD", it.mVesselId)
                        map.put("CRANEID", it.mCraneId)
                        map.put("HATCHID", it.mHatchId)
                        map.put("HATCHBWID", it.mHatchBayId)
                        map.put("WORKINGSTARTTIME", it.mWorkStartTime)
                        map.put("WORKINGENDTIME", it.mWorkEndTime)
                        map.put("MOVECOUNT", it.mMoveCount)
                        map.put("QDC", 1)
                        map.put("MoveID", it.mStartMoveId)
                        map.put("EndMoveID", it.mStartMoveId)
                        map.put("REALWORKINGSTARTTIME", it.mWorkStartTime)
                        map.put("MOVETYPE", it.mMoveType)
                        map.put("mLD", it.mLD)
                        map.put("CranesPosition", it.mCranePosition)
                        list.add(map)
                    }
                }
                def builder = new JsonBuilder(list)
                result = builder.toString()

            } catch (Exception e) {
                System.out.println("Creating cwpResult, data exception!")
                isError = true;
                e.printStackTrace()
            }
        } else {
            System.out.println("CwpResult information is null.")
        }
        if (isError) {
            System.out.println("create failure!")
            return null;
        } else {
            System.out.println("CwpResult create success!")
            return result
        }
    }

    public static String write1(List<List<CwpBlock>> cwpResult) {
        boolean isError = false;
        String result = null;

        List<CwpBlock> cwpBlockList = new ArrayList<>();
        for (List<CwpBlock> cwpBlocks : cwpResult) {
            cwpBlockList.addAll(cwpBlocks)
        }

        if (cwpBlockList != null) {
            try {
                List<Map<String, Object>> list = new ArrayList<>()
                assert cwpBlockList instanceof List
                cwpBlockList.each { it ->
                    if (it.mMoveCount != 0) {
                        Map<String, Object> map = new HashMap<String, Object>()
                        map.put("VESSELD", it.mVesselId)
                        map.put("CRANEID", it.mCraneId)
                        map.put("HATCHID", it.mHatchId)
                        map.put("HATCHBWID", it.mHatchBayId)
                        map.put("WORKINGSTARTTIME", it.mWorkStartTime)
                        map.put("WORKINGENDTIME", it.mWorkEndTime)
                        map.put("MOVECOUNT", it.mMoveCount)
                        map.put("QDC", 1)
                        map.put("MoveID", it.mStartMoveId)
                        map.put("EndMoveID", it.mStartMoveId + it.mMoveCount - 1)
                        map.put("REALWORKINGSTARTTIME", it.mWorkStartTime)
                        map.put("MOVETYPE", it.mMoveType)
                        map.put("mLD", it.mLD)
                        map.put("CranesPosition", it.mCranePosition)
                        list.add(map)
                    }
                }
                def builder = new JsonBuilder(list)
                result = builder.toString()

            } catch (Exception e) {
                System.out.println("Creating cwpResult, data exception!")
                isError = true;
                e.printStackTrace()
            }
        } else {
            System.out.println("CwpResult information is null.")
        }
        if (isError) {
            System.out.println("create failure!")
            return null;
        } else {
            System.out.println("CwpResult create success! the result is: " + result)
            return result
        }
    }
}
