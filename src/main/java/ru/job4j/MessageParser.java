package ru.job4j;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, String> params;

    public MessageParser(String httpRequestType, String poohMode, String sourceName, Map<String, String> params) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.params = params;
    }

    public static MessageParser of(String content) {
        String[] contentArray = content.split(System.lineSeparator());
        Map<String, String> paramsMap = new HashMap<>();
        String[] startingLine = contentArray[0].replace("/", " ").split(" ");
        String httpRequestType = startingLine[0];
        String poohMode = startingLine[2];
        String sourceName = startingLine[3];
        int lineCounter = 1;
        while (lineCounter < contentArray.length && !contentArray[lineCounter].trim().equals("")) {
            lineCounter++;
        }
        lineCounter++;
        while (lineCounter < contentArray.length && !contentArray[lineCounter].trim().equals("")) {
            String[] paramArray = contentArray[lineCounter++].split("=");
            if (paramArray.length >= 2) {
                paramsMap.put(paramArray[0], paramArray[1]);
            }
        }
        return new MessageParser(httpRequestType, poohMode, sourceName, paramsMap);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getFirstParam() {
        return params.size() > 0 ? params.entrySet().iterator().next().getValue() : "";
    }

    public String getParamForKey(String key) {
        return params.get(key);
    }
}