#!/bin/bash
export MAVEN_OPTS="-Xmx1024m -Xms1024m"
mvnDebug package jetty:run-exploded $*
