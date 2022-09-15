package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data


public class SystemItem {

        private final String id;
        private final String name;
        private final String date;
        private final String parentId;
        private final String type;
        private Long size;
        private final List<SystemItem> children;

        public SystemItem(String id, String name, String date, String parentId, String type, Long size, List<SystemItem> children) {
                this.id = id;
                this.name = name;
                this.date = date;
                this.parentId = parentId;
                this.type = type;
                this.size = size;
                this.children = children;
        }
}

