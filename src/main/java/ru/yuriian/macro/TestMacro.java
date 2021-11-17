package ru.yuriian.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.Map;

public class TestMacro implements Macro {

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        String output = "<div class =\"testmacro\">";
        output = output + "<div class = \"" + map.get("Color") + "\">";
        if (map.get("Title") != null) {
            output = output + ("<h1>Hello " + map.get("Title") + "!</h1>");
        } else {
            output = output + "<h1>Hello World!<h1>";
        }
        output = output + "</div>" + "</div>";
        return output;    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
