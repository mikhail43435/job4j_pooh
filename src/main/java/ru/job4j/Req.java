package ru.job4j;

import java.util.Map;

/**
 * Req - класс, служить для парсинга входящего сообщения.
 * method - GET или POST. Он указывает на тип запроса.
 * mode - указывает на режим работы: queue или topic.
 * queue - имя очереди.
 * params - содержимое запроса.
 */

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
        return queue;
    }

    public String param(String key) {
        return params.get(key);
    }
}