package com.cumulativeminds.zeus.infra;

import java.util.Collection;

import org.springframework.beans.factory.BeanFactory;

import com.hazelcast.cluster.impl.TcpIpJoiner;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.instance.Node;

public class NonStandardTcpIpJoinerOverAWS extends TcpIpJoiner {

    private AwsUtil awsUtil;
    private String awsTagKey;
    private String awsTagValue;

    public NonStandardTcpIpJoinerOverAWS(Node node) {
        super(node);

        BeanFactory factory = (BeanFactory) node.config.getUserContext().get(HazelcastNodeFactoryBean.BEAN_FACTORY);
        AwsConfig awsConfig = node.getConfig().getNetworkConfig().getJoin().getAwsConfig();

        if (!awsConfig.isEnabled()) {
            String message = String.format("%s class requires you to enabled AwsConfig under network config", getClass());
            throw new IllegalStateException(message);
        }
        this.awsTagKey = awsConfig.getTagKey();
        this.awsTagValue = awsConfig.getTagValue();
        awsUtil = factory.getBean(AwsUtil.class);
    }

    @Override
    protected Collection<String> getMembers() {
        awsUtil.findMemberBy(awsTagKey, awsTagValue);
        return super.getMembers();
    }

}
