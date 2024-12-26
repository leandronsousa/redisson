package com.test.redisson.config;

import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.redisson.api.RemoteInvocationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonService {

    @Autowired
    private RedissonClient client;

    public <T> T getService(Class<T> remoteInterface, int timeout) {
        RemoteInvocationOptions options = RemoteInvocationOptions.defaults()
                .noAck()
                .expectResultWithin(timeout, TimeUnit.MILLISECONDS);

        RRemoteService remoteService = client.getRemoteService(remoteInterface.getCanonicalName());

        return remoteService.get(remoteInterface, options);
    }

    public <T> void registerService(Class<T> remoteInterface, T service, int workers) {
        RRemoteService remoteService = client.getRemoteService(remoteInterface.getCanonicalName());
        remoteService.register(remoteInterface, service, workers);
    }

}
