package ru.job4j;

public interface Service {
    ServiceResponse process(MessageParser messageParser);
}