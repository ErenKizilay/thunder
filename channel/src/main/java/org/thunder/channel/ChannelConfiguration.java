package org.thunder.channel;

import org.thunder.function.ProcessingFunction;

public class ChannelConfiguration {

    private int initialListenerThreadCount;

    private int initialWorkerThreadCount;

    private ProcessingFunction<?, ?> function;

    private String name;

    public int getInitialListenerThreadCount() {
        return initialListenerThreadCount;
    }

    public ChannelConfiguration setInitialListenerThreadCount(int initialListenerThreadCount) {
        this.initialListenerThreadCount = initialListenerThreadCount;
        return this;
    }

    public int getInitialWorkerThreadCount() {
        return initialWorkerThreadCount;
    }

    public ChannelConfiguration setInitialWorkerThreadCount(int initialWorkerThreadCount) {
        this.initialWorkerThreadCount = initialWorkerThreadCount;
        return this;
    }

    public ProcessingFunction<Object, Object> getFunction() {
        return (ProcessingFunction<Object, Object>) function;
    }

    public ChannelConfiguration setFunction(ProcessingFunction<?, ?> function) {
        this.function = function;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChannelConfiguration setName(String name) {
        this.name = name;
        return this;
    }
}
