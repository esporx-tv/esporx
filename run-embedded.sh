#!/bin/bash
export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
export JAVA_OPTS="-server -Xmx512m -XX:MaxPermSize=512M -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"
mvn tomcat6:run $*
