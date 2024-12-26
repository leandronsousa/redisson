package com.test.redisson.produce;

import com.test.redisson.config.RedissonService;
import com.test.redisson.consume.ITaskService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskProducer {

    private static final int TIMEOUT = 25_000;

    private static final Logger LOG = LoggerFactory.getLogger(TaskProducer.class);

    @Autowired
    private ThreadPoolFactory threadPoolFactory;

    @Autowired
    private RedissonService redissonHelper;

    private ITaskService service;

    private AtomicInteger id = new AtomicInteger(1);

    @PostConstruct
    private void init() {
        service = redissonHelper.getService(ITaskService.class, TIMEOUT);
    }

    public void execute() {

        try {

            SingleAsyncTask asyncRun = new SingleAsyncTask(service, id);

            threadPoolFactory.getExecutor().submit(asyncRun);

        } catch (Exception ex) {
            LOG.warn("Single Async task busy");
        }

    }

}
