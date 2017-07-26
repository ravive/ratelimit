package com.raviv;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by ravive on 25/07/2017.
 */
public class Task implements Callable<Void> {

    final static Logger logger = Logger.getLogger(Task.class);

    private Integer clientId;
    private String host;

    public Task(int clientId,String host) {
        this.clientId = clientId;
        this.host = host;
        Unirest.setTimeouts(30,30);
    }



    @Override
    public Void call() throws Exception {
        logger.info("executed task for client "+ clientId +" started");

        while(!Thread.currentThread().isInterrupted()) {
            try {
                HttpResponse<String> response = Unirest.post(host)
                        .queryString("clientId", this.clientId)
                        .asString();

                int status = response.getStatus();
                logger.info("response code from request is "+status);
            } catch (UnirestException e) {
                logger.error("failed to execute http request with error "+e.toString());
            }

            try {
                long sleepTimeInSec = (long)(Math.random() * 1000);
                logger.debug("sleeping "+sleepTimeInSec+" seconds");
                Thread.sleep(sleepTimeInSec);
            } catch (InterruptedException e) {
                logger.info("executed task for client "+ clientId +" interrupted");
                Thread.currentThread().interrupt();
                break;
            }

        }
        return null;
    }
}
