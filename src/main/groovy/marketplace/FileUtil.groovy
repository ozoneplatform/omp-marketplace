package marketplace

import groovy.io.FileType

class FileUtil {

    // returns a map[file name, String[] of extensions]
    def static fileInfoFromDirectory(def folder) {
        def directory = new File(folder)

        // create recursive list of all files in directory
        def fileList = []
        directory.eachFileRecurse(FileType.FILES) { file ->
            fileList << file
        }

        // create a map of each file name (key) and a list of all extensions associated with it (type)
        def fileDescriptions = [:]
        fileList.each() { file ->
            def fileInfo = file.getName().split("\\.")

            if (!fileDescriptions.containsKey(fileInfo[0]))
                fileDescriptions.put(fileInfo[0], [fileInfo[1]])
            else
                fileDescriptions.get(fileInfo[0]).add(fileInfo[1])

        }

        def keys = fileDescriptions.keySet() as String[]

        keys.each() { key ->
            //verify that the proper files are present via length
            def curInfo = fileDescriptions.get(key);
            if (curInfo.size() < 3 || !curInfo[0].equals('pdf') || !curInfo[0].equals('ppt'))
                fileDescriptions.remove(key)
        }

        return fileDescriptions
    }

    // returns a map[file name, String[] of info] from descriptor file in a given folder
    def static mediaInfoFromDescriptor(def folder) {

        def fileDescriptions = [:]

        new File(folder + "/MediaDescriptor.txt").eachLine { line ->
            def fileInfo = line.split(';')
            fileInfo[0] = fileInfo[0].replace(' ', '_')

            fileDescriptions.put(fileInfo[0], fileInfo)
        }

        return fileDescriptions
    }

    // returns a map[file name, String[] of info] from given descriptor file
    def static mediaInfoFromDescriptorFile(def mediaDescriptorFile) {

        def fileDescriptions = [:]

        mediaDescriptorFile.eachLine { line ->
            def fileInfo = line.split(';')
            fileInfo[0] = fileInfo[0].replace(' ', '_')

            fileDescriptions.put(fileInfo[0], fileInfo)
        }

        return fileDescriptions
    }
}
