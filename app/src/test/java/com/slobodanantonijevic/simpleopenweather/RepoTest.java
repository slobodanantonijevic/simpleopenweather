package com.slobodanantonijevic.simpleopenweather;

import org.junit.Before;

import io.reactivex.schedulers.TestScheduler;

public class RepoTest {

    protected TestScheduler testScheduler;

    @Before
    public void init() {

        // Mock scheduler using RxJava TestScheduler.
        testScheduler = new TestScheduler();
    }
}
