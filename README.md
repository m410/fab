# Fab(ricate)  Command line application installer.

Builds the distributable zip file, that is the fabricate command line utilities.

Make sure java is installed:

    > java -version

To install on Unix (mac,linux) based systems, download the zip and place it on your file
system.

    /opt/local/fab.zip

unzip it with

    > unzip /opt/local/fab.zip

And add the bin directory to you path by editing you bash profile.

    > vi ~/.profile
    FAB_HOME=/opt/local/fab
    PATH=$PATH;$FAB_HOME/bin


Test it with:

    > fab -version

you should see something like this:

    [me@computer /Users/me]% fab -version
    Brzy Fab(ricate) Version: 0.14
    Brzy Home: /opt/local/fab
    Java: Java HotSpot(TM) 64-Bit Server VM ( build 1.6.0_45-b06-451-11M4406)
    Scala: version 2.10.0

## Projects

A project is a directory on your file system that contains one fabricate configuration
file the conforms to a project schema.

 -  brzy-webapp
 -  scala library

## Commands

List the tasks in the current project

    > fab -tasks

List avalable archetypes

    > fab-db list


## Tasks

