package com.cumulativeminds.zeus.core.spi;

import com.cumulativeminds.zeus.core.meta.Model;

public interface ModelProcessor {
    void process(Model model, Object accumulator);
}