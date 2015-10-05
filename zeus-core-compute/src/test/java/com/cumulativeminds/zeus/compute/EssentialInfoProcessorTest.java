package com.cumulativeminds.zeus.compute;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.cumulativeminds.zeus.compute.internal.ItemImpl;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.core.meta.StdFields;
import com.cumulativeminds.zeus.core.meta.StdModelFeature;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;
import com.cumulativeminds.zeus.core.spi.StdItemState;

public class EssentialInfoProcessorTest {
    private ScnGenerator scnGenerator;
    private EssentialInfoProcessor processor;

    @Rule
    public ExpectedException expection = ExpectedException.none();
    private Model mockModel;
    private ItemImpl mockItem;
    private ProcessingContext context;
    private HashSet<String> modelFeatures;
    private String itemScn = "ItemSCN";
    private EntityObject payload;

    @Before
    public void setup() throws Exception {
        scnGenerator = mock(ScnGenerator.class);
        when(scnGenerator.newScn()).thenReturn(itemScn);
        processor = new EssentialInfoProcessor(scnGenerator);

        modelFeatures = newHashSet(
                StdModelFeature.SourceHasEntityState,
                StdModelFeature.SourceHasVersion);
        mockModel = createMockModel("junit-mock-model", modelFeatures);
        when(mockModel.getModelDataStore()).thenReturn(mock(ModelDataStore.class));

        payload = new EntityObject();

        payload.put(StdFields.effectiveDate.name(), LocalDateTime.now());
        payload.put(StdFields.lastUpdated.name(), LocalDateTime.now());
        payload.put(StdFields.guid.name(), "1000001");
        payload.put(StdFields.version.name(), 1);
        
        mockItem = new ItemImpl(mockModel, payload);        
        mockItem.state(StdItemState.PENDING);
        mockItem.scn("100001");
        

        context = newMockProcessingContext(mockModel, mockItem);
    }

    @Test
    public void stateGetSetToPendingOnNonStateAwareModelContract() throws Exception {
        // set expected exception
        modelFeatures.remove(StdModelFeature.SourceHasEntityState);
        mockItem.state(null);
        // call processor
        processor.process(context);

        assertThat(mockItem.getState(), is(StdItemState.PENDING));
    }

    @Test
    public void goodItemDoesNotViolateTheModelContract() throws Exception {
        processor.process(context);
    }

    @Test
    public void noLastModifiedDoesNotViolatesTheModelContractAndSetTheDefaultValue() throws Exception {
        mockItem.lastUpdated(null);
        // call processor
        processor.process(context);

        assertThat(mockItem.getLastUpdated(), notNullValue());
    }

    @Test
    public void nullVersionDoesNotViolateTheNonVersionAwareModelContract() throws Exception {
        // set expected exception
        modelFeatures.remove(StdModelFeature.SourceHasVersion);
        payload.remove(StdFields.version.name());
        // call processor
        processor.process(context);
    }

    @Test
    public void noVersionViolatesTheModelContract() throws Exception {
        // set expected exception
        expection.expect(ModelContractViolationException.class);

        payload.remove(StdFields.version.name());
        // call processor
        processor.process(context);
    }

    @Test
    public void scnGetSetWhenScnIsUnknown() throws Exception {
        mockItem.scn(null);
        // call processor
        processor.process(context);

        assertThat(mockItem.scn(), is(itemScn));
    }

    @Test
    public void noEffectiveDateViolatesTheModelContract() throws Exception {
        // set expected exception
        expection.expect(ModelContractViolationException.class);

        payload.remove(StdFields.effectiveDate.name());
        // call processor
        processor.process(context);
    }

    @Test
    public void noEffectiveDateOnNullableStoreDoesNotViolatesTheModelContract() throws Exception {

        payload.remove(StdFields.effectiveDate.name());
        when(mockModel.getModelDataStore()).thenReturn(null);
        // call processor
        processor.process(context);
    }

    @Test
    public void noStateViolatesTheModelContract() throws Exception {
        // set expected exception
        expection.expect(ModelContractViolationException.class);

        mockItem.state(null);
        // call processor
        processor.process(context);
    }
    
    @Test
    public void noKeyViolatesTheModelContract() throws Exception {
        // set expected exception
        expection.expect(ModelContractViolationException.class);

        payload.remove(StdFields.guid.name());
        // call processor
        processor.process(context);
    }

    @Test
    public void noPayloadViolatesTheModelContract() throws Exception {
        // set expected exception
        expection.expect(ModelContractViolationException.class);
        //item with NULL payload
        context  = newMockProcessingContext(mockModel, new ItemImpl(mockModel, null));

        // call processor
        processor.process(context);
    }

    private ProcessingContext newMockProcessingContext(Model mockModel, Item... items) {
        ProcessingContext context = mock(ProcessingContext.class);
        when(context.iterator()).thenReturn(Arrays.<Item> asList(items).iterator());
        when(context.getModel()).thenReturn(mockModel);
        return context;
    }

    private Model createMockModel(String code, Set<String> features) {
        Model mockModel = Mockito.mock(Model.class);
        when(mockModel.getCode()).thenReturn(code);
        when(mockModel.getFieldNameOf(any())).then(a -> a.getArguments()[0].toString());
        when(mockModel.hasFeature(anyString())).then(a -> features.contains(a.getArguments()[0]));
        when(mockModel.getKeyPropertyName()).thenReturn("guid");
        return mockModel;
    }
}
