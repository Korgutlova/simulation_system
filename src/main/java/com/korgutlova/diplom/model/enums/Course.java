package com.korgutlova.diplom.model.enums;

public enum Course {
    FIRST_COURSE_BAC("1 курс (бакалавр)"),
    SECOND_COURSE_BAC("2 курс (бакалавр)"),
    THIRD_COURSE_BAC("3 курс (бакалавр)"),
    FOURTH_COURSE_BAC("4 курс (бакалавр)"),
    FIRST_COURSE_MAG("1 курс (магистр)"),
    SECOND_COURSE_MAG("2 курс (магистр)"),
    FIRST_COURSE_ASP("1 курс (аспирант)"),
    SECOND_COURSE_ASP("2 курс (аспирант)"),
    THIRD_COURSE_ASP("3 курс (аспирант)"),
    FOURTH_COURSE_ASP("4 курс (аспирант)");

    String description;

    Course(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
