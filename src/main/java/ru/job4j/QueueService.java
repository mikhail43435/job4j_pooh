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
 *
 * Queues
 * A JMS Queue implements load balancer semantics.
 * A single message will be received by exactly one consumer.
 * If there are no consumers available at the time the message is sent
 * it will be kept until a consumer is available that can process the message.
 * If a consumer receives a message and does not acknowledge it before closing
 * then the message will be redelivered to another consumer.
 * A queue can have many consumers with messages load balanced across the available consumers.
 */

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> cMap =
            new ConcurrentHashMap<>();

    @Override
    public ServerResponse process(MessageParser message) {
        if (!message.getPoohMode().equals("queue")) {
            return new ServerResponse("Invalid mode type", 400);
        }
        if (message.httpRequestType().equals("POST")) {
            return processPostMessage(message);
        } else if (message.httpRequestType().equals("GET")) {
            return processGetMessage(message);
        }
        return new ServerResponse("Invalid method name in message header", 400);
    }

    private ServerResponse processPostMessage(MessageParser message) {
        cMap.putIfAbsent(message.getQueueName(), new ConcurrentLinkedQueue<>());
        cMap.get(message.getQueueName()).add(message.getParam());
        return new ServerResponse("OK", 200);
    }

    private ServerResponse processGetMessage(MessageParser message) {
        ConcurrentLinkedQueue<String> queue = cMap.get(message.getQueueName());
        if (queue == null) {
            return new ServerResponse("Queue not found", 404);
        }
        String param = queue.poll();
        if (param != null) {
            return new ServerResponse(param, 200);
        } else {
            return new ServerResponse("Queue is empty", 400);
        }
    }
}