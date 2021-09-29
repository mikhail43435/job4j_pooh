package ru.job4j;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ReqTest {

    @Test
    public void whenPostMethod() {
        var content = """
                POST /topic/weather HTTP/1.1
                Host: localhost:9000
                User-Agent: curl/7.67.0
                Accept: */*
                Content-Length: 7
                Content-Type: application/x-www-form-urlencoded
                text=13
                """;
        var req = Req.of(content);
        assertThat(req.method(), is("POST"));
        assertThat(req.mode(), is("topic"));
        assertThat(req.queue(), is("weather"));
        assertThat(req.param("text"), is("13"));
    }

    @Test
    public void whenGetMethod() {
        var content = """
                GET /queue/weather HTTP/1.1
                       Host: localhost:9000
                       User-Agent: curl/7.67.0
                       Accept: */*

                userId=1
                """;
        var req = Req.of(content);
        assertThat(req.method(), is("POST"));
        assertThat(req.mode(), is("queue"));
        assertThat(req.queue(), is("weather"));
        assertThat(req.param("userId"), is("1"));
    }
}