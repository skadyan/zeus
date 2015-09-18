package com.cumulativeminds.zeus.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.util.MapFlatterner;
import com.hazelcast.cluster.Joiner;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import com.hazelcast.util.StringUtil;

@Component
@ConfigurationProperties("hazelcastnode")
public class HazelcastNodeFactoryBean extends AbstractFactoryBean<HazelcastNode> {
    public static final String BEAN_FACTORY = "beanFactory";
    public static final String NON_STD_CLUSTER_JOINER_CLASS = "nonStandardClusterJoinerClass";

    private Map<String, Object> hazelcast = new HashMap<>();
    private Map<String, Object> settings = new HashMap<>();
    
    private String instanceName;

    private Config config = new Config();

    @Override
    public Class<?> getObjectType() {
        return HazelcastNode.class;
    }

    public Config getConfig() {
        return config;
    }

    @Override
    protected HazelcastNode createInstance() throws Exception {
        config.setProperties(convertAndGetProperties());
        config.getUserContext().put(BEAN_FACTORY, getBeanFactory());

        final Class<? extends Joiner> nonStandardClusterJoinerClass = getNonStdClusterJoinerClass();

        HazelcastInstance hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(
                config,
                config.getInstanceName(),
                new NonStandardNodeContext(nonStandardClusterJoinerClass));

        return new HazelcastNode(hazelcastInstance, settings);
    }

    private Class<? extends Joiner> getNonStdClusterJoinerClass() {
        Class<? extends Joiner> type = null;
        try {
            String className = (String) settings.get(NON_STD_CLUSTER_JOINER_CLASS);
            if (!StringUtil.isNullOrEmpty(className)) {
                logger.info("Creating nonstandard cluster Joiner");
                Class<?> class1 = Class.forName(className);
                type = class1.asSubclass(Joiner.class);
            }
        } catch (Exception e) {
            Zeus.sneakyThrow(e);
        }
        return type;
    }

    private Properties convertAndGetProperties() {
        Properties props = new Properties();
        Map<String, Object> asFlatternedMap = MapFlatterner.asFlatternedMap(hazelcast);
        asFlatternedMap.forEach((key, value) -> props.put("hazelcast." + key, String.valueOf(value)));
        return props;
    }

    public void setHazelcast(Map<String, Object> properties) {
        this.hazelcast = properties;
    }

    public Map<String, Object> getHazelcast() {
        return hazelcast;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    protected void destroyInstance(HazelcastNode instance) throws Exception {
        instance.destory();
    }
}
