package com.example.loadbalancer.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Runner {
    DockerAgent dockerAgent;
    String currentUserEmail;
    String currentUserCompanyName;


    Runner() {
        this.currentUserEmail = null;
        this.currentUserCompanyName = null;
        this.dockerAgent = new DockerAgent();
        System.out.println("Starting MySQL Server");
        SQLAgent startMySQLContainer = new SQLAgent(dockerAgent.getDockerClient());
        startMySQLContainer.run();
    }

    public static void main(String[] args) throws IOException {
        Runner runner = new Runner();
        Scanner sc = new Scanner(System.in);
        String url = "http://localhost:8080/";
        URL apiUrl = new URL(url);


        while (true) {
            runner.printMenu();
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    boolean successSignup = runner.signup(url,sc);
                    if(successSignup) runner.dashboard(runner,sc,url);
                    break;
                case 2:
                    boolean successLogin = runner.login(url,sc);
                    if(successLogin) runner.dashboard(runner,sc,url);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void dashboard(Runner runner,Scanner sc,String url) {
        boolean noExit = true;
        while (noExit) {
            runner.printDashboard();
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    runner.buildAllImages();
                    break;
                case 2:
                    runner.startService();
                    break;
                case 3:
                    runner.stopService();
                    break;
                case 4:
                    runner.changeLoadBalancerConfiguration();
                    break;
                case 5:
                    runner.changeAutoScalerConfiguration();
                    break;
                case 6:
                    runner.listAllRunningServices();
                    break;
                case 7:
                    runner.deleteUser();
                    break;

                case 8:
                    noExit = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void deleteUser() {
    }

    private void listAllRunningServices() {

    }

    private void changeAutoScalerConfiguration() {
    }

    private void changeLoadBalancerConfiguration() {

    }

    private void stopService() {

    }

    private void startService() {

    }

    private void buildAllImages() {

    }

    public void printMenu() {
        clearConsole();
        System.out.println("########### Load Balancer and Autoscaler ############");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    public void printDashboard() {
        clearConsole();
        System.out.flush();
        System.out.println("########### Load Balancer and Autoscaler ############");
        System.out.println("1. Build all images");
        System.out.println("2. Start a service");
        System.out.println("3. Stop a service");
        System.out.println("4. Change Load Balancer configuration of a running service");
        System.out.println("5. Change AutoScaler configuration of a running service");
        System.out.println("6. List of all running services");
        System.out.println("7. Delete user and stop all its services");
        System.out.println("8. Exit");
    }

    public boolean login(String url,Scanner sc) {
        try{
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            String urlWithParams = url + "user/login?email=" + email + "&password=" + password;
            URL apiUrl = new URL(urlWithParams);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            int responseCode = connection.getResponseCode();
//            connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.getOutputStream().write(data.getBytes());
//                responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Login Successful");
                return true;
            } else {
                System.out.println("Login Failed");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error Signing up");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    public boolean signup(String url, Scanner sc) {
        try{
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            System.out.print("Enter company name: ");
            String companyName = sc.nextLine();
            String urlWithParams = url + "user/signup?email=" + email + "&password=" + password + "&companyName=" + companyName;
            URL apiUrl = new URL(urlWithParams);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();

//            connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.getOutputStream().write(data.getBytes());
//                responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Signup Successful");
                return true;
            } else {
                System.out.println("Signup Failed");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error Signing up");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For UNIX-like systems
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}