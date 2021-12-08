package ru.yuriian;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yuriian.dto.AccessTokenDto;
import ru.yuriian.dto.TestCaseDto;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named
public class AllureHttpClient {

    private static final Logger log = LoggerFactory.getLogger(AllureService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AccessTokenStorage accessTokenStorage;

    @Inject
    public AllureHttpClient(AccessTokenStorage accessTokenStorage) {
        this.accessTokenStorage = accessTokenStorage;
    }

    public String getAccessToken() throws IOException {
        log.error("===== getAccessToken");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(AllureConstants.BASE_URL + "/api/uaa/oauth/token");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("grant_type", "apitoken"));
            params.add(new BasicNameValuePair("scope", "openid"));
            params.add(new BasicNameValuePair("token", AllureConstants.USER_TOKEN));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse postResponse = httpClient.execute(httpPost);

            String accessToken = null;
            if (postResponse.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                String payload = IOUtils.toString(postResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                AccessTokenDto accessTokenDto = objectMapper.readValue(payload, AccessTokenDto.class);
                accessToken = accessTokenDto.getAccessToken();
            } else {
                String errorBody = IOUtils.toString(postResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                log.error("Error content:" + errorBody);
            }
            accessTokenStorage.setAccessToken(accessToken);
            log.error("===== Created token: " + accessToken);
            return accessToken;
        }
    }

    public Optional<AllureTestCase> createTestCase(String accessToken, TestCaseDto testCaseDto) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(AllureConstants.BASE_URL + "/api/rs/testcase/");
            httpPost.setHeader("Authorization", "Bearer " + accessToken);
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(testCaseDto), ContentType.APPLICATION_JSON));

            CloseableHttpResponse postResponse = httpClient.execute(httpPost);

            Optional<AllureTestCase> result;
            if (postResponse.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                String string = IOUtils.toString(postResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                TestCaseDto createdTestCase = objectMapper.readValue(string, TestCaseDto.class);
                result = Optional.of(new AllureTestCase(createdTestCase, new ArrayList<>()));
            } else {
                String errorBody = IOUtils.toString(postResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                log.error("Error content:" + errorBody);
                result = Optional.empty();
            }
            return result;
        }
    }
}
