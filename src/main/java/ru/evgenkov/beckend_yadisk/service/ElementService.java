package ru.evgenkov.beckend_yadisk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemImport;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemImportRequest;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemType;
import ru.evgenkov.beckend_yadisk.model.Element;
import ru.evgenkov.beckend_yadisk.repositories.ElementRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Service
public class ElementService {
    private final ElementRepository repository;

    @Autowired
    public ElementService(ElementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveElement(SystemItemImportRequest request) {
        List<SystemItemImport> items = request.getItems();
        LocalDateTime date = getDate(request.getUpdateDate());
        List<Element> elementList = new ArrayList<>();
        List<String> parentIds = new ArrayList<>();
        for (SystemItemImport item : items) {
            SystemItemType type;
            if (item.getType().equals("FILE")) type = SystemItemType.FILE;
            else type = SystemItemType.FOLDER;
            elementList.add(new Element(item.getId(), item.getUrl(),
                    date, type, item.getSize()));
            parentIds.add(item.getParentId());
            for (int i = 0; i < elementList.size(); i++) {
                Element element = elementList.get(i);
                String parentId = parentIds.get(i);
                if (null == parentId) element.setParent(null);
                else {
                    boolean found = false;
                    for (int j = 0; j < parentIds.size(); j++) {
                        Element potentialParent = elementList.get(j);
                        if (potentialParent.getId().equals(parentId)) {
                            element.setParent(potentialParent);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Optional<Element> potentialParent = repository.findById(parentId);
                        if (potentialParent.isPresent()) element.setParent(potentialParent.get());
                        else element.setParent(null);
                    }
                }
                elementList.set(i, element);
            }
        }
        if (elementList.size() != 0) updateDate(elementList.get(0));
        repository.saveAll(elementList);
    }

    @Transactional
    public Element findElement(String id) {
        Optional<Element> element = repository.findById(id);
        return element.orElse(null);
    }

    @Transactional
    public List<Element> findAllElements() {
        return repository.findAll();
    }

    @Transactional
    public void deleteElement(String id) {
        repository.deleteById(id);
    }

    private void updateDate(Element element) {
        Element parent = element.getParent();
        if (null != parent) {
            parent.setDate(element.getDate());
            updateDate(parent);
        }
    }

    private LocalDateTime getDate(String strDate) {
        return LocalDateTime.parse(strDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH));
    }
}
