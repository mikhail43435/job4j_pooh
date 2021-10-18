package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Topic
 * Отправитель посылает сообщение с указанием темы.
 * Получатель читает первое сообщение и удаляет его из очереди.
 * ДЛЯ КАЖДОГО потребителя в режиме "topic"
 * должна быть уникальная очередь потребления в отличии от режима "queue",
 * где очереди для всех клиентов одна и та же.
 * POST /topic/weather -d "temperature=18"
 * GET /topic/weather/1
 * topic - указывает на режим темы
 * weather - имя темы, если темы нет, то нужно создать новую
 * 1 - ID клиента
 * Ответ temperature=18
 */
public class TopicService implements Service {

    private static final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> CMAP
            = new ConcurrentHashMap<>();
    private static final String USER_ID_STRING = "userId";

    @Override
    public ServerResponse process(MessageParser message) {
        if (message.httpRequestType().equals("POST")) {
            return getResponseForPostMethod(message);
        } else if (message.httpRequestType().equals("GET")) {
            return getResponseForGetMethod(message);
        }
        return new ServerResponse("Invalid method name in message header", 400);
    }

    private ServerResponse getResponseForPostMethod(MessageParser message) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> innerMap =
                new ConcurrentHashMap<>();
        innerMap.put(message.getParamForKey(USER_ID_STRING), new ConcurrentLinkedQueue<>());
        CMAP.putIfAbsent(message.getSourceName(), innerMap);
        CMAP.get(message.getSourceName())
                .putIfAbsent(message.getParamForKey(USER_ID_STRING), new ConcurrentLinkedQueue<>());
        CMAP.get(message.getSourceName())
                .get(message.getParamForKey(USER_ID_STRING))
                .add(message.getParamForKey("temperature"));
        return new ServerResponse("Posted " + message.getSourceName(), 200);
    }

    private ServerResponse getResponseForGetMethod(MessageParser message) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map =
                CMAP.get(message.getSourceName());
        if (map != null) {
            return new ServerResponse(map
                    .get(message.getParamForKey(USER_ID_STRING))
                    .poll(), 200);
        } else {
            return new ServerResponse("Queue not found", 404);
        }
    }
}