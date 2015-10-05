package com.cumulativeminds.zeus;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.cumulativeminds.zeus.core.spi.Change;

public class ModelServiceTest extends ServiceTestCase {
    @Inject
    ModelService modelService;
    private ModelService spiedService;

    public ModelServiceTest() {
    }

    @Before
    public void beforeSetup() throws Exception {
        spiedService = spy(modelService);
    }

    @Test
    public void alteastOneModelIsRegistered() throws Exception {
        int size = modelService.getRootModels().size();

        assertThat(size, is(greaterThan(0)));
    }

    @Test
    public void submitAFlatFilechange() throws Exception {
        Map<String, Object> data = new HashMap<>();
        String modelCode = "airport";
        Change change = new Change(modelCode, data);
        doReturn(RunInCurrentThreadExecutor.INSTANCE).when(spiedService).getChangeExecutor(Mockito.any(), Mockito.anyInt());

        spiedService.submit(change);
    }

}
