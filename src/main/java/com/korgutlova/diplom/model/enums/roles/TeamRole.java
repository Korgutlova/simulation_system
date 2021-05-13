package com.korgutlova.diplom.model.enums.roles;

public enum TeamRole {
    ANALYST("Aналитик"),
    PROJECT_MANAGER("Проектный менеджер"),
    TEAMLEAD("Тимлидер"),
    TESTER("Тестировщик");

    private final String name;

    TeamRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
