package ru.evgenkov.beckend_yadisk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemImport;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemImportRequest;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemType;
import ru.evgenkov.beckend_yadisk.model.Element;

import java.util.List;

@Component
@Service
public class ValidationService {
    private final ElementService elementService;

    @Autowired
    public ValidationService(ElementService elementService) {
        this.elementService = elementService;
    }

    private boolean isISO8601(String date) {
        return date.matches
                ("^([+-]?\\d{4}(?!\\d{2}\\b))((-?)((0[1-9]|1[0-2])(\\3([12]\\d|0[1-9]|3[01]))?|W([0-4]\\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\\d|[12]\\d{2}|3([0-5]\\d|6[1-6])))([T\\s]((([01]\\d|2[0-3])((:?)[0-5]\\d)?|24:?00)([.,]\\d+(?!:))?)?(\\17[0-5]\\d([.,]\\d+)?)?([zZ]|([+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)?$");
    }

    //todo
    public static boolean isID(String id) {
        return id.matches("элемент" + "^_[1-9]_[1-9]$");
    }

    private boolean isValidId(String id, SystemItemImport systemItemImport,
                              List<SystemItemImport> items) {
        if (!isID(id)) return false;
        int count = 0;
        for (SystemItemImport item : items) {
            String elementId = item.getId();
            if (elementId.equals(id)) count++;
            if (count > 1) return false;
        }
        List<Element> elements = elementService.findAllElements();
        String url = systemItemImport.getUrl();
        String type = systemItemImport.getType();
        for (Element element : elements)
            if (element.getId().equals(id))
                if (!(url.equals(element.getUrl()) && type.equals(element.getType().toString())))
                    return false;
        return true;
    }

    private boolean isValidParent(String parentId, List<SystemItemImport> items) {
        if (!isID(parentId)) return false;
        for (SystemItemImport item : items)
            if (item.getId().equals(parentId))
                return item.getType().equals("FOLDER");
        List<Element> elementsDB = elementService.findAllElements();
        for (Element element : elementsDB)
            if (element.getId().equals(parentId))
                return element.getType() == SystemItemType.FOLDER;
        return false;
    }

    private boolean isSystemItemImport(SystemItemImport systemItemImport,
                                       List<SystemItemImport> items) {
        String id = systemItemImport.getId();
        String url = systemItemImport.getUrl();
        String parentId = systemItemImport.getParentId();
        SystemItemType type = SystemItemType.valueOf(systemItemImport.getType());
        Long size = systemItemImport.getSize();
        //todo
        switch (type) {
            case FILE:
                return (null != id && isValidId(id, systemItemImport, items)) &&
                        (null != url) && (null == parentId || isValidParent(parentId, items)) &&
                        (size > 0);
            case FOLDER:
                return (null != id && isValidId(id, systemItemImport, items)) &&
                        (null == url) && (null == parentId || isValidParent(parentId, items)) &&
                        (null == size);
            default:
                return false;
        }
    }

    @Transactional
    public boolean isSystemItemImportRequest(SystemItemImportRequest request) {
        if (isISO8601(request.getUpdateDate())) {
            List<SystemItemImport> items = request.getItems();
            for (SystemItemImport item : items) {
                if (!isSystemItemImport(item, items)) return false;
            }
            return true;
        }
        return false;
    }
}
