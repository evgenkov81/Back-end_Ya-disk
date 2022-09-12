package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.Data;

@Data
public class SystemItemImport {
    private final String id;
    private final String url;
    private final String parentId;
    private final String type;
    private final Long size;
}
