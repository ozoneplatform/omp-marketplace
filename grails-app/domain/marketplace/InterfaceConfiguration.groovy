package marketplace

class InterfaceConfiguration implements Serializable {

    String name
    String defaultLargeIconUrl
    String defaultSmallIconUrl
    String fullStaticParameters
    String deltaStaticParameters
    String deltaSinceTimeParam
    String queryDateFormat
    String responseDateFormat
    Boolean looseMatch = false
    Boolean autoCreateMetaData = false
    Boolean allowTruncate = true
    Boolean downloadImages = false

    static constraints = {
        name(blank: false, nullable: false, maxSize: 256)
        defaultLargeIconUrl(nullable: true, maxSize: 2048, validator: { val, obj ->
            if (val?.trim()?.size() > 0 && !(val ==~ Constants.URL_PATTERN)) {
                return ['interfaceConfiguration.defaultLargeIconUrl.invalid']
            }
        })
        defaultSmallIconUrl(nullable: true, maxSize: 2048, validator: { val, obj ->
            if (val?.trim()?.size() > 0 && !(val ==~ Constants.URL_PATTERN)) {
                return ['interfaceConfiguration.defaultSmallIconUrl.invalid']
            }
        })
        fullStaticParameters(blank: true, nullable: true, maxSize: 2048)
        deltaStaticParameters(blank: true, nullable: true, maxSize: 2048)
        deltaSinceTimeParam(blank: true, nullable: true, maxSize: 64)
        queryDateFormat(blank: false, nullable: true, maxSize: 32)
        responseDateFormat(blank: false, nullable: true, maxSize: 32)
    }

    public String toString() {
        return "Interface: $name"
    }

    /**
     * Given a field name, if this class has a default value for the
     * field mapped in the DEFAULT_IMAGE_MAP and a value exists, the
     * value will be returned
     * @param imageName
     * @return
     */
    public String getDefaultImage(String imageName) {
        return DEFAULT_IMAGE_MAP[imageName] ? this[DEFAULT_IMAGE_MAP[imageName]] : null
    }

    /**
     * Map the names of image fields to associated defaults provided in this config
     */
    static final Map DEFAULT_IMAGE_MAP =
        [(Constants.FIELD_IMAGESMALL): 'defaultSmallIconUrl',
            (Constants.FIELD_IMAGELARGE): 'defaultLargeIconUrl']


    public static final InterfaceConfiguration OMP_INTERFACE =
        new InterfaceConfiguration(name: Constants.OMP_IMPORT_EXECUTOR,
            queryDateFormat: Constants.EXTERNAL_DATE_FORMAT,
            responseDateFormat: Constants.EXTERNAL_DATE_PARSE_FORMAT,
            deltaSinceTimeParam: Constants.OMP_IMPORT_DELTA_DATE_FIELD)

    public static final InterfaceConfiguration FILE_IMPORT =
        new InterfaceConfiguration(name: Constants.FILE_BASED_IMPORT_EXECUTOR)

    public static InterfaceConfiguration getOmpInterface() {
        InterfaceConfiguration.findByName(Constants.OMP_IMPORT_EXECUTOR)
    }

    public static InterfaceConfiguration getFileInterface() {
        InterfaceConfiguration.findByName(Constants.FILE_BASED_IMPORT_EXECUTOR)
    }
}

