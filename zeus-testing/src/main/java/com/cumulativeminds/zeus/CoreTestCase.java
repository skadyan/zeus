package com.cumulativeminds.zeus;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WiringForTest.class })
public class CoreTestCase {
    @Inject
    protected ApplicationContext applicationContext;

    static {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    protected String getClasspathResourceAsString(String resource) {
        try {
            final Resource resource2 = applicationContext.getResource(CLASSPATH_URL_PREFIX + resource);
            String s = StreamUtils.copyToString(resource2.getInputStream(), Charset.defaultCharset());
            return s;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
