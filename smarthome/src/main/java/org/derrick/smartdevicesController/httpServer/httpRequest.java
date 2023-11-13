package org.derrick.smartdevicesController.httpServer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class httpRequest {
    synchronized public void run() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("localhost:7000/state"))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    System.out.println("success");
                    System.out.println(response.body() );
                } else {
                    System.out.println("Fail");
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        } catch (Exception e) {
            System.out.println(e.getMessage() );
        }
    }
}
