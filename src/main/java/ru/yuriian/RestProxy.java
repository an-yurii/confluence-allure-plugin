package ru.yuriian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/testcase")
public class RestProxy {

    private static final Logger log = LoggerFactory.getLogger(AllureService.class);
    private AllureService allureService;

    @Inject
    public RestProxy(AllureService service) {
        allureService = service;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{key}")
    public Response getMessage(@PathParam("key") String key) {
        log.error("========== request key: " + key);
        Optional<AllureTestCase> testCase = allureService.getTestCase(key);

        Response response;
        if (testCase.isPresent()) {
            response = Response.ok(testCase.get()).build();
        } else {
            response = Response.serverError().build();
        }
        return response;
    }
}
