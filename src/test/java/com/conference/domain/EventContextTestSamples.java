package com.conference.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventContextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventContext getEventContextSample1() {
        return new EventContext().id(1L).description("description1");
    }

    public static EventContext getEventContextSample2() {
        return new EventContext().id(2L).description("description2");
    }

    public static EventContext getEventContextRandomSampleGenerator() {
        return new EventContext().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
