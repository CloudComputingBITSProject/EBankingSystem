package com.example.loadbalancer.service.autoscaler;

import com.github.dockerjava.api.model.Container;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

public class TimeSeriesAutoScaler implements AutoScaler{
    List<Container> containerList;
    Timer timer;
    public TimeSeriesAutoScaler(List<Container> containerList){
        System.out.println("TimeSeriesAutoScaler Implementation");
        this.containerList = containerList;
        start();
    }
    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask2(), 0, 10000);

//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // Schedule the task to run every 10 seconds
//        scheduler.scheduleAtFixedRate(new Strategy2(), 0, 10, TimeUnit.SECONDS);
    }
}
class Strategy2 implements Runnable{

    @Override
    public void run() {
        // Your task logic goes here
        System.out.println("Autoscaler Task Initiated in :  " + System.currentTimeMillis());
    }
}

class MyTimerTask2 extends TimerTask {
    @Override
    public void run() {
        // Your task logic goes here
        System.out.println("Autoscaler Task Initiated in : " + System.currentTimeMillis());
    }
}

