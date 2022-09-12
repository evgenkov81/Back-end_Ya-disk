package ru.evgenkov.beckend_yadisk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItem;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemImportRequest;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemType;
import ru.evgenkov.beckend_yadisk.model.Element;
import ru.evgenkov.beckend_yadisk.service.ElementService;
import ru.evgenkov.beckend_yadisk.service.ValidationService;
import ru.evgenkov.beckend_yadisk.utility.Converter;
import ru.evgenkov.beckend_yadisk.utility.SystemItemHandler;
import ru.evgenkov.beckend_yadisk.components.shemas.Error;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ElementController {
    private final ElementService elementService;

    @Autowired
    public ElementController(ElementService elementService) {
        this.elementService = elementService;
    }

    @PostMapping("/imports")
    public ResponseEntity<Error> addElements(@RequestBody SystemItemImportRequest request) {
        ResponseEntity<Error> response;
        ValidationService validationService = new ValidationService(elementService);
        if (validationService.isSystemItemImportRequest(request)) {
            elementService.saveElement(request);
            response = new ResponseEntity<>(new Error(200, "Success"), HttpStatus.OK);
        } else response = new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteElement(@PathVariable String id) {
        ResponseEntity<Error> response;

        if (ValidationService.isID(id)) {

            Element element = elementService.findElement(id);
            if (null != element) {
                elementService.deleteElement(id);
                response = new ResponseEntity<>
                        (new Error(200, "OK"), HttpStatus.OK);
            } else response = new ResponseEntity<>
                    (new Error(404, "Element not found"), HttpStatus.NOT_FOUND);
        } else response = new ResponseEntity<>
                (new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
        return response;
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<SystemItem> getElement(@PathVariable String id) {
        ResponseEntity<SystemItem> response;
        if (ValidationService.isID(id)) {
            Element element = elementService.findElement(id);
            if (null != element) {

                SystemItem systemItem = Converter.toSystemItem(element);
                String systemItemId = systemItem.getId();
                List<SystemItem> folders = new ArrayList<>();
                SystemItem responseSystemItem = systemItem;
                if (systemItem.getType().equals(SystemItemType.FOLDER.toString())) {
                    SystemItemHandler.collectFolders(systemItem, folders);
                    folders.add(systemItem);
                    for (SystemItem folder : folders)
                        folder.setSize(SystemItemHandler.folderSize(folder));
                    for (SystemItem folder : folders) {
                        if (folder.getId().equals(systemItemId)) {
                            responseSystemItem = folder;
                            break;
                        }
                    }
                }
                response = new ResponseEntity<>(responseSystemItem, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}

