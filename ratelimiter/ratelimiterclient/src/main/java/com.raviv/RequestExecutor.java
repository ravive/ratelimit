package com.raviv;

import com.mashape.unirest.http.Unirest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * Created by ravive on 25/07/2017.
 */
public class RequestExecutor {

    final static Logger logger = Logger.getLogger(RequestExecutor.class);
    private ExecutorService executorService;
    private List<Future<Void>> tasks;
    private List<Callable<Void>> taskToExecute;


    public RequestExecutor(int concurrencyLevel) {
        executorService = Executors.newFixedThreadPool(concurrencyLevel);
        tasks = new ArrayList<>();
        taskToExecute = new ArrayList<>();
    }

    public RequestExecutor run() throws InterruptedException {
        taskToExecute.forEach(x -> tasks.add(executorService.submit(x)));
         return this;
    }

    public RequestExecutor withTask(Callable task) {
        taskToExecute.add(task);
        return this;
    }

    public RequestExecutor waitForKeyPress() throws IOException {
        System.in.read();
        logger.info("stopping clients");
        return this;
    }

    public void stop() throws IOException, InterruptedException {
        tasks.forEach(x ->
                x.cancel(true
                ));
        executorService.shutdownNow();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        Unirest.shutdown();
    }
}
