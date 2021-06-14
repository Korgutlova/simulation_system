package com.korgutlova.diplom.model.enums.simulation;

public enum CommunicationType {
    WITHOUT_COMM("Без общения", ""),
    WITH_COMM("С общением (вопросами)", ""),
    WITH_COMM_AND_REMINDERS("С вопросами и напоминанием", "");

    private final String name;
    private final String description;

    CommunicationType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
}
