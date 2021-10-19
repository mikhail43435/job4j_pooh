package ru.job4j;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        queueService.process(
                new MessageParser("POST",
                        "queue",
                        "weather",
                        "temperature=18")
        );
        ServerResponse result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null)
        );
        assertThat(result.getText(), is("temperature=18"));
        assertThat(result.getStatus(), is(200));
    }

    @Test
    public void whenInvalidMode() {
        QueueService queueService = new QueueService();
        ServerResponse result = queueService.process(
                new MessageParser("POST",
                        "topic",
                        "weather",
                        "temperature=18")
        );
        assertThat(result.getText(), is("Invalid mode type"));
        assertThat(result.getStatus(), is(400));
    }

    @Test
    public void whenPostThenGetThenQueueIsEmpty() {
        QueueService queueService = new QueueService();
        queueService.process(
                new MessageParser("POST",
                        "queue",
                        "weather",
                        "temperature=18")
        );
        ServerResponse result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null)
        );
        assertThat(result.getText(), is("temperature=18"));
        assertThat(result.getStatus(), is(200));
        result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null)
        );
        assertThat(result.getText(), is("Queue is empty"));
        assertThat(result.getStatus(), is(400));
    }

    @Test
    public void whenNoQueue() {
        QueueService queueService = new QueueService();
        ServerResponse result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null)
        );
        assertThat(result.getText(), is("Queue not found"));
        assertThat(result.getStatus(), is(404));
    }

    @Test
    public void whenPostThenGetQueueTwoQueues() {
        QueueService queueService = new QueueService();
        queueService.process(new MessageParser("POST",
                        "queue",
                        "weather",
                        "temperature=18"));
        queueService.process(new MessageParser("POST",
                        "queue",
                        "weather",
                        "temperature=19"));
        queueService.process(new MessageParser("POST",
                        "queue",
                        "water",
                        "temperature=10"));
        queueService.process(new MessageParser("POST",
                        "queue",
                        "weather",
                        "temperature=20"));
        ServerResponse result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null));
        assertThat(result.getText(), is("temperature=18"));
        assertThat(result.getStatus(), is(200));
        result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "water",
                        null));
        assertThat(result.getText(), is("temperature=10"));
        assertThat(result.getStatus(), is(200));
        result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null));
        assertThat(result.getText(), is("temperature=19"));
        assertThat(result.getStatus(), is(200));
        result = queueService.process(
                new MessageParser("GET",
                        "queue",
                        "weather",
                        null));
        assertThat(result.getText(), is("temperature=20"));
        assertThat(result.getStatus(), is(200));
    }
}