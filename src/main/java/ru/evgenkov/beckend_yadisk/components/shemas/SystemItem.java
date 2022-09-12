package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data
@AllArgsConstructor

public class SystemItem {

        private final String id;
        private final String name;
        private final String date;
        private final String parentId;
        private final String type;
        private Long size;
        private final List<SystemItem> children;
    }

