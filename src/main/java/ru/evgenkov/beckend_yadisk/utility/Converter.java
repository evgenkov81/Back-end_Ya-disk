package ru.evgenkov.beckend_yadisk.utility;

import ru.evgenkov.beckend_yadisk.components.shemas.SystemItem;
import ru.evgenkov.beckend_yadisk.model.Element;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static SystemItem toSystemItem(Element element) {
        String id = element.getId();
        String url = element.getUrl();
        String date = element.getDate().toString() + ":00.000Z";
        String parentId = null;
        if (null != element.getParent()) parentId = element.getParent().getId();
        String type = element.getType().toString();
        Long size = element.getSize();
        List<SystemItem> children = null;
        if (!element.getChildren().isEmpty()) children =
                Converter.toSystemItemList(element.getChildren());
        return new SystemItem(id, url, date, parentId, type, size, children);
    }

    public static List<SystemItem> toSystemItemList(List<Element> elements) {
        List<SystemItem> systemItemList = new ArrayList<>();
        for (Element element : elements)
            systemItemList.add(Converter.toSystemItem(element));
        return systemItemList;
    }
}
