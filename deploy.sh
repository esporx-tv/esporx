#!/bin/bash
export JAVA_OPTS="-server -Xmx512m -XX:MaxPermSize=512M -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"
: ${TOMCAT_HOME?"You should define TOMCAT_HOME as your Tomcat base directory"}

if [ ! -d $TOMCAT_HOME ]; then
	echo "The TOMCAT_HOME environment variable does not point to a valid directory"
	exit -1
fi 

if [ ! -f target/esporx_tv.war ]; then
	echo "WAR not found: you should first package the app!"
	exit -1
fi

echo ""
echo "Halting Tomcat"	
echo ""
$TOMCAT_HOME/bin/shutdown.sh

cp target/esporx_tv.war $TOMCAT_HOME/webapps/ROOT.war

echo ""
echo "Starting Tomcat again"
echo ""
$TOMCAT_HOME/bin/catalina.sh start
