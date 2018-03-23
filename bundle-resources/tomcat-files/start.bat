@ECHO OFF
SETLOCAL

SET GRAILS_ENV=production
SET CONFIG_DB=h2

SET AUTO_USER=testAdmin1

GOTO :parseOptions

:showUsage
ECHO:
ECHO Usage: %0 [/dev] [/init] [/db database]
ECHO:
ECHO Optional arguments:
ECHO   /init      Initialize the database
ECHO   /db        Use the selected database configuration
ECHO   database     h2     - Embedded H2 file-based database (default)
ECHO                pg     - PostgreSQL
ECHO                mysql  - MySQL
ECHO                oracle - Oracle RDBMS
ECHO                mssql  - Microsoft SQL Server
ECHO   /dev       Start server in DEVELOPMENT mode
ECHO:
ECHO Note: Selecting a database (other than H2) requires that the respective
ECHO       database JDBC driver JAR be placed in the 'tomcat\lib\' directory.
ECHO:
GOTO :end

:parseOptions
IF NOT "%1"=="" (
    IF "%1"=="/?" (
        GOTO :showUsage
    )
    IF "%1"=="/help" (
        GOTO :showUsage
    )
    IF "%1"=="/dev" (
        SET GRAILS_ENV=development
        GOTO :parseNextOption
    )
    IF "%1"=="/init" (
        SET INIT_DB=true
        GOTO :parseNextOption
    )
    IF "%1"=="/db" (
        IF "%2"=="" (
            ECHO Invalid argument: /db requires database option
            GOTO :showUsage
        )
        IF "%2"=="h2" (
            SET CONFIG_DB=h2
            GOTO :parseNextOption2
        )
        IF "%2"=="pg" (
            SET CONFIG_DB=postgresql
            GOTO :parseNextOption2
        )
        IF "%2"=="mysql" (
            SET CONFIG_DB=mysql
            GOTO :parseNextOption2
        )
        IF "%2"=="oracle" (
            SET CONFIG_DB=oracle
            GOTO :parseNextOption2
        )
        IF "%2"=="mssql" (
            SET CONFIG_DB=sqlserver
            GOTO :parseNextOption2
        )
        ECHO Invalid database option: %2
        GOTO :showUsage
    )
    ECHO Invalid argument: %1
    GOTO :showUsage
)
GOTO :startServer

:parseNextOption2
SHIFT /1
:parseNextOption
SHIFT /1
GOTO :parseOptions

:startServer
SET JAVA_OPTS=%JAVA_OPTS% -Dgrails.env=%GRAILS_ENV%
SET JAVA_OPTS=%JAVA_OPTS% -Dspring.config.location="classpath:/config/ozone-marketplace_%CONFIG_DB%.yml"

IF "%INIT_DB%"=="true" (
  SET JAVA_OPTS=%JAVA_OPTS% -Dowf.db.init=true
  del prodDb.mv.db > nul 2>&1
)

IF "%GRAILS_ENV%"=="development" (
  SET JAVA_OPTS=%JAVA_OPTS% -Duser=%AUTO_USER%
)


bin\catalina.bat run

:end
