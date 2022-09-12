package ru.evgenkov.beckend_yadisk.utility;

import ru.evgenkov.beckend_yadisk.components.shemas.SystemItem;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SystemItemHandler {
    public static void collectFolders(SystemItem systemItem, List<SystemItem> folders) {
        for (SystemItem child : systemItem.getChildren()) {
            if (child.getType().equals(SystemItemType.FOLDER.toString())) {
                folders.add(child);
                collectFolders(child, folders);
            }
        }
    }

    public static Long folderSize(SystemItem folder) {
        Queue<SystemItem> queue = new LinkedList<>();
        queue.add(folder);
        long sum = 0;
        while (!queue.isEmpty()) {
            SystemItem systemItem = queue.poll();
            if (systemItem.getType().equals(SystemItemType.FILE.toString())) {
                sum += systemItem.getSize();
            } else {
                List<SystemItem> children = systemItem.getChildren();
                queue.addAll(children);
            }
        }
        return sum;
    }
}
