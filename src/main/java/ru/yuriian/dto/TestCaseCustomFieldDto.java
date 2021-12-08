package ru.yuriian.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseCustomFieldDto {
    private String name;
    private CustomFieldDto customField;

    public String getName() {
        return name;
    }

    public CustomFieldDto getCustomField() {
        return customField;
    }
}
