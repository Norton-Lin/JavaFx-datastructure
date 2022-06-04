package com.example.demo1.Code.clock;

/**
 * 闹钟线程
 */
public class ClockThread implements Runnable {

    public EventClock eventclock;

    public ClockThread(EventClock eventclock) {
        this.eventclock = eventclock;
    }

    @Override
    public void run() {

        Clock t = new Clock(this.eventclock);

    }
}
