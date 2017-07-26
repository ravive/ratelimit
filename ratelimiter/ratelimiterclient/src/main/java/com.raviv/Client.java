package com.raviv;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.log4j.Logger;


import java.io.IOException;


public class Client {


    final static Logger logger = Logger.getLogger(Client.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        CommandLineArgs commandLineArgs = new CommandLineArgs();
        JCommander command = JCommander.newBuilder()
                .addObject(commandLineArgs)
                .programName("limiter tester")
                .build();

        command.parse(args);

        if(commandLineArgs.isHelp())
        {
            command.usage();
            System.exit(0);
        }

        String uri ="http://localhost:4567/";
        int numberOfTask = commandLineArgs.getConcurrency();
        logger.info("concurrency level is "+numberOfTask);
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
