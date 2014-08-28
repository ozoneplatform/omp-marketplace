import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils

includeTargets << new File("$basedir/scripts/_BuildHelper.groovy")

runAppTasks = { basedir ->
    def baseWebDir = "${basedir}/web-app"
    def applicationConfigUiDir = "${baseWebDir}/js/applicationConfiguration"

    if (!System.properties.skipSassCompile) {
        println "Compiling themes..."
        println "Use -DskipSassCompile=true to omit this step"
        compileStyleSheets baseWebDir
        println "Finished compiling themes"
    }

	if(!System.properties.skipCopyAppConfig) {
		println "Retrieving the Application Configuration Client Files from Ivy"
		println "Use -DskipCopyAppConfig=true to omit this step"
		copyAppConfigFiles applicationConfigUiDir
		println "finished copying app configuration files"
	}
}

eventRunAppHttpsStart = {
    runAppTasks(basedir)
}

eventRunAppStart = {
    runAppTasks(basedir)
}

eventCreateWarStart = { name, stagingDir ->
    def stageClassesDir ="${stagingDir}/WEB-INF/classes"
    def applicationConfigUiDir = "${stagingDir}/js/applicationConfiguration"

    copyRequireJSFiles stagingDir
	copyAppConfigFiles applicationConfigUiDir
    compileStyleSheets stagingDir

    ant.property(name: "app.version", value: metadata['app.version'])
    ant.tstamp {
        format(property: "CURRENT_TIME", pattern: "MM/dd/yyyy hh:mm:ss aa", locale: "en,US")
    }
    ant.delete(dir: "${stageClassesDir}/quartz")
    ant.copy(file: "${basedir}/grails-app/conf/quartz/quartz.properties", todir: stageClassesDir)
    ant.copy(file: "${basedir}/resources/about.properties", todir: stageClassesDir) {
        filterchain {
            expandproperties()
        }
    }
}
