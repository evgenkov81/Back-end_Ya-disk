package ru.evgenkov.beckend_yadisk.components.shemas;

import lombok.Data;

@Data
public class Error {
    private final int code;
    private final String message;
}
