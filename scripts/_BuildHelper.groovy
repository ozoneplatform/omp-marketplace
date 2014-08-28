compileStyleSheets = { dir ->
    if (System.properties['os.name'].toLowerCase().contains('windows')) {
        ant.exec(
                failonerror: "true",
                dir: "${dir}/themes",
                executable: 'cmd') {

            arg(value: "/c")
            arg(value: "compile_all_themes.bat")
        }
    }
    else {
        ant.exec(
                failonerror: "true",
                dir: "${dir}/themes",
                executable: 'bash') {

            arg(value: "-l")
            arg(value: "compile_all_themes.sh")

        }
    }

    //delete cache files
    ant.delete(includeemptydirs:true) {
        fileset(dir:"${dir}") {
            include(name:"**/.sass-cache/**")
        }
    }
}

copyAppConfigFiles = { destinationDir ->

	String sourceDir = "${basedir}/plugins/ozone-appconfig-0.9/web-app/js/applicationConfiguration"

	ant.copy(todir: destinationDir) {
		fileset(dir: sourceDir)
	}
}

copyRequireJSFiles = { destinationDir ->
    ant.copy(file: "${basedir}/js-build/quickview/index.js", todir: "${destinationDir}/js/quickview")
    ant.copy(file: "${basedir}/js-build/dataExchange/exportWizard/app.js", todir: "${destinationDir}/js/dataExchange/exportWizard")
    ant.copy(file: "${basedir}/js-build/dataExchange/importWizard/app.js", todir: "${destinationDir}/js/dataExchange/importWizard")
    ant.copy(file: "${basedir}/js-build/affiliatedSearch/affiliatedSearch-main.js", todir: "${destinationDir}/js/affiliatedSearch")
    ant.copy(file: "${basedir}/js-build/listingManagement/index.js", todir: "${destinationDir}/js/listingManagement")
    ant.copy(file: "${basedir}/js-build/createEditListing/index.js", todir: "${destinationDir}/js/createEditListing")
    ant.copy(file: "${basedir}/js-build/RouterMain.js", todir: "${destinationDir}/js")
    ant.copy(file: "${basedir}/js-build/profile/Window.js", todir: "${destinationDir}/js/profile")
    ant.copy(file: "${basedir}/js-build/views/filter/FilterMenu.js", todir: "${destinationDir}/js/views/filter")
}
