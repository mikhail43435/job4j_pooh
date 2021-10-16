package ru.job4j;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        var queueService = new QueueService();
        Map<String, String> params = new HashMap<>();
        params.put("temperature", "18");
        queueService.process(new MessageParser("POST", "queue", "weather", params));
        var result = queueService.process(new MessageParser("GET", "queue", "weather", params));
        assertThat(result.getText(), is("18"));
    }
}