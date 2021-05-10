package com.korgutlova.diplom.model.enums.simulation;


public enum SimStatus {
    INITIALIZE("Инициализация"),
    IN_PROCESS("В процессе"),
    ON_PAUSE("На паузе (сейчаспользователь проходит другую симуляцию)"),
    FINISHED("Заверешена");

    private final String name;

    SimStatus(String name) {
        this.name = name;
    }
}
