package ru.job4j;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        ServerResponse result = topicService.process(
                new MessageParser("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new MessageParser("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        ServerResponse result1 = topicService.process(
                new MessageParser("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        ServerResponse result2 = topicService.process(
                new MessageParser("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result.getText(), is("Queue for subscriber <client407> in topic <weather> has been created"));
        assertThat(result.getStatus(), is(200));
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result1.getStatus(), is(200));
        assertThat(result2.getText(), is("Queue for subscriber <client6565> in topic <weather> has been created"));
        assertThat(result2.getStatus(), is(200));
    }

    @Test
    public void whenTopicTwoTopics() {
        TopicService topicService = new TopicService();
        String topicName1 = "weather";
        String topicName2 = "water";
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        ServerResponse result = topicService.process(
                new MessageParser("GET", "topic", topicName1, paramForSubscriber1)
        );
        System.out.println(result.getText());
        topicService.process(
                new MessageParser("POST", "topic", "weather", paramForPublisher)
        );
        ServerResponse result1 = topicService.process(
                new MessageParser("GET", "topic", topicName1, paramForSubscriber1)
        );
        ServerResponse result2 = topicService.process(
                new MessageParser("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result.getText(), is("Queue for subscriber <client407> in topic <weather> has been created"));
        assertThat(result.getStatus(), is(200));
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result1.getStatus(), is(200));
        assertThat(result2.getText(), is("Queue for subscriber <client6565> in topic <weather> has been created"));
        assertThat(result2.getStatus(), is(200));
    }


    @Test
    public void whenEmptySubscriberQueue() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        /* Режим topic. Подписываемся на топик weather. client407. */
        ServerResponse result = topicService.process(
                new MessageParser("GET",
                        "topic",
                        "weather",
                        paramForSubscriber1)
        );
        topicService.process(
                new MessageParser("POST",
                        "topic",
                        "weather",
                        paramForPublisher)
        );
        ServerResponse result1 = topicService.process(
                new MessageParser("GET",
                        "topic",
                        "weather",
                        paramForSubscriber1)
        );
        ServerResponse result2 = topicService.process(
                new MessageParser("GET",
                        "topic",
                        "weather",
                        paramForSubscriber1)
        );
        assertThat(result.getText(), is("Queue for subscriber <client407> in topic <weather> has been created"));
        assertThat(result.getStatus(), is(200));
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result1.getStatus(), is(200));
        assertThat(result2.getText(), is(""));
        assertThat(result2.getStatus(), is(400));
    }

}