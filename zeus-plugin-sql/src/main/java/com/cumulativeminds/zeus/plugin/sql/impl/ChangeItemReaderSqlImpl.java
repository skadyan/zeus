package com.cumulativeminds.zeus.plugin.sql.impl;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.AbstractItemReader;
import com.cumulativeminds.zeus.core.spi.ItemReaderCallback;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ChangeItemReaderSqlImpl extends AbstractItemReader {
    public ChangeItemReaderSqlImpl(Model model, Map<String, Object> arguments) {
        super(model, arguments);
    }

    @Override
    public void initialize() {

    }
    @Override
    public void read(ItemReaderCallback cb) {
    }

}
