package com.cumulativeminds.zeus;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;

import org.springframework.boot.context.web.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class SpringJerseyConfiguration {
    @Bean
    public static HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter() {
            private final String JERSEY_API_ROOT = (String) AnnotationUtils
                    .getValue(ServiceResourceConfiguration.class.getAnnotation(ApplicationPath.class)) + "/";

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws IOException, ServletException {
                String uri = request.getRequestURI();
                if (uri.startsWith(JERSEY_API_ROOT)) {
                    filterChain.doFilter(request, response);
                } else {
                    super.doFilterInternal(request, response, filterChain);
                }
            }
        };
    }

    @Bean
    public static View error() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // kind of hack; if client does not specify application/json as accepted
        // type.
        jsonView.setContentType("text/html");
        return jsonView;
    }

}
