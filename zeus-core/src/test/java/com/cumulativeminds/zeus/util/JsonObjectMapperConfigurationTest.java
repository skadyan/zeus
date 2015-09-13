package com.cumulativeminds.zeus.util;

import static com.cumulativeminds.zeus.matchers.Matchers.matchesPattern;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectMapperConfigurationTest {

    private ObjectMapper objectMapper;

    @Test
    public void jsonObjectMapperIsConfigured() throws Exception {
        objectMapper = new JsonObjectMapper();
        LocalDate date = LocalDate.now();
        String nowAsStr = objectMapper.writeValueAsString(date);

        assertThat(nowAsStr, matchesPattern("\"\\d{4}-\\d{2}-\\d{2}\""));
    }
}
