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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Named
public class AllureService {
    private static final String TESTCASE_URL = "%s/api/rs/testcase/%s";
    private static final String CUSTOM_FIELDS_URL = "%s/api/rs/testcase/%s/cfv";

    private static final Logger log = LoggerFactory.getLogger(AllureService.class);

    @ConfluenceImport
    private HttpRetrievalService service;
    private AllureHttpClient allureHttpClient;
    private AccessTokenStorage accessTokenStorage;

    @Inject
    public AllureService(HttpRetrievalService service, AllureHttpClient allureHttpClient, AccessTokenStorage accessTokenStorage) {
        this.service = service;
        this.allureHttpClient = allureHttpClient;
        this.accessTokenStorage = accessTokenStorage;
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
            HttpRequest request = getHttpRequest(String.format(TESTCASE_URL, AllureConstants.BASE_URL, id));
            HttpResponse response = service.get(request);
            log.error("Response code: " + response.getStatusCode());

            if (response.getStatusCode() == 200) {
                Gson gson = new Gson();
                result = gson.fromJson(IOUtils.toString(response.getResponse(), StandardCharsets.UTF_8),
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
            HttpRequest request = getHttpRequest(String.format(CUSTOM_FIELDS_URL, AllureConstants.BASE_URL, id));
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

    private HttpRequest getHttpRequest(String url) throws IOException {
        HttpRequest request = new HttpRequest();
        request.setUrl(url);

        log.error("---------- ACCESS_TOKEN_STORAGE Id = " + accessTokenStorage.toString());
        String accessToken = accessTokenStorage.getAccessToken();
        if (accessToken == null) {
            accessToken = allureHttpClient.getAccessToken();
        }
        request.setHeader("Authorization", "Bearer " + accessToken);
        return request;
    }
}
