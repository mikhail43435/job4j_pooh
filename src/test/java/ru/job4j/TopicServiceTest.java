package ru.job4j;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {
    @Test
    public void whenPostThenGetTopic() {
        var topicService = new TopicService();
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        params.put("temperature", "18");
        /* Добавляем данные в очередь weather. Режим topic */
        topicService.process(new MessageParser("POST", "topic", "weather", params));
        /* Забираем данные из очереди weather. Режим topic */
        ServerResponse result = topicService.process(new MessageParser("GET", "topic", "weather", params));
        assertThat(result.text(), is("18"));
    }
}