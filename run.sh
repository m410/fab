#!/bin/bash
#java -jar ~/Projects/Java/felix-framework-4.2.1/bin/felix.jar -b target
java -cp ~/.m2/repository/org/apache/felix/org.apache.felix.main/4.2.1/org.apache.felix.main-4.2.1.jar:\
~/.m2/repository/org/apache/felix/org.apache.felix.framework/4.2.1/org.apache.felix.framework-4.2.1.jar:\
target/fab-runner-bundle-0.1-SNAPSHOT.jar \
org.m410.fab.Application $@

