package com.example.loadbalancer.service;//package com.loadbalancer;

//import com.github.dockerjava.api.DockerClient;
//import com.github.dockerjava.api.model.Container;
//import com.github.dockerjava.api.model.Image;
//import com.github.dockerjava.core.DefaultDockerClientConfig;
//import com.github.dockerjava.core.DockerClientConfig;
//import com.github.dockerjava.core.DockerClientImpl;
//import com.github.dockerjava.transport.DockerHttpClient;

import java.time.Duration;
import java.util.*;

import com.github.dockerjava.api.DockerClient;

import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
//import com.github.dockerjava.core.command.StartContainerResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DockerClientBuilder;


import javax.persistence.Entity;
import java.io.File;
import java.util.List;

@Getter
@Setter
@Service
public class DockerAgent {
    DockerClient dockerClient;
    Map<String,List<Container>> serviceContainerMap = new HashMap<>();
    public DockerAgent() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        dockerClient = DockerClientImpl.getInstance(config, httpClient);String networkName = "mynetwork-net";
        CreateNetworkResponse networkResponse = null;

        try{
            Network existingNetwork = dockerClient.inspectNetworkCmd().withNetworkId(networkName).exec();
            System.out.println("Network already exists: " + existingNetwork.getId());
        }
        catch (Exception e){
            System.out.println("Network does not exist");
            networkResponse = dockerClient.createNetworkCmd().withName(networkName).exec();
            System.out.println("Network created: " + networkResponse.getId());
        }
    }
    public static void runContainers(DockerClient dockerClient,List<String> listOfContainerID){
        for (String containerID : listOfContainerID) {
            dockerClient.startContainerCmd(containerID).exec();
            System.out.println("Container started with ID: " + containerID);
        }
    }
    public static List<String> createMultipleContainer(DockerClient dockerClient,int numberOfContainers,int port,String serviceName){
        ExposedPort exposedPort = ExposedPort.tcp(port);
        List<String> listOfContainerID = new ArrayList<>();
        for (int i = 0; i < numberOfContainers; i++) {
            String containerName = serviceName +"-container-" + (i+1);
            try {
                listOfContainerID.add(createContainer(dockerClient, exposedPort, containerName, serviceName));
            }catch (ConflictException e){
                List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
                String containerId = findContainerIdByName(containers, containerName);
                System.out.println(containerId);
                dockerClient.removeContainerCmd(containerId).exec(); //TODO Modidy
                listOfContainerID.add(createContainer(dockerClient, exposedPort, containerName, serviceName));
            }
        }
        System.out.printf("%d instances of container increased successfully: \n\n",numberOfContainers);
        for (int i = 0; i < numberOfContainers; i++) {
            System.out.printf("Container running with ID: %s\n",listOfContainerID.get(i));
        }
        System.out.println("Starting the containers one by one");
        runContainers(dockerClient,listOfContainerID);
        return listOfContainerID;
    }
    public static void buildAllImages(DockerClient dockerClient){
        List<Image> images = listAllImages(dockerClient);
        for(int i=1;i<=1;i++){
            String imageName = "service-"+i;
            boolean imageExists = images.stream().anyMatch(image -> image.getRepoTags()!=null && Arrays.asList(image.getRepoTags()).contains(imageName));
            if(!imageExists){
                System.out.println("Building image"+i);
                String imageId = buildImage(dockerClient,"/home/ayush/Cloud Project/LoadBalancer/EBankingSystems/Dockerfile","service-"+i);
                System.out.println("Built image"+i +"with ID: " + imageId);
            }
            else {
                System.out.println("Image with name: service-"+i+"already exists");
            }
        }
    }
    private static String findContainerIdByName(List<Container> containers, String containerName) {
        for (Container container : containers) {
            for (String name : container.getNames()) {
                // Check if the container name matches
                if (name.equals("/" + containerName)) {
                    return container.getId();
                }
            }
        }
        return null; // Container not found with the specified name
    }
    public static String createContainer(DockerClient dockerClient,ExposedPort exposedPort,String containerName,String service_tag){
        Ports portBindings = new Ports();
        portBindings.bind(exposedPort, Ports.Binding.bindPort(0));

        CreateContainerResponse container
                = dockerClient.createContainerCmd(service_tag +":latest")
                .withName(containerName)
                .withHostName("ayush")
                .withEnv("spring.datasource.url=jdbc:mysql://docker-mysql:3306/mysql?allowPublicKeyRetrieval=true")
                .withExposedPorts(exposedPort)
                .withPortBindings(portBindings)
                .withNetworkMode("mynetwork-net")
                .exec();
        System.out.printf("Container created with Name: %s \t ID: %s\n\n",containerName,container.getId());
        return container.getId();
    }
    public static void deleteContainers(DockerClient dockerClient,List<Container> listOfContainerID){
        for (Container container : listOfContainerID) {
            dockerClient.stopContainerCmd(container.getId()).exec();
            System.out.println("Container stopped with ID: " + container.getId());
//            dockerClient.killContainerCmd(container.getId()).exec();
//            System.out.println("Container killed with ID: " + container.getId());
        }
    }

    public static List<Container> listAllContainers(DockerClient dockerClient){
        List<Container> containers = dockerClient.listContainersCmd().exec();
        System.out.println("\nList of all currently Running Containers");

        for(Container container: containers){
            System.out.println(Arrays.toString(container.getNames()));
        }
        return containers;
    }
    public static List<Container> listAllContainersExited(DockerClient dockerClient){
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowSize(true)
                .withShowAll(true)
                .withStatusFilter(Collections.singleton("exited")).exec();
        System.out.println("\nList of all Exited and Running Containers");
        for(Container container: containers){
            System.out.println(Arrays.toString(container.getNames()));
            System.out.println(container.getImageId());
            System.out.println();
        }
        return containers;
    }

    public static List<Image> listAllImages(DockerClient dockerClient){
        List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
        for(Image image: images){
            System.out.println(image.getId());
        }
        return images;
    }
    public static String buildImage(DockerClient dockerClient,String dockerFilePath,String service_tag){
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File(dockerFilePath))
                .withPull(true)
                .withNoCache(true)
                .withTag(service_tag+":latest")
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        return imageId;
    }

    public static void main(String[] args) {
//        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
//        List<Image> images = dockerClient.listImagesCmd().exec();
//
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config,httpClient);



