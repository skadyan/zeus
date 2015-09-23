package com.cumulativeminds.zeus.plugin.sql.impl;

import static org.springframework.util.StringUtils.capitalize;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.AbstractItemHandler;
import com.cumulativeminds.zeus.core.spi.ItemStateHandler;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;
import com.cumulativeminds.zeus.plugin.sql.SqlK;

public class ChangeItemStateHandlerSqlImpl extends AbstractItemHandler implements ItemStateHandler {

    private BeanFactory beanFactory;

    @Inject
    public ChangeItemStateHandlerSqlImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void process(ProcessingContext context) {
        Model model = context.getModel();
        SqlSessionTemplate template = getSqlTemplate(model);
        String updateStatement = getUpdateStatementId(model);

        context.forEach(i -> template.update(updateStatement, i));
        log.debug("UpdateStatement: {} is used for processing context: {}", updateStatement, context);
    }

    private String getUpdateStatementId(Model model) {
        return capitalize(model.getCode()) + "Mapper.updateItemState";
    }

    private SqlSessionTemplate getSqlTemplate(Model model) {
        String dataSource = model.getModelDataSource().getDefinition().getSimpleValue(SqlK.dataSource);
        String beanName = String.format("sqlSessionBatchTemplate-" + dataSource);

        return beanFactory.getBean(beanName, SqlSessionTemplate.class);
    }

}
