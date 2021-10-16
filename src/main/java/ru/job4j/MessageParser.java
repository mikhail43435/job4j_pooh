package ru.job4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Req - класс, служить для парсинга входящего сообщения.
 * method - GET или POST. Он указывает на тип запроса.
 * mode - указывает на режим работы: queue или topic.
 * queue - имя очереди.
 * params - содержимое запроса.
 * <p>
 * var content = "POST /topic/weather HTTP/1.1" + System.lineSeparator()
 */

public class MessageParser {
    private final String method;
    private final String mode;
    private final String queueName;
    private final Map<String, String> params;

    public MessageParser(String method, String mode, String queueName, Map<String, String> params) {
        this.method = method;
        this.mode = mode;
        this.queueName = queueName;
        this.params = params;
    }

    public static MessageParser of(String content) {
        String[] contentArray = content.split(System.lineSeparator());
        if (contentArray.length != 0) {
            if (contentArray[0].startsWith("POST")) {
                return getPostMessage(contentArray);
            } else if (contentArray[0].startsWith("GET")) {
                return getGetMessage(contentArray);
            } else {
                throw new IllegalArgumentException("Illegal message provided: method type not found");
            }
        } else {
            throw new IllegalArgumentException("Illegal message provided: message is empty");
        }
    }

    private static MessageParser getPostMessage(String[] contentArray) {
        String[] firstLineArray = contentArray[0].replace("/", " ").split(" ");
        if (firstLineArray.length < 3) {
            throw new IllegalArgumentException("Invalid header: mode or queue info not found");
        }
        String mode = firstLineArray[2];
        String queue = firstLineArray[3];
        if (contentArray.length < 7) {
            throw new IllegalArgumentException("Invalid header: params info not found");
        }
        String[] paramArray = contentArray[6].split("=");
        if (paramArray.length < 2) {
            throw new IllegalArgumentException("Invalid header: invalid params data");
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(paramArray[0], paramArray[1]);
        return new MessageParser("POST", mode, queue, paramsMap);

    }

    private static MessageParser getGetMessage(String[] contentArray) {
        String[] firstLineArray = contentArray[0].replace("/", " ").split(" ");
        if (firstLineArray.length < 3) {
            throw new IllegalArgumentException("Invalid header: mode or queue info not found");
        }
        String mode = firstLineArray[2];
        String queue = firstLineArray[3];
        if (contentArray.length < 5) {
            throw new IllegalArgumentException("Invalid header: params info not found");
        }
        String[] paramArray = contentArray[4].split("=");
        if (paramArray.length < 2) {
            throw new IllegalArgumentException("Invalid header: invalid params data");
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(paramArray[0], paramArray[1]);
        return new MessageParser("GET", mode, queue, paramsMap);

    }

    public String getMethodName() {
        return method;
    }

    public String getMode() {
        return mode;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getSingleParam() {
        return params.entrySet().iterator().next().getValue();
    }

    public String getParamForKey(String key) {
        return params.get(key);
    }
}