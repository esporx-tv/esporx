#!/bin/bash
export JAVA_OPTS="-server -Xmx512m -XX:MaxPermSize=512M -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"
: ${TOMCAT_HOME?"You should define TOMCAT_HOME as your Tomcat base directory"}

if [ -f target/esporx_tv.war ]; then
	$TOMCAT_HOME/bin/shutdown.sh
	cp target/esporx_tv.war $TOMCAT_HOME/webapps/ROOT.war
	$TOMCAT_HOME/bin/catalina.sh start
else
	echo "WAR not found: you should first package the app!"
fi
