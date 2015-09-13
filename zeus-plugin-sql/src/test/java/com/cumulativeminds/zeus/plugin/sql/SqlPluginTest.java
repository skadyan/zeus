package com.cumulativeminds.zeus.plugin.sql;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;

public class SqlPluginTest extends SqlPluginTestCase {

    @Test
    public void validSqlModelSource() throws Exception {
        Model model = zzModel;
        assertThat(model.getName(), is("mock.ZZModel"));

        ModelDataSource modelDataSource = model.getModelDataSource();
        assertThat(modelDataSource, instanceOf(SqlModelDataSource.class));

        SqlModelDataSource sql = (SqlModelDataSource) modelDataSource;

        assertThat(sql.getIntegrationModel(), is(ModelSourceIntegrationModel.PULL));
        assertThat(sql.getChangeViewName(), is("V_CUSTOM_MODEL_CHG"));
    }

}
