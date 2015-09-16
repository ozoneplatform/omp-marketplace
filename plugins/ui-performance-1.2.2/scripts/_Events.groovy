import grails.util.GrailsUtil

/**
 * In 1.0.x this is called after the staging dir is prepared but before the war is packaged.
 */
eventWarStart = { name ->
	if (name instanceof String || name instanceof GString) {
		versionResources name, stagingDir
	}
}

/**
 * In 1.1 this is called after the staging dir is prepared but before the war is packaged.
 */
eventCreateWarStart = { name, stagingDir ->
	versionResources name, stagingDir
}

void versionResources(name, stagingDir) {
	def classLoader = Thread.currentThread().contextClassLoader
	classLoader.addURL(new File(classesDirPath).toURL())

	def config = new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('Config')).uiperformance
	def enabled = config.enabled
	enabled = enabled instanceof Boolean ? enabled : true

	if (!enabled) {
		println "\nUiPerformance not enabled, not processing resources\n"
		return
	}

	println "\nUiPerformance: versioning resources ...\n"

	String className = 'com.studentsonly.grails.plugins.uiperformance.ResourceVersionHelper'
	def helper = Class.forName(className, true, classLoader).newInstance()
	helper.version stagingDir, basedir
}
