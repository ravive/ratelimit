package com.raviv;


import spark.QueryParamsMap;

import static spark.Spark.*;


public class Server {

    private static RateLimiter rateLimiter =new RateLimiter(5,5);

    public static void main(String[] args) {
        post("/", (req, res) -> "echo");
        get("/", (req, res) -> "echo");

        /**
         * rate limiter filter before each request
         */
        before((request, response) -> {
            QueryParamsMap clientId = request.queryMap("clientId");

            if (!rateLimiter.consume(clientId.value(),1)) {
                halt(503 , "Service Unavailable");
            }
        });
    }

}
