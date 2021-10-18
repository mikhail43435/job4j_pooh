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
                + "" + System.lineSeparator()
                + "text=13";
        MessageParser message = MessageParser.of(content);
        assertThat(message.httpRequestType(), is("POST"));
        assertThat(message.getPoohMode(), is("topic"));
        assertThat(message.getSourceName(), is("weather"));
        assertThat(message.getFirstParam(), is("13"));
    }

    @Test
    public void whenGetMethod() {
        var content = "GET /queue/weather HTTP/1.1" + System.lineSeparator()
                + "Host: localhost:9000" + System.lineSeparator()
                + "User-Agent: curl/7.67.0" + System.lineSeparator()
                + "Accept: */*" + System.lineSeparator()
                + "" + System.lineSeparator()
                + "userId=1";
        var message = MessageParser.of(content);
        assertThat(message.httpRequestType(), is("GET"));
        assertThat(message.getPoohMode(), is("queue"));
        assertThat(message.getSourceName(), is("weather"));
        assertThat(message.getFirstParam(), is("1"));
    }

    @Test
    public void whenQueueModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        MessageParser req = MessageParser.of(content);
        assertThat(req.httpRequestType(), is("POST"));
        assertThat(req.getPoohMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getFirstParam(), is("18"));
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        MessageParser req = MessageParser.of(content);
        assertThat(req.httpRequestType(), is("GET"));
        assertThat(req.getPoohMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getFirstParam(), is(""));
    }

    @Test
    public void whenTopicModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        MessageParser req = MessageParser.of(content);
        assertThat(req.httpRequestType(), is("POST"));
        assertThat(req.getPoohMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getFirstParam(), is("18"));
    }

    @Test
    public void whenTopicModePostMethodMulti() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature1=18" + ls +
                "temperature2=19";
        MessageParser req = MessageParser.of(content);
        assertThat(req.httpRequestType(), is("POST"));
        assertThat(req.getPoohMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParamForKey("temperature1"), is("18"));
        assertThat(req.getParamForKey("temperature2"), is("19"));
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls
                + "" + ls
                + "userId=client407" + ls;
        MessageParser req = MessageParser.of(content);
        assertThat(req.httpRequestType(), is("GET"));
        assertThat(req.getPoohMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getFirstParam(), is("client407"));
    }
}