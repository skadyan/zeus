package com.cumulativeminds.zeus.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class TemplateEngine {

    @Inject
    private TemplateEngineProperties templateEngineProperties;

    private Configuration configuration;

    @Inject
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() throws TemplateException {
        Configuration cfg = new Configuration();
        TemplateEngineProperties settings = templateEngineProperties;
        for (Map.Entry<String, String> setting : settings.getSettings().entrySet()) {
            cfg.setSetting(setting.getKey(), setting.getValue());
        }
        cfg.setAutoFlush(settings.isAutoFlush());
        ApplicationContextTemplateLoader loader = new ApplicationContextTemplateLoader(applicationContext,
                settings.getTemplateLoaderPath());
        loader.setSuffix(settings.getSuffix());
        cfg.setTemplateLoader(loader);

        this.configuration = cfg;
    }

    public void process(Writer out, String template, Object model) {
        try {
            Template t = configuration.getTemplate(template);
            t.process(model, out);
            out.close();
        } catch (TemplateException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
