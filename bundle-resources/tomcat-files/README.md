# Usage Instructions

## Database drivers

__IMPORTANT:__ 
To use any database (other than H2), the JDBC driver JAR must be copied 
to the `$TOMCAT_HOME/lib/` directory.

PostgreSQL drivers may be found in `$BUNDLE_ROOT/drivers/postgresql-*.jar`


## Configuration

Example configuration files are provided in `$TOMCAT_HOME/lib/config/`:


## Windows start script

Usage:
```
start.bat [/dev] [/init] [/db database]
  /init      Pre-populate the database with the initial data (only use on the first launch!)
  /db        Use the selected database configuration
  database     h2     - Embedded H2 file-based database (default)
               pg     - PostgreSQL
               mysql  - MySQL
               oracle - Oracle RDBMS
               mssql  - Microsoft SQL Server
  /dev       Start server in DEVELOPMENT mode                
```


## Linux start script

Usage:
```
./start.sh [dev] [init] [db [--mysql | --h2 | --pg | --oracle] ]
  init   Pre-populate the database with the initial data (only use on the first launch!)
  dev    Start in `development` mode
  db	 Takes an additional parameter for type of database to be used:  [--mysql | --h2 | --pg | --oracle]
```
