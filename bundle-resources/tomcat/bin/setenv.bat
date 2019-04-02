set CATALINA_OPTS=-Djavax.net.ssl.trustStore="%CATALINA_HOME%\certs\keystore.jks" -Djavax.net.ssl.keyStore="%CATALINA_HOME%\certs\keystore.jks" -Djavax.net.ssl.trustStorePassword=changeit -Djavax.net.ssl.keyStorePassword=changeit -Xmx1024m -Xms512m -XX:PermSize=128m -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrent %CATALINA_OPTS%

rem
rem High Performance EXAMPLE CATALINA_OPTS Settings (Notice, Xmx == Xms):
rem   -Xmx7168m 
rem	  -Xms7168m 
rem   -XX:PermSize=1024m 
rem   -XX:MaxPermSize=2048m 
rem

rem Check for -server flag
java -server -version 1> nul 2>&1
if not errorlevel 1 (
  set CATALINA_OPTS=-server %CATALINA_OPTS%
)

set JAVA_OPTS=%JAVA_OPTS% -Dstringchararrayaccessor.disabled=true

echo Running SETENV.BAT
echo Changing directory to CATALINA_HOME
cd "%CATALINA_HOME%"
