package com.example.loadbalancer.service;

import com.example.loadbalancer.service.autoscaler.*;
import com.example.loadbalancer.service.loadbalancer.*;
import com.github.dockerjava.api.model.Container;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Component
@Getter
@Setter
public class User {
    private String username;
    private DockerAgent dockerAgent;
    private Map<String, LoadBalancer> loadBalancerMap;
    private Map<String, AutoScaler> autoScalerMap;
    private Map<String, List<Container>> serviceContainerMap;
    User(String username){
        this.username = username;
        this.dockerAgent = new DockerAgent();
        this.serviceContainerMap = new HashMap<>();
        this.loadBalancerMap = new HashMap<>();
        this.autoScalerMap = new HashMap<>();
//        this.loadBalancer = null; //TODO Set Default
//        this.autoScaler= null;//TODO Set Default
    }

    public LoadBalancer setLoadBalancerStrategy(String strategy, String serviceName, List<Integer> weights) {
        LoadBalancer loadBalancer;
        switch (strategy){
            case("weightedRoundRobin") :
                loadBalancer = new WeightedRoundRobinLoadBalancer(this.serviceContainerMap.get(serviceName),weights);
                break;
            case("random") :
                loadBalancer = new RandomLoadBalancer(this.serviceContainerMap.get(serviceName));
                break;
            case("weightedLeastConnection") :
                loadBalancer = new WeightedLeastConnectionLoadBalancer(this.serviceContainerMap.get(serviceName),weights);
                break;
            case("ipHash") :
                loadBalancer = new IpHashLoadBalancer(this.serviceContainerMap.get(serviceName));
                break;
            case ("powerOfTwoChoices"):
                loadBalancer = new PowerOfTwoChoicesLoadBalancer(this.serviceContainerMap.get(serviceName));
                break;
            default:
                loadBalancer = new WeightedRoundRobinLoadBalancer(this.serviceContainerMap.get(serviceName),weights);
                break;
        }
        loadBalancerMap.put(serviceName,loadBalancer);
        return loadBalancer;
    }

    public AutoScaler setAutoScalerStrategy(String strategy,String serviceName) {
        AutoScaler autoScaler;
        switch (strategy){
            case("threshold") :
                autoScaler = new ThresholdAutoScaler(this.serviceContainerMap.get(serviceName));
                break;
            case("timeseries") :
                autoScaler = new TimeSeriesAutoScaler(this.serviceContainerMap.get(serviceName));
                break;
            default:
                autoScaler = new ThresholdAutoScaler(this.serviceContainerMap.get(serviceName));
                break;
            }
        autoScalerMap.put(serviceName,autoScaler);
        return autoScaler;
    }
}
