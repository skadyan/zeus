package com.cumulativeminds.zeus.impl;

import com.cumulativeminds.zeus.core.meta.Model;

public interface ModelProcessor {
    void process(Model model, Object accumulator);
}