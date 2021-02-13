package org.thunder.channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thunder.core.Context;
import org.thunder.core.ProcessingResult;
import org.thunder.function.ProcessingFunction;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public abstract class Channel<T, R extends ChannelConfiguration> {

    private final BlockingQueue<T> events;

    private final ProcessingFunction<Object, Object> function;

    private final ExecutorService workerExecutorService;

    private final ExecutorService listenerExecutorService;

    protected Logger logger;

    private final AtomicBoolean stop;

    private final R channelConfiguration;

    public Channel(R channelConfiguration) {
        this.channelConfiguration = channelConfiguration;
        logger = LogManager.getLogger(this);
        stop = new AtomicBoolean();
        stop.set(false);
        this.events = new LinkedBlockingQueue<>();
        this.function = channelConfiguration.getFunction();
        listenerExecutorService = buildListenerExecutorService(channelConfiguration);
        workerExecutorService = buildWorkerExecutorService(channelConfiguration);
        initialize();
        startProcessing();
    }

    public ExecutorService buildWorkerExecutorService(R channelConfiguration) {
        return Executors.newCachedThreadPool();
    }

    public ExecutorService buildListenerExecutorService(R channelConfiguration) {
        return Executors.newCachedThreadPool();
    }

    public abstract T listen();

    public abstract Object convert(T event);

    private void process(T message) {
        System.out.println("asdasdsa " + message);
        logger.info("processing: " + message);
        Object parameter = convert(message);
        Class<?> parameterType = function.getParameterType();
        if (parameterType.isAssignableFrom(parameter.getClass())) {
            logger.info("will apply!");
            ProcessingResult<?> processingResult = function.apply(parameter, new Context());
        }
    }

    private void listen(R channelConfiguration) {
        logger.info("asd");
        while (!stop.get()) {
            T event = listen();
            if (event != null) {
                events.add(event);
            }
        }
    }

    private void poll() {
        while (true) {
            T event = events.poll();
            logger.info("polledEvent: " + event);
            if (event != null) {
                process(event);
            }
        }
    }

    public void stop() {
        logger.info("Stopping.. Current job size: " + events.size());
        stop.set(true);
        waiting();
        listenerExecutorService.shutdown();
        waiting();
        List<Runnable> hookedEvents = listenerExecutorService.shutdownNow();
        workerExecutorService.shutdown();
        waiting();
        List<Runnable> unprocessedEvents = workerExecutorService.shutdownNow();
        logger.info("Stopped.. Current job size: " + events.size());
    }

    private void waiting() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void startProcessing() {
        IntStream.range(0, channelConfiguration.getInitialListenerThreadCount())
                .forEach(i -> listenerExecutorService.submit(() -> listen(channelConfiguration)));

        IntStream.range(0, channelConfiguration.getInitialWorkerThreadCount())
                .forEach(i -> workerExecutorService.submit(this::poll));
    }

    public void initialize() {

    }


}
