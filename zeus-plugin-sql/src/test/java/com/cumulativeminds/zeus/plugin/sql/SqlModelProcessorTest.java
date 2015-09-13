package com.cumulativeminds.zeus.plugin.sql;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.StringWriter;

import javax.inject.Inject;

import org.junit.Test;

import com.cumulativeminds.zeus.core.meta.Model;

public class SqlModelProcessorTest extends SqlPluginTestCase {
    private static boolean debugTest = true;

    @Inject
    private SqlMappingGenerator sqlModelProcessor;

    @Test
    public void generateSqlMapperForBlog() throws Exception {
        StringWriter writer = new StringWriter();
        Model model = zzModel;
        sqlModelProcessor.process(model, writer);
        String control = writer.toString();
        if (debugTest) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>\n\n");
            System.out.println(control);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>\n\n");
        }

        assertXMLEqual(control, getClasspathResourceAsString("/zz_model-sql-mapper.xml"));

    }

}
