package com.cumulativeminds.zeus.plugin.mongostore;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataStore;

public class StorePluginTest extends StorePluginTestCase {

    @Test
    public void test1() {
        Model model = zzModel;

        assertThat(model.getName(), is("mock.ZZModel"));

        ModelDataStore modelDataStore = model.getModelDataStore();
        assertThat(modelDataStore, instanceOf(MongodDataStore.class));

        MongodDataStore ds = (MongodDataStore) modelDataStore;
        assertThat(ds.getCollectionName(), is("ZZ_Model"));
    }
}
