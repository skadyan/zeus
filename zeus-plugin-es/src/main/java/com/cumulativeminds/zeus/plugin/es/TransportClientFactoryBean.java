package com.cumulativeminds.zeus.plugin.es;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.util.MapFlatterner;

@Component
@ConfigurationProperties("esplugin.client")
public class TransportClientFactoryBean extends AbstractFactoryBean<TransportClient> {

    private Map<String, Object> settings = new HashMap<>();

    private List<String> addresses;

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    @Override
    public Class<?> getObjectType() {
        return TransportClient.class;
    }

    @Override
    protected TransportClient createInstance() throws Exception {
        
        Settings settings2 = ImmutableSettings.builder().put(MapFlatterner.asFlatternedMap(settings))
                .build();

        TransportClient client = new TransportClient(settings2, false);
        addresses.forEach(a -> client.addTransportAddress(parseTransportAddress(a)));
        return client;
    }

    private TransportAddress parseTransportAddress(String address) {
        String[] hostAndPort = address.split(":");
        return new InetSocketTransportAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
    }

    @Override
    protected void destroyInstance(TransportClient instance) throws Exception {
        instance.close();
    }

}
