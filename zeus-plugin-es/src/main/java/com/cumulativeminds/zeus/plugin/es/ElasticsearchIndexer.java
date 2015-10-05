package com.cumulativeminds.zeus.plugin.es;

import java.util.HashMap;

import javax.inject.Inject;

import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;

import com.cumulativeminds.zeus.core.features.SourceHasVersion;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.AbstractItemHandler;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;
import com.cumulativeminds.zeus.core.spi.StdItemState;

public class ElasticsearchIndexer extends AbstractItemHandler {
    private TransportClient client;

    @Inject
    public ElasticsearchIndexer(TransportClient client) {
        this.client = client;
    }

    @Override
    public void process(ProcessingContext context) {
        Model model = context.getModel();
        ESModelDataIndex modelDataIndex = (ESModelDataIndex) model.getModelDataIndex();
        String indexPhysicalName = modelDataIndex.getPhysicalIndexName();
        boolean versionAware = model.hasFeature(SourceHasVersion.NAME);
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        HashMap<String, Item> items = new HashMap<>();
        context.forEach(i -> {
            if (StdItemState.INDEX.isItemStateGood(i.getState())) {
                String id = i.getItemKey();
                IndexRequestBuilder requestBuilder = client.prepareIndex(
                        indexPhysicalName,
                        modelDataIndex.getIndexName(),
                        id).setConsistencyLevel(WriteConsistencyLevel.DEFAULT);

                if (versionAware) {
                    requestBuilder.setVersion(i.version())
                            .setVersionType(VersionType.EXTERNAL);
                }

                bulkRequestBuilder.add(requestBuilder.request());
                items.put(id, i);
            }
        });

        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        log.debug("bulk request executed and took: {}", bulkResponse.getTook());
        if (bulkResponse.hasFailures()) {
            log.error("bulk request has failures in index: {}", indexPhysicalName);
            bulkResponse.forEach(r -> {
                if (r.getFailure().getStatus() == RestStatus.CONFLICT) {
                    items.get(r.getId()).state(StdItemState.INDEX);
                } else {
                    items.get(r.getId()).state(StdItemState.INDEX_FAILED);
                }
            });
        } else {
            items.values().forEach(i -> i.state(StdItemState.INDEX));
        }

    }
}
