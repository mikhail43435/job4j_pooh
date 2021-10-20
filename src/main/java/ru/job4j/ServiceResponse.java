package ru.job4j;

/**
 * Класс ответа от сервиса
 */

public class ServiceResponse {
    private final String text;
    private final int status;

    /**
     * Конструктор ответа
     * @param text - текст ответа
     * @param status - цифровой статус ответа (HTTP response status codes)
     */
    public ServiceResponse(String text, int status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }
}