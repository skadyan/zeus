package com.cumulativeminds.zeus.plugin.es;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataIndex;

public class ESPluginTest extends ESPluginTestCase {

    @Test
    public void validateESModelDataIndex() throws Exception {
        Model model = zzModel;
        assertThat(model.getName(), is("mock.ZZModel"));

        ModelDataIndex modelDataIndex = model.getModelDataIndex();
        assertThat(modelDataIndex, instanceOf(ESModelDataIndex.class));

        ESModelDataIndex es = (ESModelDataIndex) modelDataIndex;
        assertThat(es.getPhysicalIndexName(), is("zz_model_v1.0"));
    }
}
