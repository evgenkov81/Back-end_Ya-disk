package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.Data;


public class SystemItemHistoryUnit {
    private String id;
    private String name;
    private String parentId;
    private String type;
    private Long size;
    private String date;
}
