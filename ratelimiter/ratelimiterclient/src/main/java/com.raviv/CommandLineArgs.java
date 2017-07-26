package com.raviv;

import com.beust.jcommander.Parameter;

/**
 * Created by ravive on 26/07/2017.
 */
public class CommandLineArgs {

    @Parameter(names = "-c", description = "concurrency level")
    private int concurrency = 1;

    @Parameter(names = "-h", description = "help")
    private boolean help = false;

    public int getConcurrency() {
        return concurrency;
    }


    public boolean isHelp() {
        return help;
    }
}
