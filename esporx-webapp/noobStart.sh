#!/bin/bash
mvn clean package tomcat:run tomcat:deploy $*