//        System.out.println("Fetching list of all running Containers");
//        listAllContainersExited(dockerClient);

//        System.out.println("Fetching list of all Containers");
//        listAllContainersExited(dockerClient);
//        System.out.println("Building image");
//        buildImage(dockerClient,"/home/ayush/Cloud Project/LoadBalancer/EBankingSystems/Dockerfile","service-1");

//        System.out.println("Deleting all containers");
//        deleteContainers(dockerClient,listAllContainersExited(dockerClient));

//        dockerClient.killContainerCmd("b93bdec6bb05d9e4fc93527e7490003d4cd246402dbc2d5f3e26fa265aa34167").exec();

        System.out.println("Building all images");
        buildAllImages(dockerClient);


//        System.out.println("Starting MySQL Server");
//        StartMySQLContainer startMySQLContainer = new StartMySQLContainer(dockerClient);
//        startMySQLContainer.run();


        System.out.println("Creating multiple Container on the built image");
        List<String> containerId = createMultipleContainer(dockerClient,1,8080,"service-1");


        System.out.println("Fetching list of all running Containers");
        listAllContainers(dockerClient);


//        System.out.println("Fetching list of all running Containers");
//        listAllContainers(dockerClient);


//        InspectContainerResponse inspectContainer
//                = dockerClient.inspectContainerCmd(container.getId()).exec();


//        String imageId = dockerClient.buildImageCmd()
//                .withDockerfile(new File(""))
//                .withPull(true)
//                .withNoCache(true)
//                .withTag("service-1:latest")
//                .exec(new BuildImageResultCallback())
//                .awaitImageId();


//        List<Container> containers = dockerClient.listContainersCmd()
//                .withShowSize(true)
//                .withShowAll(true)
//                .withStatusFilter(Collections.singleton("exited")).exec();


//        String imageId = dockerClient.buildImageCmd()
//                .withDockerfile(new File("./Dockerfile"))
//                .withPull(true)
//                .withNoCache(true)
//                .withTag("api_services")
//                .exec(new BuildImageResultCallback())
//                .awaitImageId();

    }
}
