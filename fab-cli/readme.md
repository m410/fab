Fab(ricate) CLI
======================================

This is a java 8 OSGI bundle that's part of the fab build tool. This is the command line interface.


 1. Look for project configuration file.
 2. If no file run global command. 
    1. Global command do not need or make use of an osgi container.
    2. Global commands do not create a .fab cache directory or need one.
 3. If a project file is found, initialize project build. 
    1. load the files bundle dependencies.
    2. Read the base project file and load the bundle dependencies.
    3. Cache the base configuration file.  (not done yet).
    4. Read the module configuration files and load the bundle dependencies.
    5. Cache the module configuration file (not done yet).
    6. Start the osgi container and load the bundles.
    7. execute the command or commands passed as arguments


Command execution process
------------------------

there are a fix set of commands:

Each command are comprised of multiple steps.

Each step can be comprise of zero or more tasks.

a task has an execute method which takes a build context.  The build context
is mutable, and contains all the configuration information needed to operate 
on the project.

See [fabricate share](https://github.com/m410/fab-share) for more information about commands, steps and tasks.

 
