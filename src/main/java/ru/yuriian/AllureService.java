package ru.yuriian;

import com.atlassian.confluence.util.http.HttpRequest;
import com.atlassian.confluence.util.http.HttpResponse;
import com.atlassian.confluence.util.http.HttpRetrievalService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yuriian.dto.TestCaseCustomFieldDto;
import ru.yuriian.dto.TestCaseDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Named
public class AllureService {
    private static final String TESTCASE_URL = "https://allure.tinkoff.ru/api/rs/testcase/%s";
    private static final String CUSTOM_FIELDS_URL = "https://allure.tinkoff.ru/api/rs/testcase/%s/cfv";

    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ2LmtyaXpoYW5vdnNraXkiLCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiQWxsdXJlIFRlc3RPcHMiLCJleHAiOjE2Mzc4MjM0MjEsImlhdCI6MTYzNzc2NTgyMSwiY2xpZW50X2lkIjoiYWxsdXJlLWVlLWdhdGV3YXkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiLCJST0xFX0FVRElUT1IiXSwianRpIjoiYTgxOWMxZWEtOGM2Yi00NjkzLTllYzYtNDEwY2Q2Y2FmNWRiIn0.TRTfHIghO2U8G9uXWcMiBwCCsN6hugX_hcp2nW-PAhCrgDAMppQun68jruoJ8UYThrBnZnWtB-iXqhzITGn5tMF4DdDiSG0HzgtjkBnD5HnLLjne3Bb-eMc8CnCabZEgO-95l9PJc8ixazjnbroAzmfn9u-b74Aj5sYrMkAs8npwN6ve4DUlTLtjtXgEwuRnLGCqaJ_JJFV9f-JHmJo1w_m4kAtR2yHDDODj409u4KRahX71lM3d2UpOxR3Xl04gjDsR-v7dDvTg61KCykh7GR-xrumRL8SeY1u26buUb9LXVHli-dEU897fZSGRgwjPZKA210WQKuNAedsw9VMW8w";

    @ConfluenceImport
    private HttpRetrievalService service;
    private static final Logger log = LoggerFactory.getLogger(AllureService.class);

    @Inject
    public AllureService(HttpRetrievalService service) {
        this.service = service;
    }

    @Nonnull
    public Optional<AllureTestCase> getTestCase(String id) {
        TestCaseDto testCaseDto = getTestCaseById(id);
        List<TestCaseCustomFieldDto> customFieldsDtos = getCustomFieldsById(id);

        Optional result = Optional.empty();
        if (testCaseDto != null && customFieldsDtos != null) {
            result = Optional.of(new AllureTestCase(testCaseDto, customFieldsDtos));
        }
        return result;
    }

    private TestCaseDto getTestCaseById(String id) {
        TestCaseDto result = null;
        try {
            HttpRequest request = getHttpRequest(String.format(TESTCASE_URL, id));
            HttpResponse response = service.get(request);
            log.error("Response code: " + response.getStatusCode());

            if (response.getStatusCode() == 200) {
                Gson gson = new Gson();
                result = gson.fromJson(IOUtils.toString(response.getResponse(), "UTF-8"),
                        TestCaseDto.class);
            }
        } catch (IOException e) {
            log.error("Exception during request to Allure", e);
        }
        return result;
    }

    private List<TestCaseCustomFieldDto> getCustomFieldsById(String id) {
        List<TestCaseCustomFieldDto> result = null;
        try {
            HttpRequest request = getHttpRequest(String.format(CUSTOM_FIELDS_URL, id));
            HttpResponse response = service.get(request);
            log.error("Response code: " + response.getStatusCode());

            if (response.getStatusCode() == 200) {
                Gson gson = new Gson();
                result = gson.fromJson(IOUtils.toString(response.getResponse(), "UTF-8"),
                        new TypeToken<List<TestCaseCustomFieldDto>>(){}.getType());
            }
        } catch (IOException e) {
            log.error("Exception during request to Allure", e);
        }
        return result;
    }

    private HttpRequest getHttpRequest(String url) {
        HttpRequest request = new HttpRequest();
        request.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        request.setUrl(url);
        return request;
    }
}
