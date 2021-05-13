package com.korgutlova.diplom.model.enums.roles;

public enum Role {
    USER("Пользователь"),
    ORGANIZER("Организатор"),
    ADMIN("Администратор"),
    BOT("Бот");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
