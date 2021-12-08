package ru.yuriian;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import ru.yuriian.dto.TestCaseCustomFieldDto;
import ru.yuriian.dto.TestCaseDto;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AllureTestCaseDto {
    private Integer id;
    private Integer projectId;
    private String name;
    private Boolean automated;
    private String createdBy;
    private String url;
    private Map<String, String> customFields;

    public AllureTestCaseDto(TestCaseDto testCaseDto, List<TestCaseCustomFieldDto> testCaseCustomFieldDtos, String url) {
        id = testCaseDto.getId();
        projectId = testCaseDto.getProjectId();
        name = testCaseDto.getName();
        automated = testCaseDto.isAutomated();
        createdBy = testCaseDto.getCreatedBy();
        this.url = url;

        customFields = new TreeMap<>();
        for (TestCaseCustomFieldDto customFieldDto : testCaseCustomFieldDtos) {
            customFields.put(customFieldDto.getCustomField().getName(), customFieldDto.getName());
        }
    }
}
