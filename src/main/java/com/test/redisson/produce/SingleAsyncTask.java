package com.test.redisson.produce;

import com.test.redisson.consume.ITaskService;
import org.redisson.remote.RemoteServiceTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

public class SingleAsyncTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SingleAsyncTask.class);

    private ITaskService service;

    private AtomicInteger id;

    public SingleAsyncTask(ITaskService service, AtomicInteger id) {
        this.service = service;
        this.id = id;
    }

    @Override
    public void run() {

        int currId = id.getAndIncrement();

        LOG.info("{} calling consumer", id);

        try {

            int count = service.execute(currId);

            LOG.info("{} returned from consumer, count: {} ", currId, count);

        } catch (RemoteServiceTimeoutException ex) {
            LOG.error(format("%s returned with error [RemoteServiceTimeoutException]", id));
        }

    }
}
