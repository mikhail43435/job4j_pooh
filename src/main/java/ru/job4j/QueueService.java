package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Queue
 * Отправитель посылает сообщение с указанием очереди.
 * Получатель читает первое сообщение и удаляет его из очереди.
 * Если приходят несколько получателей, то они читают из одной очереди.
 * Уникальное сообщение может быть прочитано, только одним получателем.
 *
 * Пример запросов:
 *
 * POST запрос должен добавить элементы в очередь weather.
 * curl -X POST -d "temperature=18" http://localhost:9000/queue/weather
 * queue указывает на режим "очередь".
 * weather указывает на имя очереди.
 * Если очереди нет в сервисе, то нужно создать новую.
 *
 * GET запрос должен получить элементы из очереди weather.
 * curl -X GET http://localhost:9000/queue/weather
 * Ответ, temperature=18
 */
public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> cMap =
            new ConcurrentHashMap<>();

    @Override
    public ServerResponse process(MessageParser message) {
        if (message.getMethodName().equals("POST")) {
            cMap.putIfAbsent(message.getQueueName(), new ConcurrentLinkedQueue<>());
            cMap.get(message.getQueueName()).add(message.getSingleParam());
            return new ServerResponse("OK", 200);
        } else if (message.getMethodName().equals("GET")) {
            if (cMap.containsKey(message.getQueueName())) {
                return new ServerResponse(cMap.get(message.getQueueName()).poll(), 200);
            } else {
                return new ServerResponse("Queue not found", 404);
            }
        }
        return new ServerResponse("Invalid method name in message header", 400);
    }
}