Fab(ricate) Java compiler task for fab project build tool
======================================

An OSGI bundle that's part of the fab build tool.

deploy normally with source and javadoc, but you also need to deploy the configration file 
separately like this:

    mvn deploy:deploy-file\
     -Dfile=src/main/resources/configuration.yml\
     -DrepositoryId=repo.m410.org\
     -DartifactId=fab-java-garden-project\
     -DgroupId=org.m410.fabricate.garden\
     -Dversion=1.1-SNAPSHOT\
     -Durl=http://repo.m410.org/content/repositories/snapshots


