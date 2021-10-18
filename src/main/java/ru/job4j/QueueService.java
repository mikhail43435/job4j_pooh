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
        if (message.httpRequestType().equals("POST")) {
            cMap.putIfAbsent(message.getSourceName(), new ConcurrentLinkedQueue<>());
            cMap.get(message.getSourceName()).add(message.getFirstParam());
            return new ServerResponse("OK", 200);
        } else if (message.httpRequestType().equals("GET")) {
            String param = cMap.get(message.getSourceName()).poll();
            if (param != null) {
                return new ServerResponse(param, 200);
            } else {
                return new ServerResponse("Queue not found", 404);
            }
        }
        return new ServerResponse("Invalid method name in message header", 400);
    }
}