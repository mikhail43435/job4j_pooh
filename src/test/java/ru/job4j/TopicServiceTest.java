package ru.job4j;

import junit.framework.TestCase;

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
        topicService.process(new Req("POST", "topic", "weather", params));
        /* Забираем данные из очереди weather. Режим topic */
        var result = topicService.process(new Req("GET", "topic", "weather", params));
        assertThat(result.text(), is("18"));
    }
}