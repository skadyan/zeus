package com.cumulativeminds.zeus.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

public class ClassPathScanningYamlDefinitionProvider {
    private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

    @Inject
    public ClassPathScanningYamlDefinitionProvider(ResourceLoader resourceLoader) {
        pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }

    public Resource[] scan(String basePackage) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + "/**/*.yaml";

        return pathMatchingResourcePatternResolver.getResources(packageSearchPath);
    }

}
