package datamodel;

import importDataInfo.CraneInfo;
import importDataInfo.VoyageInfo;
import listener.TableEvent;
import listener.TableListener;

import java.util.*;

/**
 * Created by csw on 2016/12/14 11:30.
 * Explain:
 */
public class GlobalImportData {

    public static Integer ratio = 1;

    public static Integer width = 1000;
    public static Integer height = 600;

    public static Integer reWidth = 2500;
    public static Integer reHeight = 1000;

    public static Map<Integer, VoyageInfo> voyageMap = new HashMap<>();
    public static Map<Integer, CraneInfo> craneInfoMap = new HashMap<>();

    private Set<TableListener> listeners;

    public void addListener(TableListener tableListener) {
        if (listeners == null)
            return;
        listeners.add(tableListener);
    }

    private void notifyListeners(TableEvent event) {
        for (TableListener listener : listeners) {
            listener.changeGlobalData(event);
        }
    }
}
