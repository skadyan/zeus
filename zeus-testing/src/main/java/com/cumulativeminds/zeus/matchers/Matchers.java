package com.cumulativeminds.zeus.matchers;

public class Matchers extends org.hamcrest.Matchers {
    public static RegexMatcher matchesPattern(String regex) {
        return RegexMatcher.matches(regex);
    }
}
