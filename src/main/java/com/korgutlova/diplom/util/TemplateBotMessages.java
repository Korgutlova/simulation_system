package com.korgutlova.diplom.util;

public final class TemplateBotMessages {
    public final static String CHAT_DESTINATION = "/queue/bot/%s/user/%s";

    public final static String TIME_WRITE_REMINDER = "Привет, за эту неделю было списано только " +
            "%s часов, необходимо списывать %s часов в неделю";

    public final static String OVERDUE_TASK_REMINDER = "Напоминаю, что по этой задаче <a href=\"http://localhost:8080/api/task?id=%s\">%s</a>" +
            " истек срок, необходимо как можно быстрее ее завершить";

    public final static String ISSUE_NEW_TASK = "Новая задача для тебя, держи " +
            "<a href=\"http://localhost:8080/api/task?id=%s\">%s</a>";

    public final static String[] REPLICAS_RIGHT_ANSWERS = new String[]{"Действительно, верно", "Да, так и есть",
            "Верный подход", "Думаю это правильный ответ", "Правильный вариант"};

    public final static String[] REPLICAS_WRONG_ANSWERS = new String[]{"Нет, не так. Правильно: %s", "Ответ другой: %s",
            "Думаю ответ следующий %s", "Нет, думаю что все таки %s", "%s - корректный ответ"};

    public final static String DEFAULT_MESSAGE_DURING_ERRORS = "Перефомулируй вопрос пожалуйста, " +
            "у меня нет информации по этому поводу";
}