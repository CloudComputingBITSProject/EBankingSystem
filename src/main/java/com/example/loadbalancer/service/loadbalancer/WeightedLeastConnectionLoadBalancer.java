package com.example.loadbalancer.service.loadbalancer;

import com.example.loadbalancer.service.loadbalancer.LoadBalancer;
import com.github.dockerjava.api.model.Container;

import java.util.Arrays;
import java.util.List;

public class WeightedLeastConnectionLoadBalancer implements LoadBalancer {
    List<Container> containerList;
    public WeightedLeastConnectionLoadBalancer(List<Container> containerList, List<Integer> weights){
        System.out.println("WeightedLeastConnectionLoadBalancer LoadBalancer Implementation");
        this.containerList = containerList;
    }

    public int nextContainerPort(String ipAddress){
        int port = 9090;
        int n = containerList.size();
        int randomNo = (int)(Math.random()*n);
        Container container = containerList.get(randomNo);
        try{
            port = container.getPorts()[0].getPublicPort();
            System.out.printf("Sending Request: Container: %s \t Port: %d \n", Arrays.toString(container.getNames()),port);
            return port;
        }
        catch (Exception e){
            System.out.printf("Could not send Request: Container: %s \t Port: %d \n", Arrays.toString(container.getNames()),port);
            System.out.println("Reason: "+e.getMessage());
        }
        return port;
    }
}
