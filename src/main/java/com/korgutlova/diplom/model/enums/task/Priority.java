package com.korgutlova.diplom.model.enums.task;

public enum Priority {
    HIGH("Высокий приоритет"),
    MEDIUM("Средний приоритет"),
    LOW("Низкий приоритет");

    private final String name;

    Priority(String name) {
        this.name = name;
    }
}
