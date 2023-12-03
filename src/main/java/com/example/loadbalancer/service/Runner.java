package com.example.loadbalancer.service;
import com.example.loadbalancer.service.loadbalancer.*;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Runner {
    DockerAgent dockerAgent;
    String currentUserEmail;
    String currentUserCompanyName;


    Runner() {
        this.currentUserEmail = null;
        this.currentUserCompanyName = null;
        this.dockerAgent = new DockerAgent();
//        System.out.println("Starting MySQL Server");
//        SQLAgent startMySQLContainer = new SQLAgent(dockerAgent.getDockerClient());
//        startMySQLContainer.run();
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
                    boolean successSignup = runner.signup(sc);
                    if(successSignup) runner.dashboard(runner,sc,url);
                    break;
                case 2:
                    boolean successLogin = runner.login(sc);
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
                    runner.startService(url,runner.serviceDashboard(sc));
                    break;
                case 3:
                    runner.stopService(url,runner.serviceDashboard(sc));
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
                    noExit = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void listAllRunningServices() {
        dockerAgent.listAllContainers();
    }

    private void changeAutoScalerConfiguration(Scanner sc) {
        String as_strategy;
        int as = autoScalerDashboard(sc);
        switch (as){
            case(1) :
                as_strategy = "threshold";
                break;
            case(2) :
                as_strategy = "timeseries";
                break;
            default:
                as_strategy = "threshold";
                break;
        }


    }

    private void changeLoadBalancerConfiguration(Scanner sc) {
        String lb_strategy;
        int lb = loadBalancerDashBoard(sc);
        switch (lb){
            case(3) :
                lb_strategy = "weightedRoundRobin";
                break;
            case(1) :
                lb_strategy = "random";
                break;
            case(4) :
                lb_strategy = "weightedLeastConnection";
                break;
            case(5) :
                lb_strategy = "ipHash";

                break;
            case (2):
                lb_strategy = "powerOfTwoChoices";
                break;
            default:
                lb_strategy = "weightedRoundRobin";
                break;
        }


    }

    private void stopService(String url, String[] containers) {

        try{
            String urlWithParams = url + "settings/stop?services=" + containers + "&username=" + currentUserCompanyName;
            URL apiUrl = new URL(urlWithParams);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();

//            connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.getOutputStream().write(data.getBytes());
//                responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Services Started");
                return true;
            } else {
                System.out.println("Services could not start");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Services could not start");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return false;

    }

    private boolean startService(String url, String[] containers) {

        try{
            String urlWithParams = url + "settings/start?services=" + containers + "&username=" + currentUserCompanyName;
            URL apiUrl = new URL(urlWithParams);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();

//            connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.getOutputStream().write(data.getBytes());
//                responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Services Started");
                return true;
            } else {
                System.out.println("Services could not start");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Services could not start");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return false;

    }

    private void buildAllImages() {
        dockerAgent.buildAllImages();
    }

    public void printMenu() {
        clearConsole();
        System.out.println("########### Load Balancer and Autoscaler ############");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }
    public String[] serviceDashboard(Scanner sc) {
        clearConsole();
        System.out.println("########### SelectService ############");
        System.out.println("1. Service1");
        System.out.println("2. Service2");
        System.out.println("3. Service3");
        System.out.println("4. Service4");
        System.out.println("5. Service5");

        System.out.print("Enter Choices: ");
        String n = sc.nextLine();
        return n.split(" ");
    }

    public int loadBalancerDashBoard(Scanner sc) {
        this.listAllRunningServices();



//        clearConsole();
        System.out.println("########### Load Balancer ############");
        System.out.println("1. Random Load Balancer");
        System.out.println("2. Power of 2 choices Load Balancer");
        System.out.println("3. Weighted Round Robin Load Balancer");
        System.out.println("4. Weighted Least Connected");
        System.out.println("5. IP Hash");
        System.out.print("Enter Choice: ");;
        return sc.nextInt();
    }

    public int autoScalerDashboard(Scanner sc) {
        this.listAllRunningServices();
        int lb = loadBalancerDashBoard(sc);

        System.out.println("########### Load Balancer ############");
        System.out.println("1. Threshold Based Auto Scaler");
        System.out.println("2. Time Series Based Auto Scaler");
        System.out.print("Enter Choice: ");;
        return sc.nextInt();
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
        System.out.println("7. Exit");
    }

    private boolean login(Scanner scanner) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
//            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(email) && parts[1].equals(password)) {
                    this.currentUserEmail = parts[0];
                    this.currentUserCompanyName = parts[2];
                    System.out.println("Login successful. Welcome, " + parts[2] + "!");
                    return true;
                }
            }


            System.out.println("Invalid email or password. Please try again.");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    private boolean signup(Scanner scanner) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
//            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            System.out.print("Enter your company name: ");
            String company = scanner.nextLine();

            // Append user information to the file
            writer.write(email + "," + password + "," + company);
            writer.newLine();
            this.currentUserEmail = email;
            this.currentUserCompanyName = company;

            System.out.println("User signed up successfully.");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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