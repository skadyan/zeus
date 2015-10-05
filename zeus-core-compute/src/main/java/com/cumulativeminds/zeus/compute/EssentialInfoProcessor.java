package com.cumulativeminds.zeus.compute;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.StdModelFeature;
import com.cumulativeminds.zeus.core.spi.AbstractItemHandler;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;
import com.cumulativeminds.zeus.core.spi.StdItemState;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EssentialInfoProcessor extends AbstractItemHandler {
    private ScnGenerator scnGenerator;

    @Inject
    public EssentialInfoProcessor(ScnGenerator scnGenerator) {
        this.scnGenerator = scnGenerator;
    }

    @Override
    public void process(ProcessingContext context) {
        log.trace("Processing started of context {}", context);
        final Model model = context.getModel();
        final boolean hasVersion = model.hasFeature(StdModelFeature.SourceHasVersion);
        final boolean hasItemState = model.hasFeature(StdModelFeature.SourceHasEntityState);
        final boolean requireEffectiveDate = model.getModelDataStore() != null;

        int i = 0;
        for (Item item : context) {
            EntityObject payload = item.getPayload();
            String key = item.getItemKey();

            // 1. validate payload
            contractViolation(payload != null, "payload is not populated of item at: {0} ", i);

            // 2. validate key
            contractViolation(hasText(key), "key is not populated of item: {0} ", i);

            // 3. If has Version, check it
            if (hasVersion) {
                contractViolation(item.version() != null, "version is not populated of item: {0} at {1}", key, i);
            }

            // 4. If has store, check it effective date
            if (requireEffectiveDate) {
                contractViolation(item.getEffectiveDate() != null, "effectiveDate is not populated of item: {0} at {1}", key, i);
            }

            // populate key data

            if (item.getLastUpdated() == null) {
                item.lastUpdated(LocalDateTime.now());
            }

            if (item.scn() == null) {
                item.scn(scnGenerator.newScn());
            }

            if (hasItemState) {
                contractViolation(item.getState() != null, "state is not populated of item: {0} at {1}", key, i);
            } else {
                item.state(StdItemState.PENDING);
            }

            i++;
        }

    }

    private void contractViolation(boolean pass, String message, Object... args) {
        if (!pass) {
            throw new ModelContractViolationException(message, args);
        }
    }
}
