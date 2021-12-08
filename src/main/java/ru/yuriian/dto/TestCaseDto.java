package ru.yuriian.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseDto {
    private Integer id;
    private Integer projectId;
    private String name;
    private Boolean automated;
    private String createdBy;
    private String lastModifiedBy;
    private List<LinkDto> links;

    public Integer getId() {
        return id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public Boolean isAutomated() {
        return automated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "TestCaseDto: id = " + id + ", name = " + name + ", projectId = " + projectId;
    }
}
