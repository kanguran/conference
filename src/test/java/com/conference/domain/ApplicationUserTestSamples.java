package com.conference.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser().id(1L);
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser().id(2L);
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser().id(longCount.incrementAndGet());
    }
}
