package org.thunder.channel;


import org.junit.Before;
import org.junit.Test;
import org.thunder.core.Context;
import org.thunder.core.ProcessingResult;
import org.thunder.function.ProcessingFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ChannelTest {

    private ChannelConfiguration channelConfiguration;

    private static List<Object> results = Collections.synchronizedList(new ArrayList<>());


    @Before
    public void setUp() throws Exception {
        channelConfiguration = new ChannelConfiguration()
                .setInitialListenerThreadCount(1)
                .setInitialWorkerThreadCount(1)
                .setName("hello")
                .setFunction(new TestFunction());
    }

    @Test
    public void name() throws InterruptedException {
        org.thunder.channel.BasicChannel basicChannel = new org.thunder.channel.BasicChannel(channelConfiguration);
        Thread.sleep(1000);
        basicChannel.stop();
        System.out.println(results);
    }

    static class TestFunction implements ProcessingFunction<Map<String, Object>, Void> {

        @Override
        public Class<Map<String, Object>> getParameterType() {
            Map<String, Object> map = new HashMap<>();
            return (Class<Map<String, Object>>) map.getClass();
        }

        @Override
        public ProcessingResult<Void> apply(Map<String, Object> stringObjectMap, Context context) {
            System.out.println("applying: " +  stringObjectMap);
            results.add(stringObjectMap.toString());
            return null;
        }
    }
}