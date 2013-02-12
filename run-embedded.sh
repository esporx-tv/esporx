#!/bin/bash
export JAVA_OPTS="-server -Xmx512m -XX:MaxPermSize=512M -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"
mvn tomcat6:run $*
