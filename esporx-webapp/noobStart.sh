#!/bin/bash
export MAVEN_OPTS="-Xmx1024m -Xms1024m"
mvn package jetty:run-exploded $*
