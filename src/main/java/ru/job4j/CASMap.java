package ru.job4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CASMap {
    public static void main(String[] args) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
        String name = "weather";
        queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
        queue.get(name).add("value");
    }
}