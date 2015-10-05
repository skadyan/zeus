package com.cumulativeminds.zeus.plugin.flatfile;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.meta.K;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.AbstractItemReader;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.ItemReaderCallback;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ChangeItemReaderFlatfileImpl extends AbstractItemReader {
    private static Map<String, String> shortClassNames = new HashMap<>();

    static{
        shortClassNames.put("FlatFileItemReader", "org.springframework.batch.item.file.FlatFileItemReader");
    }
    private MultiResourceItemReader<EntityObject> resourceItemReader;

    public ChangeItemReaderFlatfileImpl(Model model, Map<String, Object> arguments) {
        super(model, arguments);

        FlatfileModelDataSource source = (FlatfileModelDataSource) model.getModelDataSource();
        String rawReader = source.getReaderType();
        TypedValueMapAccessor settings = source.getDefinition().getNestedObject(K.settings);
        ResourceAwareItemReaderItemStream<EntityObject> delegate = newRawResourceAwareItemReader(rawReader, settings);

        MultiResourceItemReader<EntityObject> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setDelegate(delegate);
        resourceItemReader.setResources(getResources());
        resourceItemReader.setStrict(true);

        this.resourceItemReader = resourceItemReader;
    }

    private Resource[] getResources() {
        // arguments.values().stream().filter(f -> f instanceof File);
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResourceAwareItemReaderItemStream<EntityObject> newRawResourceAwareItemReader(String rawReader,
            TypedValueMapAccessor settings) {
        String className = shortClassNames.getOrDefault(rawReader, rawReader);
        ResourceAwareItemReaderItemStream<EntityObject> reader = Zeus.instantiate(className,
                ResourceAwareItemReaderItemStream.class);
        BeanUtils.copyProperties(settings, reader);
        return reader;
    }

    @Override
    public void read(ItemReaderCallback cb) {

    }
}
