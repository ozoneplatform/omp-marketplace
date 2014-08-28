quartz {
    // Starting up automatically must be done in order to start the
    //   QuartzScheduler; However, Quartz wants to launch a scheduled Job  
    //   based on the class itself; our jobs are scheduled based on the 
    //   ImportTasks in the database. Ensure that any Job class created is
    //   configured to not execute, unless class-driven scheduling is desired.
}

environments {
    development {
        quartz {
            autoStartup = true
            jdbcStore = false
        }
    }
    test {
        quartz {
            autoStartup = true
            jdbcStore = false
        }
    }
    production {
        quartz {
            autoStartup = true
            jdbcStore = false
            waitForJobsToCompleteOnShutdown = true
        }
    }
}
