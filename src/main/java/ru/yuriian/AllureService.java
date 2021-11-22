package ru.yuriian;

import com.atlassian.confluence.util.http.HttpResponse;
import com.atlassian.confluence.util.http.HttpRetrievalService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Named
public class AllureService {

    @ConfluenceImport
    private HttpRetrievalService service;
    private static final Logger log = LoggerFactory.getLogger(AllureService.class);
    private static final String WIKI_URL_TEMPLATE =
            "https://en.wikipedia.org/w/api.php?format=json&action=query&generator=search&gsrsearch=%s&gsrlimit=1&" +
                    "prop=extracts&exintro&explaintext&exsentences=2";

    @Inject
    public AllureService(HttpRetrievalService service) {
        this.service = service;
    }

    @Nonnull
    public Optional<AllureResponse> searchByText(String searchText) {
        try {
            HttpResponse response = service.get((String.format(WIKI_URL_TEMPLATE, URIUtil.encodeAll(searchText))));
            JsonParser parser = new JsonParser();
            JsonElement body = parser.parse(IOUtils.toString(response.getResponse(), "UTF-8"));
            JsonElement result = getFirstPage(body);
            String snippet = result.getAsJsonObject().getAsJsonPrimitive("extract").getAsString();
            Integer pageId = result.getAsJsonObject().getAsJsonPrimitive("pageid").getAsInt();
            return Optional.of(new AllureResponse(String.format("https://en.wikipedia.org/?curid=%d", pageId), snippet));
        } catch (IOException e){
            log.error("Exception during request to Wikipedia", e);
            return Optional.empty();
        }
    }

    private JsonElement getFirstPage(JsonElement body){
        JsonObject query = Objects.requireNonNull(body.getAsJsonObject().getAsJsonObject("query"), "Response body doesn't contain \"query\" key");
        return query.getAsJsonObject("pages").entrySet().stream().findFirst().get().getValue();
    }
}
