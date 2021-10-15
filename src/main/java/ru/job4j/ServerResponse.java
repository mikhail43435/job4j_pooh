package ru.job4j;

/**
 * Resp - ответ от сервиса
 */
public class ServerResponse {
    private final String text;
    private final int status;

    public ServerResponse(String text, int status) {
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