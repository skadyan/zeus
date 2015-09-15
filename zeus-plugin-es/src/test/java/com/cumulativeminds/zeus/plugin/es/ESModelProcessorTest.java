package com.cumulativeminds.zeus.plugin.es;

import java.io.StringWriter;

import javax.inject.Inject;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.cumulativeminds.zeus.core.meta.Model;

public class ESModelProcessorTest extends ESPluginTestCase {
    private static boolean debugTest = !true;

    @Inject
    private ESModelProcessor esModelProcessor;

    @Test
    public void generateSqlMapperForTestModel() throws Exception {
        StringWriter writer = new StringWriter();
        Model model = zzModel;
        esModelProcessor.process(model, writer);
        String control = writer.toString();
        if (debugTest) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>\n\n");
            System.out.println(control);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>\n\n");
        }

        String expected = getClasspathResourceAsString("/zz_model-default-elasticsearch-index-def.json");
        JSONAssert.assertEquals(control, expected, JSONCompareMode.LENIENT);

    }
}
