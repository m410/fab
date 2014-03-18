#!/bin/bash
java -cp \
/Users/m410/.m2/repository/org/apache/felix/org.apache.felix.main/4.2.1/org.apache.felix.main-4.2.1.jar:\
/Users/m410/.m2/repository/org/apache/felix/org.apache.felix.framework/4.2.1/org.apache.felix.framework-4.2.1.jar:\
/Users/m410/.m2/repository/org/yaml/snakeyaml/1.13/snakeyaml-1.13.jar:\
target/fab-runner-0.1-SNAPSHOT.jar \
org.m410.fab.Application $@

