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
 *  */
public class TopicService implements Service {

    private static final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> CMAP
            = new ConcurrentHashMap<>();

    @Override
    public ServerResponse process(MessageParser message) {
        String userIdLabel = "userId";
        if (message.getMethodName().equals("POST")) {
            if (!CMAP.containsKey(message.getQueueName())) {
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> innerMap =
                        new ConcurrentHashMap<>();
                innerMap.put(message.getParamForKey(userIdLabel), new ConcurrentLinkedQueue<>());
                CMAP.putIfAbsent(message.getQueueName(), innerMap);
            } else if (!CMAP.get(message.getQueueName())
                    .containsKey(message.getParamForKey(userIdLabel))) {
                CMAP.get(message.getQueueName())
                        .put(message.getParamForKey(userIdLabel), new ConcurrentLinkedQueue<>());
            }
            CMAP.get(message.getQueueName())
                    .get(message.getParamForKey(userIdLabel))
                    .add(message.getParamForKey("temperature"));
            return new ServerResponse("Posted " + message.getQueueName(), 200);
        } else if (message.getMethodName().equals("GET")) {
            if (CMAP.containsKey(message.getQueueName())) {
                return new ServerResponse(CMAP.get(message.getQueueName())
                        .get(message.getParamForKey(userIdLabel))
                        .poll(), 200);
            } else {
                return new ServerResponse("Queue not found", 404);
            }
        }
        return new ServerResponse("Invalid method name in message header", 400);
    }
}