package com.cumulativeminds.zeus.infra;

import java.lang.reflect.Constructor;

import com.hazelcast.cluster.Joiner;
import com.hazelcast.instance.DefaultNodeContext;
import com.hazelcast.instance.Node;
import com.hazelcast.util.ExceptionUtil;

public class NonStandardNodeContext extends DefaultNodeContext {
    private Class<? extends Joiner> nonStandardClusterJoinerClass;

    public NonStandardNodeContext() {
    }

    public NonStandardNodeContext(Class<? extends Joiner> nonStandardClusterJoinerClass) {
        this.nonStandardClusterJoinerClass = nonStandardClusterJoinerClass;
    }

    @Override
    public Joiner createJoiner(Node node) {
        return nonStandardClusterJoinerClass == null
                ? super.createJoiner(node)
                : instanciate(node);
    }

    private Joiner instanciate(Node node) {
        Joiner joiner = null;
        try {
            Constructor<? extends Joiner> constructor = nonStandardClusterJoinerClass.getConstructor(Node.class);
            joiner = constructor.newInstance(node);
        } catch (Throwable ex) {
            ExceptionUtil.rethrow(ex);
        }
        return joiner;
    }
}
