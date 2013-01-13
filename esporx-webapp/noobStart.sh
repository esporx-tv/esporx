#!/bin/bash
export MAVEN_OPTS="-Xmx2048m -Xms1024m"
mvn package jetty:run-exploded $*
