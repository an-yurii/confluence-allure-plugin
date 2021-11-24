package ru.yuriian;

public class AllureTestCase {
    private static final String URL_TEMPLATE = "https://allure.tinkoff.ru/project/%d/test-cases/%d";

    private int id;
    private int projectId;
    private String name;
    private boolean automated;

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public boolean isAutomated() {
        return automated;
    }

    public String getAllureUrl() {
        return String.format(URL_TEMPLATE, projectId, id);
    }
}
