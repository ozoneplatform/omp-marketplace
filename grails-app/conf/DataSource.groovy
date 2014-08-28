dataSource {
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}

hibernate {
  cache.use_second_level_cache=true
  cache.use_query_cache=true
  cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
    development {

        hibernate {
            cache.use_second_level_cache = false
            cache.use_query_cache = false
        }

        dataSource {

            // Uncomment to enable SQL logging
            //logSql=true


            /* H2 */
            driverClassName = "org.h2.Driver"
            url = "jdbc:h2:file:mktplDevDb"
            username = "sa"
            password = ""
            dbCreate="none"
            properties {
                minEvictableIdleTimeMillis = 30000
                timeBetweenEvictionRunsMillis = 5000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                validationQuery = "SELECT 1"
                validationInterval=30000
                maxActive=100
                initialSize = 10
                maxIdle=50
                minIdle=10
                removeAbandoned=true
                removeAbandonedTimeout=60
                abandonWhenPercentageFull=50
                jdbcInterceptors = "ResetAbandonedTimer"
            }
		}
	}
	test {
        hibernate {
            cache.use_second_level_cache = false
            cache.use_query_cache = false
        }

		dataSource {
            pooled = true

            /* H2 */
            driverClassName = "org.h2.Driver"
            url = "jdbc:h2:file:mktplTestDb"
            username = "sa"
            password = ""
            dbCreate="none"
            properties {
                minEvictableIdleTimeMillis = 30000
                timeBetweenEvictionRunsMillis = 5000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                validationQuery = "SELECT 1"
                validationInterval=30000
                maxActive=100
                initialSize = 10
                maxIdle=50
                minIdle=10
                removeAbandoned=true
                removeAbandonedTimeout=60
                abandonWhenPercentageFull=50
                jdbcInterceptors = "ResetAbandonedTimer"
            }
		}
	}
	loadtest {
		hibernate {
			cache.use_second_level_cache=false
			cache.use_query_cache=false
			cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
			jdbc.batch_size = 50
		}
		dataSource {
			println "Running in load test mode"

			//logSql=true
			pooled = true

			dbCreate="create"
			url = "jdbc:hsqldb:${basedir}/mktplDevDB;shutdown=true"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	functionaltest {
		dataSource {
			println "Executing Functional Test Mode"
			pooled = true
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}

			dbCreate="create-drop"
			url = "jdbc:hsqldb:${basedir}/mktplDevDB;shutdown=true"
		}
	}
	production {
		hibernate {
		    cache.use_second_level_cache = true
		    cache.use_query_cache = true
		    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
		}

		dataSource {
			pooled = true
			dbCreate = "none" // one of 'create', 'create-drop','update'
			url = "jdbc:h2:file:mktplProdDb"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	with_hsql {
		dataSource {
			pooled = true
			dbCreate="create-drop"
			url = "jdbc:hsqldb:${basedir}/mktplDevDB;shutdown=true"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	with_mysql  {
		dataSource {
			pooled = true
			dbCreate = "none"
			driverClassName = "com.mysql.jdbc.Driver"
			url="jdbc:mysql://amldb01.goss.owfgoss.org:3306/aml_mp_unit"
			username = "aml_mp_unit"
			password = "aml_mp_unit"
			dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	with_oracle {
		dataSource {
			pooled = true
			dbCreate = "none"
			driverClassName="oracle.jdbc.OracleDriver"
			url="jdbc:oracle:thin:@owfdb03.goss.owfgoss.org:1521:XE"
			username="aml_mp_unit"
			password="aml_mp_unit"
			dialect="org.hibernate.dialect.Oracle10gDialect"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1 FROM DUAL"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	with_postgres {
		dataSource {
			pooled = true
			dbCreate = "none"
			username = "owf_test"
			password = "owf_test"
			driverClassName = "org.postgresql.Driver"
			url = "jdbc:postgresql://owfdb02.goss.owfgoss.org:5432/aml_unit"
			dialect="org.hibernate.dialect.PostgreSQLDialect"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}
	with_sql_server {
		dataSource {
			pooled = true
			dbCreate = "none"
			driverClassName = "net.sourceforge.jtds.jdbc.Driver"
			url = "jdbc:jtds:sqlserver://owfdb02.goss.owfgoss.org:1443/aml_unit"
			username = "owf_test"
			password = "owf_test"
			dialect="ozone.marketplace.domain.NSQLServerDialect"
			properties {
				minEvictableIdleTimeMillis = 30000
				timeBetweenEvictionRunsMillis = 5000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				validationQuery = "SELECT 1"
				validationInterval=30000
				maxActive=100
				initialSize = 10
				maxIdle=50
				minIdle=10
				removeAbandoned=true
				removeAbandonedTimeout=60
				abandonWhenPercentageFull=50
				jdbcInterceptors = "ResetAbandonedTimer"
			}
		}
	}

	mysql_migration  {
		dataSource {
			driverClassName = "com.mysql.jdbc.Driver"
			url="jdbc:mysql://amldb01.goss.owfgoss.org:3306/aml_mp_migra"
			username = "aml_mp_migra"
			password = "aml_mp_migra"
			dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
		}
	}

	oracle_migration {
		dataSource {
			driverClassName="oracle.jdbc.OracleDriver"
			url="jdbc:oracle:thin:@owfdb03.goss.owfgoss.org:1521:XE"
			username="aml_mp_migration"
			password="aml_mp_migration"
			dialect="org.hibernate.dialect.Oracle10gDialect"
		}
	}

	postgresql_migration {
		dataSource {
			driverClassName = "org.postgresql.Driver"
			url = "jdbc:postgresql://owfdb02.goss.owfgoss.org:5432/aml_migration"
			username = "owf_test"
			password = "owf_test"
			dialect="org.hibernate.dialect.PostgreSQLDialect"
		}
	}

	sqlserver_migration {
		dataSource {
			driverClassName = "net.sourceforge.jtds.jdbc.Driver"
			url = "jdbc:jtds:sqlserver://owfdb02.goss.owfgoss.org:1443/aml_migration"
			username = "owf_test"
			password = "owf_test"
			dialect="ozone.marketplace.domain.NSQLServerDialect"
		}
	}

	mysql_empty  {
		dataSource {
			driverClassName = "com.mysql.jdbc.Driver"
			url="jdbc:mysql://amldb01.goss.owfgoss.org:3306/aml_mp_empty"
			username = "aml_mp_empty"
			password = "aml_mp_empty"
			dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
		}
	}

	oracle_empty {
		dataSource {
			driverClassName="oracle.jdbc.OracleDriver"
			url="jdbc:oracle:thin:@owfdb03.goss.owfgoss.org:1521:XE"
			username="aml_mp_empty"
			password="aml_mp_empty"
			dialect="org.hibernate.dialect.Oracle10gDialect"
		}
	}

	postgresql_empty {
		dataSource {
			driverClassName = "org.postgresql.Driver"
			url = "jdbc:postgresql://owfdb02.goss.owfgoss.org:5432/aml_empty"
			username = "owf_test"
			password = "owf_test"
			dialect="org.hibernate.dialect.PostgreSQLDialect"
		}
	}

	sqlserver_empty {
		dataSource {
			driverClassName = "net.sourceforge.jtds.jdbc.Driver"
			url = "jdbc:jtds:sqlserver://owfdb02.goss.owfgoss.org:1443/aml_empty"
			username = "owf_test"
			password = "owf_test"
			dialect="ozone.marketplace.domain.NSQLServerDialect"
		}
	}
}
