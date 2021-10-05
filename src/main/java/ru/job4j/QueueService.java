package ru.job4j;

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
    @Override
    public ServerResponse process(MessageParser messageParser) {
        return null;
    }
}
