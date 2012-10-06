#!/bin/bash
export MAVEN_OPTS="-Xmx2000m -Xms2000m"
mvn package jetty:run-exploded $*
