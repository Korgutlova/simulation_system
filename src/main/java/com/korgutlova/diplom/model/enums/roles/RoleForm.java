package com.korgutlova.diplom.model.enums.roles;

public enum RoleForm {
    USER("Пользователь"),
    ORGANIZER("Организатор");

    private final String name;

    RoleForm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
