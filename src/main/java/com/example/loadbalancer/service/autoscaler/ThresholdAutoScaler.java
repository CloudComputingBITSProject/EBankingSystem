package com.example.loadbalancer.service.autoscaler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AutoScalerImpl1 implements AutoScaler{

    public AutoScalerImpl1(){
        System.out.println("AutoScalerImpl1 Constructor");
    }
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(new Strategy1(), 0, 10, TimeUnit.SECONDS);
    }
}
class Strategy1 implements Runnable{

    @Override
    public void run() {
        // Your task logic goes here
        System.out.println("Task performed at: " + System.currentTimeMillis());
    }
}
