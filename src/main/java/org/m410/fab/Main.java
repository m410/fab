package org.m410.fab;

/**
 * @author m410
 */
public class Main {
    public static void main(String[] args) {
        // parse command line args

        // fab -t --tasks
        // fab -i --info
        // fab -h --help
        // fab -d --dependencies
        // fab -b --bundles
        // fab [-c cache] [-h home] [-b base] [-v] [environment] [commands]
        // fab  <runs default command>
        // environment and task names must be unique

        // start osgi container
        // call container runner bundle with parameters

        switch(args[0]) {
            case "-t":
                System.out.println("tasks");
                break;
            case "-i":
                System.out.println("info");
                break;
            case "-h":
                System.out.println("help");
                break;
            case "-d":
                System.out.println("dependencies");
                break;
            default:
                System.out.println("default - run");
        }
    }
}
