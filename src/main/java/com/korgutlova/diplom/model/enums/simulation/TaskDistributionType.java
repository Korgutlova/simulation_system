package com.korgutlova.diplom.model.enums.simulation;

public enum TaskDistributionType {
    STANDART("Стандартная", "Выдача произвольных задач от ботов"),
    VARIABLE("Вариативная", "В зависимости от ответов пользователей, " +
            "подбираются задачи разной сложности");

    private final String name;
    private final String description;

    TaskDistributionType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}