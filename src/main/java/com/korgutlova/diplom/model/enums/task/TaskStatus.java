package com.korgutlova.diplom.model.enums.task;

public enum TaskStatus {
    TO_DO("К выполнению"),
    IN_PROGRESS("В процессе"),
    REVIEW("На ревью"),
    QA("На тестировании"),
    DONE("Сделана"),
    CLOSED("Закрыта"),
    CANCELLED("Отменена");

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
