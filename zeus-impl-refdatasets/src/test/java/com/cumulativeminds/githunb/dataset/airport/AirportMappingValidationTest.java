package com.cumulativeminds.githunb.dataset.airport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.cumulativeminds.zeus.CoreTestCase;

@ContextConfiguration(classes = { ConfigLoader.class })
public class AirportMappingValidationTest extends CoreTestCase {
    @Test
    public void loadModelDefinition() throws Exception {
        assertTrue(true);
    }
}
