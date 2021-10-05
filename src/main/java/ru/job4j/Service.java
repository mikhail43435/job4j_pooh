package ru.job4j;

public interface Service {
    ServerResponse process(MessageParser messageParser);
}