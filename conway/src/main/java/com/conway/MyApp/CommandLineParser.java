package com.conway.MyApp;

public class CommandLineParser {

    // Class to hold parsed options
    public  class CommandLineOptions {
        public int height = 0;
        public int width = 0;
        public int timeInSeconds = 0;
        public boolean jsvOutput = false;
        public boolean csvOutput = false;
        public boolean debug = false;
        public boolean showHelp = false;
        public String filename = "default";
        public CommandLineFlags flags;
    }

    public class CommandLineFlags {
            public boolean height        = false;
            public boolean width         = false;
            public boolean timeInSeconds = false;
            public boolean filename      = false;
    }

    /**
     * Parses the command line arguments.
     * Recognized options:
     *   -h <height>     : sets the height (integer)
     *   -w <width>      : sets the width (integer)
     *   -t <time>       : sets time in seconds (integer)
     *   -j              : enable JSV output
     *   -c              : enable CSV output
     *   -f <filename>   : sets the filename (string)
     *   -?              : display help message
     *
     * @param args the command line arguments
     * @return a CommandLineOptions object with parsed values
     */
    public CommandLineOptions parseArguments(String[] args) {
        CommandLineOptions options = new CommandLineOptions();
        options.flags = new CommandLineFlags();


        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch(arg) {
                case "-h":
                    if (i + 1 < args.length) {
                        try {
                            options.height = Integer.parseInt(args[++i]);
                            options.flags.height = true;
                            
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid height value: " + args[i]);
                        }
                    } else {
                        System.err.println("Missing value after -h");
                    }
                    break;
                case "-w":
                    if (i + 1 < args.length) {
                        try {
                            options.width = Integer.parseInt(args[++i]);
                            options.flags.width = true;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid width value: " + args[i]);
                        }
                    } else {
                        System.err.println("Missing value after -w");
                    }
                    break;
                case "-t":
                    if (i + 1 < args.length) {
                        try {
                            options.timeInSeconds = Integer.parseInt(args[++i]);
                            options.flags.timeInSeconds = true;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid time value: " + args[i]);
                        }
                    } else {
                        System.err.println("Missing value after -t");
                    }
                    break;
                case "-?":
                    options.showHelp = true;
                    break;
                case "-j":
                    options.jsvOutput = true;
                    break;
                case "-c":
                    options.csvOutput = true;
                    break;
                case "-d":
                    options.debug = true;
                    break;                case "-f":
                    if (i + 1 < args.length) {
                        options.filename = args[++i];
                          options.flags.filename = true;
                    } else {
                        System.err.println("Missing filename after -f");
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + arg);
                    break;
            }
        }
        return options;
    }

       
        // Displays the help message.
        public  void printHelp() {
            System.out.println("Usage: java CommandLineParser [options]");
            System.out.println("Options:");
            System.out.println("  -h <height>    Set the height (integer).");
            System.out.println("  -w <width>     Set the width (integer).");
            System.out.println("  -t <time>      Set the time in seconds (integer).");
            System.out.println("  -j             Enable JSV output.");
            System.out.println("  -c             Enable CSV output.");
            System.out.println("  -f <filename>  Specify the filename (string).");
            System.out.println("  -?             Display this help message.");
        
    }

    public void main(String[] args) {
        CommandLineOptions options = parseArguments(args);

        if (options.showHelp) {
            printHelp();
            return;
        }

        // Use the parsed values as needed. For demo, we print them.
        System.out.println("Parsed Values:");
        System.out.println("  Height       : " + options.height);
        System.out.println("  Width        : " + options.width);
        System.out.println("  Time (sec)   : " + options.timeInSeconds);
        System.out.println("  JSV Output   : " + options.jsvOutput);
        System.out.println("  CSV Output   : " + options.csvOutput);
        System.out.println("  Filename     : " + options.filename);
    }
}
