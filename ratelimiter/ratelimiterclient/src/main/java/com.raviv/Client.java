package com.raviv;


import java.io.IOException;


public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        String uri ="http://localhost:4567/";
        int numberOfTask = 10;
        RequestExecutor requestExecutor = new RequestExecutor(numberOfTask);
        for (int i=0;i<numberOfTask;++i) {
            requestExecutor.withTask(new Task(i,uri));
        }

        requestExecutor
                .run()
                .waitForKeyPress()
                .stop();
    }
}
