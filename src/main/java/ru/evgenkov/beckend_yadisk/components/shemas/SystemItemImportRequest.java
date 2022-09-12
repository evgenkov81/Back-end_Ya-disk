package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.Data;
import java.util.List;
@Data
public class SystemItemImportRequest {
        private final List<SystemItemImport> items;
        private final String updateDate;
}
