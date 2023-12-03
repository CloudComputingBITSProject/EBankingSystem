package com.example.loadbalancer.service.autoscaler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoScalerImpl2 implements AutoScaler{

    public AutoScalerImpl2(){
        System.out.println("AutoScalerImpl2 Constructor");
    }
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(new Strategy2(), 0, 10, TimeUnit.SECONDS);
    }
}
class Strategy2 implements Runnable{

    @Override
    public void run() {
        // Your task logic goes here
        System.out.println("Task performed at: " + System.currentTimeMillis());
    }
}
