package com.cumulativeminds.zeus.core.spi;

public interface ItemReader {
    void initialize();
    void read(ItemReaderCallback cb);

}
