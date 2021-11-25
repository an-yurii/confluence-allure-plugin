package ru.yuriian.dto;

public class TestCaseDto {
    private int id;
    private int projectId;
    private String name;
    private boolean automated;
    private String createdBy;
    private String lastModifiedBy;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
}
