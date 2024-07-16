package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true){
            System.out.println("Enter your choice: 1 - Get Tasks, 2 - Add Task, 3 - Register, 4 - Set Task As Done");
            int choice = new Scanner(System.in).nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter your id: ");
                    String id = new Scanner(System.in).nextLine();
                    getTasks(id);
                }
                case 2 -> {
                    System.out.println("Enter your id: ");
                    String id = new Scanner(System.in).nextLine();
                    System.out.println("Enter the task: ");
                    String task = new Scanner(System.in).nextLine();
                    addTask(id, task);
                }
                case 3 -> {
                    System.out.println("Enter your id: ");
                    String id = new Scanner(System.in).nextLine();
                    register(id);
                }
                case 4 -> {
                    System.out.println("Enter your id: ");
                    String id = new Scanner(System.in).nextLine();
                    System.out.println("Enter the task: ");
                    String task = new Scanner(System.in).nextLine();
                    setTaskAsDone(id, task);
                }

            }
        }
    }


    public static void getTasks (String id) {
        try {
            CloseableHttpClient client =  HttpClients.createDefault();
            URI uri = new URIBuilder("https://app.seker.live/fm1/get-tasks")
                    .setParameter("id", id)
                    .build();
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            Response myResponse = new ObjectMapper().readValue(json, Response.class);
            if (myResponse.isSuccess()) {
                System.out.println("You have " + myResponse.getTasks().size() + " tasks");
                for (TaskModel taskModel : myResponse.getTasks()) {
                    System.out.println(taskModel.getTitle());
                    System.out.println(taskModel.isDone() ? "DONE!" : "NOT DONE!");
                }
            } else {
                System.out.println(getTextualError(myResponse.getErrorCode()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register (String id) {
        try {
            CloseableHttpClient client =  HttpClients.createDefault();
            URI uri = new URIBuilder("https://app.seker.live/fm1/register")
                    .setParameter("id", id)
                    .build();
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            Response myResponse = new ObjectMapper().readValue(json, Response.class);
            if (myResponse.isSuccess()) {
                System.out.println("You have registered successfully");
            } else {
                System.out.println(getTextualError(myResponse.getErrorCode()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addTask (String id, String text) {
        try {
            CloseableHttpClient client =  HttpClients.createDefault();
            URI uri = new URIBuilder("https://app.seker.live/fm1/add-task")
                    .setParameter("id", id)
                    .setParameter("text", text)
                    .build();
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            Response myResponse = new ObjectMapper().readValue(json, Response.class);
            if (myResponse.isSuccess()) {
                System.out.println("Task was added successfully");
            } else {
                System.out.println(getTextualError(myResponse.getErrorCode()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTextualError (int code) {
        return switch (code) {
            case 1001 -> "You have registered yet!";
            case 1005 -> "You already have this task!";
            case 1003 -> "This user ID is already in use";
            default -> "Unknown error";
        };
    }

    public static void setTaskAsDone (String id, String text) {
        try {
            CloseableHttpClient client =  HttpClients.createDefault();
            URI uri = new URIBuilder("https://app.seker.live/fm1/set-task-done")
                    .setParameter("id", id)
                    .setParameter("text", text)
                    .build();
            HttpPost request = new HttpPost(uri);
            CloseableHttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            Response myResponse = new ObjectMapper().readValue(json, Response.class);
            if (myResponse.isSuccess()) {
                System.out.println("Task was marked as completed!");
            } else {
                System.out.println(getTextualError(myResponse.getErrorCode()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
