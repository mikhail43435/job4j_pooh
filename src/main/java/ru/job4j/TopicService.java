package ru.job4j;

import java.util.Map;
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

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> cMap
            = new ConcurrentHashMap<>();
    private final String userIdString = "userId";

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
        innerMap.put(message.getParamForKey(userIdString), new ConcurrentLinkedQueue<>());
        cMap.putIfAbsent(message.getSourceName(), innerMap);
        cMap.get(message.getSourceName())
                .putIfAbsent(message.getParamForKey(userIdString), new ConcurrentLinkedQueue<>());
        cMap.get(message.getSourceName())
                .get(message.getParamForKey(userIdString))
                .add(message.getParamForKey("temperature"));
        return new ServerResponse("Posted " + message.getSourceName(), 200);
    }

    private ServerResponse getResponseForGetMethod(MessageParser message) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map =
                cMap.get(message.getSourceName());
        if (map != null) {
            return new ServerResponse(map
                    .get(message.getParamForKey(userIdString))
                    .poll(), 200);
        } else {
            return new ServerResponse("Queue not found", 404);
        }
    }
}