package ru.job4j;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MessageParserTest {

    @Test
    public void whenPostMethod() {
        var content = "POST /topic/weather HTTP/1.1" + System.lineSeparator()
                + "Host: localhost:9000" + System.lineSeparator()
                + "User-Agent: curl/7.67.0" + System.lineSeparator()
                + "Accept: */*" + System.lineSeparator()
                + "Content-Length: 7" + System.lineSeparator()
                + "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator()
                + "text=13";
        MessageParser message = MessageParser.of(content);
        assertThat(message.getMethodName(), is("POST"));
        assertThat(message.getMode(), is("topic"));
        assertThat(message.getQueueName(), is("weather"));
        assertThat(message.getSingleParam(), is("13"));
    }

    @Test
    public void whenGetMethod() {
        var content = "GET /queue/weather HTTP/1.1" + System.lineSeparator()
                + "Host: localhost:9000" + System.lineSeparator()
                + "User-Agent: curl/7.67.0" + System.lineSeparator()
                + "Accept: */*" + System.lineSeparator()
                + "userId=1";
        var message = MessageParser.of(content);
        assertThat(message.getMethodName(), is("GET"));
        assertThat(message.getMode(), is("queue"));
        assertThat(message.getQueueName(), is("weather"));
        assertThat(message.getSingleParam(), is("1"));
    }
}