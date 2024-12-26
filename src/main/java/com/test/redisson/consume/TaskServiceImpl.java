package com.test.redisson.consume;

import com.test.redisson.config.RedissonService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskServiceImpl implements ITaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final int WORKERS = 10;

    @Autowired
    private RedissonService redissonHelper;

    private AtomicInteger count = new AtomicInteger(1);

    @PostConstruct
    private void init() {
        redissonHelper.registerService(ITaskService.class, this, WORKERS);
    }

    @Override
    @Transactional(timeout = 30, propagation = Propagation.REQUIRES_NEW)
    public int execute(int id) {

        LOG.info("{} executing consumer",  id);

        sleep();

        int c = count.getAndIncrement();

        LOG.info("{} returning consumer, count: {}",  id, c);

        return c;
    }

    private void sleep() {
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
