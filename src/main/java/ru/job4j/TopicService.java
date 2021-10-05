package ru.job4j;

/**
 * Topic
 * Отправитель посылает сообщение с указанием темы.
 * Получатель читает первое сообщение и удаляет его из очереди.
 * Для каждого потребителя в режиме "topic" должна быть уникальная очередь потребления в отличии от режима "queue",
 * где очереди для всех клиентов одна и та же.
 * POST /topic/weather -d "temperature=18"
 * GET /topic/weather/1
 * topic указывает на режим темы.
 * weather имя темы, если темы нет, то нужно создать новую.
 * 1 - ID клиента.
 * Ответ temperature=18
 *  */
public class TopicService implements Service {
    @Override
    public ServerResponse process(MessageParser message) {
        return null;
    }
}