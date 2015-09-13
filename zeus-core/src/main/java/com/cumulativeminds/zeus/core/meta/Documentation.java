package com.cumulativeminds.zeus.core.meta;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public final class Documentation {
    private final String title;
    private final String description;
    private final List<String> examples;
    private final List<String> examplesReadOnly;

    public Documentation(String title, String description) {
        this.title = title;
        this.description = description;
        this.examples = new ArrayList<>();
        this.examplesReadOnly = unmodifiableList(examples);
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getExamples() {
        return examplesReadOnly;
    }

    void addExample(String example) {
        if (StringUtils.hasText(example)) {
            examples.add(example);
        }
    }

}
