package ru.yuriian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yuriian.dto.TestCaseDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@Path("/testcase")
public class PluginRestService {

    private static final Logger log = LoggerFactory.getLogger(AllureService.class);
    private AllureService allureService;

    @Inject
    public PluginRestService(AllureService service) {
        allureService = service;
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

        Optional<AllureTestCase> testCase = allureService.createTestCase(testCaseDto);
        Response response;
        if (testCase.isPresent()) {
            response = Response.ok(testCase.get().toDto()).build();
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
    }
}
