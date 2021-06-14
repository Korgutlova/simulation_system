package com.korgutlova.diplom.util;

public final class TemplateBotMessages {
    public final static String CHAT_DESTINATION = "/queue/bot/%s/user/%s";

    public final static String TIME_WRITE_REMINDER = "Привет, за эту неделю было списано только " +
            "%s часов, необходимо списывать %s часов в неделю";

    public final static String OVERDUE_TASK_REMINDER = "Напоминаю, что по этой задаче <a href=\"http://localhost:8080/api/task?id=%s\">%s</a>" +
            "истек срок, необходимо как можно быстрее ее завершить";
}