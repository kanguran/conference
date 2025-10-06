package com.conference.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventRegistrationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventRegistration getEventRegistrationSample1() {
        return new EventRegistration().id(1L).description("description1");
    }

    public static EventRegistration getEventRegistrationSample2() {
        return new EventRegistration().id(2L).description("description2");
    }

    public static EventRegistration getEventRegistrationRandomSampleGenerator() {
        return new EventRegistration().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
