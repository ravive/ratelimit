package com.raviv;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.log4j.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * rate limiter class allow define number of request allow in x window of seconds
 */
public class RateLimiter {

    final static Logger logger = Logger.getLogger(RateLimiter.class);
    private Cache<String, AtomicInteger> cache;

    private Integer requests;
    private long intervalInSeconds;

    public RateLimiter(long intervalInSeconds,Integer requests) {
        this.requests = requests;
        this.intervalInSeconds = intervalInSeconds;
        cache = CacheBuilder.newBuilder().expireAfterWrite(intervalInSeconds, TimeUnit.SECONDS).build();
    }


    /**
     *
     * @param clientId - the id of the client made the request
     * @param units - number of units to consume
     * @return if the client allow to consume more request in the given policy
     * @throws ExecutionException
     */
    public boolean consume(String clientId,int units) throws ExecutionException {

        AtomicInteger value = cache.get(clientId, AtomicInteger::new);
        int currentState = value.addAndGet(units);
        boolean canConsume = currentState <= this.requests;
        logger.debug("consume client "+clientId+" request "+canConsume+" with number of requests "+currentState);
        return canConsume;
    }

    @Override
    public String toString() {
        return "RateLimiter windows is "+this.intervalInSeconds+" for "+this.requests+" requests";

    }
}
