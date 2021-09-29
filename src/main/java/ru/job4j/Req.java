package ru.job4j;

import java.util.Map;

public class Req {
    private final String method;
    private final String mode;
    private final String queue;
    private final Map<String, String> params;

    public Req(String method, String mode, String queue, Map<String, String> params) {
        this.method = method;
        this.mode = mode;
        this.queue = queue;
        this.params = params;
    }

    public static Req of(String content) {
        /* TODO parse a content */
        return new Req(null, null,  null,null);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String queue() {
        return null;
    }

    public String param(String key) {
        return params.get(key);
    }
}