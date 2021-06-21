package com.korgutlova.diplom.model.enums.simulation;


public enum SimStatus {
    INITIALIZE("Инициализация"),
    IN_PROCESS("В процессе"),
    ON_PAUSE("На паузе"),
    FINISHED("Заверешена");

    private final String name;

    SimStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
