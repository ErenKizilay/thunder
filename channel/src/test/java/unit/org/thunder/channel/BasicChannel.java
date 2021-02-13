package org.thunder.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class BasicChannel extends Channel<String, ChannelConfiguration> {

    private ObjectMapper objectMapper;

    public BasicChannel(ChannelConfiguration channelConfiguration) {
        super(channelConfiguration);
    }

    @Override
    public String listen() {
        try {
            String event = objectMapper.writeValueAsString(randomMap());
            logger.info("hooked event: " + event);
            return event;
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        objectMapper = new ObjectMapper();
    }

    @Override
    public Object convert(String event) {
        try {
            Thread.sleep(500);
            return objectMapper.readerFor(Map.class).readValue(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Map<String, Object> randomMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphabetic(10));
        map.put(RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphabetic(10));
        return map;
    }
}
