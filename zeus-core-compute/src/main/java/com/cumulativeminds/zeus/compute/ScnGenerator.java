package com.cumulativeminds.zeus.compute;

import org.springframework.stereotype.Component;

@Component
public class ScnGenerator {

    public String newScn() {
        return String.format("%s", System.nanoTime());
    }
}
