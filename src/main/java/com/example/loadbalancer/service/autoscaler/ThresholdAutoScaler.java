package com.example.loadbalancer.service.autoscaler;

import com.github.dockerjava.api.model.Container;
import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;

public class ThresholdAutoScaler implements AutoScaler{
    List<Container> containerList;
    Timer timer;


    public ThresholdAutoScaler(List<Container> containerList){
        System.out.println("ThresholdAutoscaler Implementation");
        this.containerList = containerList;
        start();
    }
    public void start() {

//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 0, 10000);
        // Schedule the task to run every 10 seconds
//        scheduler.scheduleAtFixedRate(new Strategy1(), 0, 10, TimeUnit.SECONDS);
    }
}
//class Strategy1 implements Runnable{
//    @Override
//    public void run() {
//        // Your task logic goes here
//        System.out.println("Autoscaler Task Initiated in : " + System.currentTimeMillis());
//    }
//}

class MyTimerTask extends TimerTask {
    @Override
    public void run() {
        // Your task logic goes here
        System.out.println("Autoscaler Task Initiated in : " + System.currentTimeMillis());
    }
}
