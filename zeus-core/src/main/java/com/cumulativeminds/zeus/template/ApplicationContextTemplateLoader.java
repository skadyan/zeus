package com.cumulativeminds.zeus.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import freemarker.cache.TemplateLoader;

public class ApplicationContextTemplateLoader implements TemplateLoader {

    private String[] templateLoaderPath;
    private ResourceLoader resourceLoader;
    private String suffix;

    public ApplicationContextTemplateLoader(ResourceLoader resourceLoader, String[] templateLoaderPath) {
        this.resourceLoader = resourceLoader;
        this.templateLoaderPath = templateLoaderPath;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        if (!name.endsWith(suffix)) {
            name += suffix;
        }
        for (String path : templateLoaderPath) {
            Resource resource = resourceLoader.getResource(path + name);
            if (resource.exists()) {
                return new Source(resource);
            }
        }
        Resource resource = resourceLoader.getResource(name);
        return resource.exists() ? new Source(resource) : null;
    }

    @Override
    public long getLastModified(Object templateSource) {
        try {
            return ((Source) templateSource).resource.lastModified();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        try {
            InputStream in = ((Source) templateSource).resource.getInputStream();
            return new InputStreamReader(in, encoding);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private static class Source {
        Resource resource;

        public Source(Resource resource) {
            this.resource = resource;
        }
    }

}
