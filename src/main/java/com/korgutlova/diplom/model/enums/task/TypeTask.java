package com.korgutlova.diplom.model.enums.task;

public enum TypeTask {
    BUG("Баг"),
    TASK("Задача"),
    TEST("Тестирование"),
    ARCH("Архитектурная задача");

    private final String name;

    TypeTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
