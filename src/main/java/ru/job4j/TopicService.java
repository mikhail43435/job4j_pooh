package ru.job4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
* Topics
* In JMS a Topic implements publish and subscribe semantics.
* When you publish a message it goes to all the subscribers who are interested
* - so zero to many subscribers will receive a copy of the message.
* Only subscribers who had an active subscription at the time
 * the broker receives the message will get a copy of the message.
 *
*/

public class TopicService implements Service {

    /**
     * cMap - мапа содержащая данные об очередях и подписчиках.
     * key - имя очереди
     * value - мапа,
     *      где key - имя подписчика
     *      value - очередь ConcurrentLinkedQueue содержащая значения очереди для конкретного подписчика
     */
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> cMap = new ConcurrentHashMap<>();

    @Override
    public ServiceResponse process(MessageParser message) {
        if (message.httpRequestType().equals("POST")) {
            return processPostMethod(message);
        } else if (message.httpRequestType().equals("GET")) {
            return processGetMethod(message);
        }
        return new ServiceResponse("Invalid method name in message header", 400);
    }

    private ServiceResponse processPostMethod(MessageParser message) {
        /* готовим кМапу для добавляем в очередь */
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> subscribersMap = new ConcurrentHashMap<>();
        subscribersMap.put(message.getParam(), new ConcurrentLinkedQueue<>());
        /* если нет такой очереди то добавляем ее */
        cMap.putIfAbsent(message.getQueueName(), subscribersMap);
        /* добавляем полученные данные во все очереди подписок */
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> fss = cMap.get(message.getQueueName());
        for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry : fss.entrySet()) {
            entry.getValue().add(message.getParam());
        }
        return new ServiceResponse("Message posted at queue: " + message.getQueueName(), 200);
    }

    private ServiceResponse processGetMethod(MessageParser message) {
        String subscriberName = message.getParam();
        cMap.putIfAbsent(message.getQueueName(), new ConcurrentHashMap<>());
        /* получаем мапу для очереди */
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> mapForQueue = cMap.get(message.getQueueName());
        if (mapForQueue == null) {
            return new ServiceResponse("Queue not found", 404);
        } else {
            ConcurrentLinkedQueue<String> linkedQueue = mapForQueue.get(subscriberName);
            if (linkedQueue == null) {
                mapForQueue.put(subscriberName, new ConcurrentLinkedQueue<>());
                return new ServiceResponse("Queue for subscriber <"
                        + subscriberName
                        + "> in topic <"
                        + message.getQueueName()
                        + "> has been created", 200);
            } else {
                String paramValue = mapForQueue.get(message.getParam()).poll();
                if (paramValue == null) {
                    return new ServiceResponse("", 400);
                } else {
                    return new ServiceResponse(paramValue, 200);
                }
            }
        }
    }
}