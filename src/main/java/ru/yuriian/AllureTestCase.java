package ru.yuriian;

import ru.yuriian.dto.AllureTestCaseDto;
import ru.yuriian.dto.TestCaseCustomFieldDto;
import ru.yuriian.dto.TestCaseDto;

import javax.annotation.Nonnull;
import java.util.List;

public class AllureTestCase {
    private static final String URL_TEMPLATE = "%s/project/%d/test-cases/%d";

    private TestCaseDto testCaseDto;
    private List<TestCaseCustomFieldDto> testCaseCustomFieldDtos;

    public AllureTestCase(TestCaseDto testCaseDto, List<TestCaseCustomFieldDto> testCaseCustomFieldDtos) {
        this.testCaseDto = testCaseDto;
        this.testCaseCustomFieldDtos = testCaseCustomFieldDtos;
    }

    public int getId() {
        return testCaseDto.getId();
    }

    public int getProjectId() {
        return testCaseDto.getProjectId();
    }

    public String getName() {
        return testCaseDto.getName();
    }

    public boolean isAutomated() {
        return testCaseDto.isAutomated();
    }

    public String getAuthor() {
        return testCaseDto.getCreatedBy();
    }

    @Nonnull
    public String getAllureUrl() {
        return String.format(URL_TEMPLATE, AllureConstants.BASE_URL, testCaseDto.getProjectId(), testCaseDto.getId());
    }

    @Nonnull
    public String getCustomFieldsHtml() {
        StringBuilder result = new StringBuilder();
        for (TestCaseCustomFieldDto customFieldDto : testCaseCustomFieldDtos) {
            result
                    .append(customFieldDto.getCustomField().getName())
                    .append(": ")
                    .append(customFieldDto.getName())
                    .append("<br/>");
        }
        return result.toString();
    }

    public AllureTestCaseDto toDto() {
        return new AllureTestCaseDto(testCaseDto, testCaseCustomFieldDtos, getAllureUrl());
    }
}
