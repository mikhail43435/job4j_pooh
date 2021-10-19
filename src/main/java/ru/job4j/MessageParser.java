package ru.job4j;

/**
 * Req - класс, служить для парсинга входящего сообщения.
 * httpRequestType - GET или POST. Он указывает на тип запроса.
 * poohMode - указывает на режим работы: queue или topic.
 * sourceName - имя очереди.
 * params - содержимое запроса.
 *
 * Структура HTTP-сообщения
 * Каждое HTTP-сообщение состоит из трёх частей, которые передаются в указанном порядке:
 * Стартовая строка (англ. Starting line) — определяет тип сообщения;
 * Заголовки (англ. Headers) — характеризуют тело сообщения, параметры передачи и прочие сведения;
 * Тело сообщения (англ. Message Body) — непосредственно данные сообщения.
 * Обязательно должно отделяться от заголовков пустой строкой.
 * Тело сообщения может отсутствовать, но стартовая строка и заголовок являются обязательными элементами.
 */

public class MessageParser {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public MessageParser(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static MessageParser of(String content) {
        String[] contentArray = content.split(System.lineSeparator());
        String[] startingLine = contentArray[0].replace("/", " ").split(" ");
        String httpRequestType = startingLine[0];
        String poohMode = startingLine[2];
        String sourceName = startingLine[3];
        String param = "";
        if (httpRequestType.equals("POST")) {
            param = contentArray[contentArray.length - 1];
        } else if (httpRequestType.equals("GET")) {
            param = poohMode.equals("queue") ? "" : startingLine[4];
        } else {
            throw new IllegalArgumentException("Illegal request type");
        }
        return new MessageParser(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getQueueName() {
        return sourceName;
    }

    public String getParam() {
        return this.param;
    }
}