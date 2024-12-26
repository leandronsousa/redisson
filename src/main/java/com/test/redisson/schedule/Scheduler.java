package com.test.redisson.schedule;

import com.test.redisson.produce.TaskProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private TaskProducer producer;

    @Scheduled(fixedRate = 4000)
    public void task() {

        producer.execute();

    }

}
