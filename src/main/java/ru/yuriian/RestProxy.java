package ru.yuriian;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yuriian.dto.TestCaseDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Path("/testcase")
public class RestProxy {

    private static String ACCESS_TOKEN = null;

    private static final Logger log = LoggerFactory.getLogger(AllureService.class);
    private AllureService allureService;
    private AllureHttpClient httpClient;

    @Inject
    public RestProxy(AllureService service, AllureHttpClient httpClient) {
        allureService = service;
        this.httpClient = httpClient;
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{key}")
    public Response getMessage(@PathParam("key") String key) {
        log.error("========== request key: " + key);
        Optional<AllureTestCase> testCase = allureService.getTestCase(key);

        Response response;
        if (testCase.isPresent()) {
            response = Response.ok(testCase.get().toDto()).build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createTestCase(TestCaseDto testCaseDto) throws IOException {
        log.error("=============== createTestCase(): " + testCaseDto);

        ObjectMapper objectMapper = new ObjectMapper();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://staging.allure.tinkoff.ru/api/rs/testcase/");
            httpPost.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            String payload = objectMapper.writeValueAsString(testCaseDto);
            httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

            CloseableHttpResponse postResponse = httpClient.execute(httpPost);

            Response response;
            if (postResponse.getStatusLine().getStatusCode() == 200) {
                String string = IOUtils.toString(postResponse.getEntity().getContent(), "UTF-8");
                TestCaseDto answer = objectMapper.readValue(string, TestCaseDto.class);
                response = Response.ok(new AllureTestCase(answer, new ArrayList<>()).toDto()).build();
            } else {
                String string = IOUtils.toString(postResponse.getEntity().getContent(), "UTF-8");
                log.error("err answer:" + string);
                response = Response
                        .status(postResponse.getStatusLine().getStatusCode())
                        .build();
            }
            return response;
        }
    }
}
