package ru.yuriian;

import com.atlassian.cache.Cache;
import com.atlassian.cache.CacheManager;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.Optional;

@Named
public class AllureMacro implements Macro {

    private Cache<String, Optional<AllureTestCase>> cache;

    @Inject
    public AllureMacro(AllureService service, @ConfluenceImport CacheManager cacheManager) {
        cache = cacheManager.getCache("allure-macro", service::getTestCaseById);
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map context = ImmutableMap.builder().put("testcase", cache.get(map.get("search"))
                        .orElseThrow(() -> new MacroExecutionException("Unable to retrieve response from Allure"))).putAll(map).build();
        return VelocityUtils.getRenderedTemplate("templates/allure-testcase-macro.vm", context);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.INLINE;
    }
}
