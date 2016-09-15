# Fab(ricate)  Command line application installer.

Builds the distributable zip file, that is the fabricate command line utilities.

## Install on widows using Scoop

Install scoop from the url: http://scoop.sh and run the command:
    
    > scoop install https://raw.githubusercontent.com/m410/fab/master/fab.json

## Install on mac using homebrew

Install homebrew from the url: http://brew.sh and run the command:
    
    > brew install https://raw.githubusercontent.com/m410/fab/master/fab.rb

## Install from scratch

Make sure java is installed:

    > java -version

To install on Unix (mac,linux) based systems, download the zip and place it on your file
system.

    /opt/local/fab-0.2.tar.gz

unzip it with

    > unzip /opt/local/fab-0.2.tar.gz

And add the bin directory to you path by editing you bash profile.

    > vi ~/.profile
    FAB_HOME=/opt/local/fab
    PATH=$PATH;$FAB_HOME/bin


Test it with:

    > fab -version

you should see something like this:

    [me@computer /Users/me]% fab -version
    Fab(ricate) Version: 0.1
    Home: /opt/local/fab
    Java: Java HotSpot(TM) 64-Bit Server VM ( build 1.6.0_45-b06-451-11M4406)

## Projects

Fab works from within a project and globally on your system.  When working outside
of a project you can create new project, manage your repostories, etc. From
within a project, the commands available depend on the type of project
and modules installed.

 -  [Garden web application example.](https://github.com/m410/example-garden)
 -  [Basic java jar example.](https://github.com/m410/example-lib)
 

## Commands

List the tasks in the current project

    > fab tasks



