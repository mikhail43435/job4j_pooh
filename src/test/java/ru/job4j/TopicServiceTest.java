package ru.job4j;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class TopicServiceTest {
    @Test
    public void whenPostThenGetTopic() {
        TopicService topicService = new TopicService();
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        params.put("temperature", "18");
        topicService.process(
                new MessageParser("POST", "topic", "weather", params));
        ServerResponse result = topicService.process(
                new MessageParser("GET", "topic", "weather", params));
        assertThat(result.getText(), is("18"));
    }

    @Test
    public void whenPostThenGetTopicTwice() {
        TopicService topicService = new TopicService();
        Map<String, String> paramsForPutAction = new HashMap<>();
        paramsForPutAction.put("userId", "1");
        paramsForPutAction.put("temperature", "18");
        topicService.process(
                new MessageParser("POST", "topic", "weather", paramsForPutAction));
        Map<String, String> paramsForGetAction = new HashMap<>();
        paramsForGetAction.put("userId", "1");
        ServerResponse firstResult = topicService.process(
                new MessageParser("GET", "topic", "weather", paramsForGetAction));
        assertThat(firstResult.getText(), is("18"));
        ServerResponse secondResult = topicService.process(
                new MessageParser("GET", "topic", "weather", paramsForGetAction));
        assertThat(secondResult.getText(), is(nullValue()));
    }

    @Test
    public void whenPostThenGetTopicForTwoUsers() {
        TopicService topicService = new TopicService();
        Map<String, String> paramsFirstUser = new HashMap<>();
        paramsFirstUser.put("userId", "1");
        paramsFirstUser.put("temperature", "18");
        Map<String, String> paramsSecondUser = new HashMap<>();
        paramsSecondUser.put("userId", "2");
        paramsSecondUser.put("temperature", "19");
        topicService.process(
                new MessageParser("POST", "topic", "weather", paramsFirstUser));
        topicService.process(
                new MessageParser("POST", "topic", "weather", paramsSecondUser));
        ServerResponse resultForFirstUser = topicService.process(
                new MessageParser("GET", "topic", "weather", paramsFirstUser));
        assertThat(resultForFirstUser.getText(), is("18"));
        ServerResponse resultForSecondUser = topicService.process(
                new MessageParser("GET", "topic", "weather", paramsSecondUser));
        assertThat(resultForSecondUser.getText(), is("19"));
    }
}