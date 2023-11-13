package org.derrick.smartdevicesController;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.result("Hello ! Welcome to the Hub Monitor"));
        app.get("/state", Controller::getFileReaderInfo);


    }



}
