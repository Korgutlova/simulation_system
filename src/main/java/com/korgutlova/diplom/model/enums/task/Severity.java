package com.korgutlova.diplom.model.enums.task;

public enum Severity {
    CRITICAL("Критичная"),
    MAJOR("Важная"),
    MINOR("Средняя"),
    TRIVIAL("Незначительная");

    private final String name;

    Severity(String name) {
        this.name = name;
    }

}
