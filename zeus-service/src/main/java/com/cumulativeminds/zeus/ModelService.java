package com.cumulativeminds.zeus;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.api.internal.ChangeRequestThreadPoolExecutor;
import com.cumulativeminds.zeus.compute.ChangeTask;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.core.meta.ModelType;
import com.cumulativeminds.zeus.core.spi.Change;
import com.cumulativeminds.zeus.core.spi.ItemReader;
import com.cumulativeminds.zeus.integration.BootstrapModelDefintionLoader;

@Component
public class ModelService implements ModelSettings {
    private ModelRegistry registry;

    private List<Model> rootModels;

    private BeanFactory beanFactory;

    private IdentityHashMap<Model, ThreadPoolExecutor[]> executors;

    private Environment environment;

    @Inject
    public ModelService(BootstrapModelDefintionLoader loader, BeanFactory beanFactory, Environment environment) {
        this.registry = loader.getModelRegistry();
        this.beanFactory = beanFactory;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        initRootModels();
        initExecutors();
    }

    private void initExecutors() {
        IdentityHashMap<Model, ThreadPoolExecutor[]> executors = new IdentityHashMap<>();
        registry.forEach(m -> {
            if (m.getModelType() == ModelType.ROOT) {
                ThreadPoolExecutor[] threadPoolExecutors = new ThreadPoolExecutor[Change.PRIORITY_LOWEST];
                // for now only normal priority is worked out
                threadPoolExecutors[Change.PRIORITY_NORMAL] = ChangeRequestThreadPoolExecutor.newExecutor(m, this);

                executors.put(m, threadPoolExecutors);
            }
        });

        this.executors = executors;
    }

    private void initRootModels() {
        ArrayList<Model> list = new ArrayList<Model>();
        registry.forEach(m -> {
            if (m.getModelType() == ModelType.ROOT)
                list.add(m);
        });

        this.rootModels = list;
    }

    public List<Model> getRootModels() {
        return rootModels;
    }

    public Model getModel(String code) {
        return registry.getModelByCode(code);
    }

    public void submit(Change change) {
        Model model = getModel(change.getCode());

        int priority = priorityOf(model, change);
        Executor executor = getChangeExecutor(model, priority);

        executor.execute(newChangeTask(model, priority, change.getData()));
    }

    // for test visibility
    ChangeTask newChangeTask(Model model, int priority, Map<String, Object> data) {
        return beanFactory.getBean(ChangeTask.class, model, priority, newItemReader(model, data));
    }

    ItemReader newItemReader(Model model, Map<String, Object> data) {
        Class<? extends ItemReader> reader = model.getModelDataSource().getChangeItemReader();
        return beanFactory.getBean(reader, model, data);
    }

    Executor getChangeExecutor(Model model, int priority) {
        return executors.get(model)[priority];
    }

    private int priorityOf(Model model, Change change) {
        return Change.PRIORITY_NORMAL;
    }

    @Override
    public <T> T getSetting(Model model, String context, String key, Class<T> type, T defaultValue) {
        String key1 = "model." + context + "." + model.getCode() + "." + key;
        T value = environment.getProperty(key1, type);
        if (value == null) {
            key1 = "model." + context + "." + key;
            value = environment.getProperty(key1, type, defaultValue);
        }
        return value;
    }

    @Override
    public <T> T getSetting(Model model, String context, String key, Class<T> type) {
        return getSetting(model, context, key, type, null);
    }
}
