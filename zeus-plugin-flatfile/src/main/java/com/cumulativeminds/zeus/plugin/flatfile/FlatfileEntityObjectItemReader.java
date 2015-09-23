package com.cumulativeminds.zeus.plugin.flatfile;

import org.springframework.batch.item.file.FlatFileItemReader;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.EntityObjectItemReader;

public class FlatfileEntityObjectItemReader implements EntityObjectItemReader {

    private FlatFileItemReader<EntityObject> flatfileReader;

    public FlatfileEntityObjectItemReader() {
        flatfileReader = new FlatFileItemReader<>();
    }
     
    public FlatFileItemReader<EntityObject> getFlatfileReader() {
        return flatfileReader;
    }
    
    @Override
    public EntityObject read() {
        try {
            return flatfileReader.read();
        } catch (Exception e) {
            Zeus.sneakyThrow(e);
        }
        return null;
    }

}
