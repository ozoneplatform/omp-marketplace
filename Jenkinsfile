
// Jenkinsfile

//  Additional Confiuration for the project
node('ompbuildserver') {
   // only needed to run on polling
    git poll: true, url: 'http://jflores@rite.sd.spawar.navy.mil/stash/scm/dg/omp-marketplace.git', credentialsId: 'rite-jflores'
    // Set a trigger to work with jenkins web hook
    properties([
        pipelineTriggers([
            [$class: "SCMTrigger", scmpoll_spec: "H/5 1 1 1 1"],
        ])
   ])
   
    env.JAVA_HOME="/opt/java"
    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
    sh 'java -version'   

   
}

pipeline {

    agent {
      label 'ompbuildserver'
    }

    stages {
        // Build the project
        stage('Build') {
            steps {


              script {
			    sh "sed -i \"/mavenRepo/a         mavenRepo 'https://maven.atlassian.com/3rdparty/'\" grails-app/conf/BuildConfig.groovy"
                sh "ant setup-ant"
                sh "ant bundle"
              }
	
			}  

        }


    }
    post {
      always {
	    // publish JUnit results to Jenkins
		junit 'target/test-reports/*.xml'
        archive "target/*"
		deleteDir()
		
      }
    }
}

