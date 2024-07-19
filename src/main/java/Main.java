import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    private static String userInput;
    private static boolean appRunning;

    private static URI uri;
    private static HttpGet requestGet;
    private static HttpPost requestPost;
    private static CloseableHttpClient client;
    private static CloseableHttpResponse response;

    public static void main(String[] args) throws IOException {
        appRunning = true;
        runApp();
    }

    public static void runApp() throws IOException {
        client = HttpClients.createDefault();
        while (appRunning){
            System.out.println("What would you like to do?");
            System.out.println("1. Register" +
                    "\n" + "2. Get user tasks" +
                    "\n" + "3. Add task" +
                    "\n" + "4. Set task done" +
                    "\n" + "5. Exit program");
            userInput = new Scanner(System.in).nextLine();
            Response responseObject;
            switch (userInput){
                case "1":
                    uri = getUriBuilder("register");
                    requestPost = new HttpPost(uri);
                    response = client.execute(requestPost);
                    responseObject = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Response.class);
                    System.out.println("Created user - " + responseObject.isSuccess());
                    break;
                case "2":
                    uri = getUriBuilder("get-tasks");
                    requestGet = new HttpGet(uri);
                    response = client.execute(requestGet);
                    responseObject = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Response.class);
                    for (Task task : responseObject.getTasks()){
                        System.out.println(task.getTitle() + " , status done: " + task.isDone());
                    }
                    break;
                case "3":
                    uri = getUriBuilder("add-task");
                    requestPost = new HttpPost(uri);
                    response = client.execute(requestPost);
                    responseObject = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Response.class);
                    System.out.println("Added task - " + responseObject.isSuccess());
                    break;
                case "4":
                    uri = getUriBuilder("set-task-done");
                    requestPost = new HttpPost(uri);
                    response = client.execute(requestPost);
                    responseObject = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Response.class);
                    System.out.println("Set task done - " + responseObject.isSuccess());
                    break;
                case "5":
                    System.out.println("GoodBye");
                    appRunning = false;
                default:
                    break;
            }
        }
    }

    public static URI getUriBuilder(String path){
        URI tempUri = null;
        try {
            if (userInput.equals("1") || userInput.equals("2")){
                System.out.print("Enter id: ");
                String id = new Scanner(System.in).nextLine();

                tempUri = new URIBuilder("https://app.seker.live/fm1/" + path)
                        .setParameter("id", id)
                        .build();
            } else if (userInput.equals("3") || userInput.equals("4")) {
                System.out.print("Enter id: ");
                String id = new Scanner(System.in).nextLine();
                System.out.print("Enter task title: ");
                String text = new Scanner(System.in).nextLine();

                tempUri = new URIBuilder("https://app.seker.live/fm1/" + path)
                        .setParameter("id", id)
                        .setParameter("text", text)
                        .build();
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return tempUri;
    }

}
