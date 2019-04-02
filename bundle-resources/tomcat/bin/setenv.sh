#!/bin/sh

CATALINA_OPTS="
    -Djavax.net.ssl.trustStore=\"$CATALINA_HOME/certs/keystore.jks\" 
    -Djavax.net.ssl.keyStore=\"$CATALINA_HOME/certs/keystore.jks\" 
    -Djavax.net.ssl.keyStorePassword=changeit 
    -Djavax.net.ssl.trustStorePassword=changeit 
    -Xmx1024m 
    -Xms512m 
    -XX:PermSize=128m 
    -XX:MaxPermSize=256m 
    -XX:+UseConcMarkSweepGC 
    -XX:+ExplicitGCInvokesConcurrent
    $CATALINA_OPTS
"

#
# High Performance EXAMPLE CATALINA_OPTS Settings (Notice, Xmx == Xms):
# 	-Xmx7168m 
#	-Xms7168m 
#   -XX:PermSize=1024m 
#   -XX:MaxPermSize=2048m 
#

#if the server jvm is available
if java -server -version 2> /dev/null; then
    CATALINA_OPTS="-server $CATALINA_OPTS"
fi

export JAVA_OPTS=$JAVA_OPTS -Dstringchararrayaccessor.disabled=true

echo "Running setenv.sh"
echo "Changing directory to CATALINA_HOME"
cd $CATALINA_HOME
