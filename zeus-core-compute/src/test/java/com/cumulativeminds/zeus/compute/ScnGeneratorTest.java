package com.cumulativeminds.zeus.compute;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Before;
import org.junit.Test;

public class ScnGeneratorTest {

    private ScnGenerator generator;

    public ScnGeneratorTest() {
    }

    @Before
    public void setup() throws Exception {
        generator = new ScnGenerator();
    }

    @Test
    public void generateScn() throws Exception {
        String scn1 = generator.newScn();
        String scn2 = generator.newScn();
        
        assertThat(scn1, lessThan(scn2));
    }
}
