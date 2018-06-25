package marketplace

import javax.servlet.ServletContext

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.util.Environment
import grails.util.Holders
import org.grails.core.artefact.DomainClassArtefactHandler
import org.grails.orm.hibernate.HibernateDatastore
import org.grails.web.util.GrailsApplicationAttributes

import org.springframework.context.ApplicationContext
import org.springframework.security.core.context.SecurityContextHolder

import org.hibernate.SessionFactory

import marketplace.configuration.ApplicationConfigLoader
import marketplace.configuration.FranchiseMessageBundle
import marketplace.configuration.MarketplaceApplicationConfigurationService
import marketplace.rest.ItemCommentServiceItemDto
import marketplace.rest.ProfileServiceItemTagDto
import marketplace.scheduledimport.ScheduledImportSchedulingService
import org.apache.log4j.helpers.Loader
import org.apache.log4j.xml.DOMConfigurator
import org.apache.lucene.search.BooleanQuery

import ozone.marketplace.enums.DefinedDefaultTypes
import ozone.marketplace.enums.ImageType
import ozone.utils.Utils


class BootStrap {

    private static final String COMMON_IMAGES_PATH = '/themes/common/images'

    ServiceItemService serviceItemService

    MarketplaceConversionService marketplaceConversionService

    SearchableService searchableService

    ImagesService imagesService

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    AccountService accountService

    TextService textService

    ScheduledImportSchedulingService scheduledImportSchedulingService

    GrailsApplication grailsApplication

    SessionFactory sessionFactory

    FranchiseMessageBundle messageSource

    InitializationService initializationService

    def init = { ServletContext servletContext ->
        if (Environment.current == Environment.TEST) {
            // Enable global (not thread-local) security context for testing
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
        }

        log.info "BootStrap init (environment=${Environment.current})"

        // setting it to a million clauses by default
        // http://stackoverflow.com/questions/1534789/help-needed-figuring-out-reason-for-maxclausecount-is-set-to-1024-error
        BooleanQuery.setMaxClauseCount(10 ** 6)

        ApplicationContext applicationContext = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT) as ApplicationContext

        Profile systemUser = getOrCreateSystemUser()

        accountService.systemUserId = systemUser.id

        // TODO: Is this still used?
        Holders.config.system_user_id = systemUser.id  //What is this?  conf holder is a singleton, this is bad

        ApplicationContext mainContext = grailsApplication.mainContext
        HibernateDatastore datastore = mainContext.getBean(HibernateDatastore)

        //Register an alias to the configuration service. This provides a common convention for accessing
        //the service from, for example, a grails plugin
        mainContext.registerAlias('marketplaceApplicationConfigurationService', 'ozoneConfiguration')

        configureLogging(applicationContext)

        accountService.runAsSystemUser {
            preload()

            runConversionTasks()

            if (Environment.current == Environment.TEST || Environment.current.toString().startsWith('with_')) {
                createUserProfileForTesting()
            }

            registerObjectMarshallers(servletContext)

            configureImportTasks()
        }
    }

    def destroy = { servletContext ->
        log.info "Bootstrap destroy"
        ApplicationContext apc = servletContext?.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        def quartzScheduler = apc?.getBean('quartzScheduler')
        quartzScheduler?.shutdown()
    }

    private void configureImportTasks() {
        InterfaceConfiguration.withTransaction {
            // Ensure that well-known InterfaceConfigurations exist in the database
            def configs = InterfaceConfiguration.list()
            if (!configs || (InterfaceConfiguration.findByName(Constants.FILE_BASED_IMPORT_EXECUTOR) == null)) {
                InterfaceConfiguration.FILE_IMPORT.save(flush: true, failOnError: true)
            }
            if (!configs || (InterfaceConfiguration.findByName(Constants.OMP_IMPORT_EXECUTOR) == null)) {
                InterfaceConfiguration.OMP_INTERFACE.save(flush: true, failOnError: true)
            }

            // Ensure that well-known ImportTasks in the database
            def importTasks = ImportTask.findByName(Constants.FILE_BASED_IMPORT_TASK)
            if (!importTasks) {
                // Find the correlated InterfaceConfig
                def fileConfig = InterfaceConfiguration.findByName(Constants.FILE_BASED_IMPORT_EXECUTOR)
                new ImportTask(
                        name: Constants.FILE_BASED_IMPORT_TASK, updateType: Constants.IMPORT_TYPE_FULL,
                        enabled: false, interfaceConfig: fileConfig).save(flush: true, failOnError: true)
            }
        }

        scheduledImportSchedulingService.updateImportTasksFromConfig()
    }

    private void createUserProfileForTesting() {
        def username = System.properties.user ?: "testUser1"
        //Create the user for integration tests since no one will physically log in
        new Profile(username: username).save()
    }

    private void configureLogging(ApplicationContext applicationContext) {
        if (Environment.current == Environment.PRODUCTION) {
            def log4jConfigure
            URL url = Loader.getResource('mp-override-log4j.xml')
            String fileName = url.toString()
            if (fileName.startsWith('file:/')) {
                File file
                try {
                    file = new File(url.toURI())
                } catch (URISyntaxException ignored) {
                    file = new File(url.getPath())
                }

                //if the file does not exist -- this really shouldn't happen
                //set url to null thus causing the default log4j file to be loaded
                if (!file.exists()) {
                    url = null
                }
                log4jConfigure = {
                    def watchTime = applicationContext?.getBean('OzoneConfiguration')?.getLog4jWatchTime()
                    println "########## Found mp-override-log4j.xml at: ${file.getAbsolutePath()} ${watchTime}"
                    DOMConfigurator.configureAndWatch(file.getAbsolutePath(), watchTime ? watchTime : 180000)
                }
            }
            else {
                url = null
            }

            if (!url) {
                url = Loader.getResource('mp-log4j.xml')
                log4jConfigure = {
                    println "########## Found mp-log4j.xml at: ${url.toString()}"
                    DOMConfigurator.configure(url)
                }
            }

            if (url) {
                try {
                    log4jConfigure()
                } catch (Throwable t) {
                    println "########## ${t.getMessage()}"
                }
            }
        }
    }

    private void runConversionTasks() {
        if (!marketplaceConversionService.upgrade11To20()) {
            throw new Exception("Failed to upgrade database to 2.0")
        }
        marketplaceConversionService.setOwfPropertiesDefault()
        marketplaceConversionService.setUUIDs()
    }

    private void registerObjectMarshallers(ServletContext servletContext) {
        AffiliatedMarketplace.contextPath = servletContext.contextPath

        // Domain Classes
        findMarshallableDomainClasses().each { clazz ->
            JSON.registerObjectMarshaller(clazz, { ToJSON it -> it.asJSON() })
        }

        // DTOs (and non-Domain Classes)
        [ProfileServiceItemTagDto,
         ItemCommentServiceItemDto].each { clazz ->
            JSON.registerObjectMarshaller(clazz, { ToJSON it -> it.asJSON() })
        }
    }

    private List<Class<? extends ToJSON>> findMarshallableDomainClasses() {
        findDomainArtefactClasses().findAll { ToJSON.isAssignableFrom(it) }
    }

    private List<Class> findDomainArtefactClasses() {
        grailsApplication.getArtefacts(DomainClassArtefactHandler.TYPE)*.clazz
    }

    //private method to register marshallers for domain objects
    private static Profile getOrCreateSystemUser() {
        Profile systemUser = Profile.getSystemUser()
        if (systemUser) {
            return systemUser
        }

        return new Profile(username: Profile.SYSTEM_USER_NAME).save(failOnError: true, flush: true)
    }

    void preload() {
        log.info "Preloading Database"
        def loader = new ApplicationConfigLoader(sessionFactory)
        loader.loadDefaultSettings()

        initializationService.createRequired()

//        marketplaceApplicationConfigurationService.initializeConfigDependentServices()
//        marketplaceApplicationConfigurationService.checkThatConfigsExist()
        messageSource.setBaseNameOrder(marketplaceApplicationConfigurationService.isFranchiseStore())

        preloadAvatars()
        preloadDefaultServiceItemIcon()
        preloadDefaultMarketplaceIcon()

        //Update isOutside flag if needed
        marketplaceConversionService.updateIsOutsideFlag()

        textService.manageRequiredTexts()
    }

    void preloadDefaultServiceItemIcon() {
        log.info "Checking to load a default ServiceItem Icon image"
        if (Images.countByTypeAndIsDefault(ImageType.SERVICEITEM, true) == 0) {
            log.info "Loading a default ServiceItem Icon image..."
            def typesImageLoc1 = 'resources/images/default_serviceitem_icon.png'
            log.info "Attempting to load a default ServiceItem Icon image from '${typesImageLoc1}'..."
            if (!handleAddNewImageFromFile(DefinedDefaultTypes.SERVICEITEM_ICON, typesImageLoc1, ImageType.SERVICEITEM)) {
                def typesImageLoc2 = "${COMMON_IMAGES_PATH}/default/default_serviceitem_icon.png"
                log.info "Attempting to load a default ServiceItem Icon image from '${typesImageLoc2}'..."
                if (!handleAddNewImageFromFile(DefinedDefaultTypes.SERVICEITEM_ICON, typesImageLoc2, ImageType.SERVICEITEM)) {
                    log.info "Could not find a default Service Item Icon image. Not loading."
                }
            }
        }
    }

    void preloadDefaultMarketplaceIcon() {
        log.info "Checking to load a default Marketplace Icon image"
        Images defaultMarketplaceImage = Images.findByTypeAndIsDefault(ImageType.MARKETPLACE_APP, true)
        def defaultMarketplaceIcon = 'resources/images/default_market_64x64.png'
        if (defaultMarketplaceImage == null) {
            log.info "Loading a default Marketplace Icon image..."
            log.info "Attempting to load a default Marketplace Icon image from '${defaultMarketplaceIcon}'..."
            if (!handleAddNewImageFromFile(DefinedDefaultTypes.MARKETPLACE_ICON, defaultMarketplaceIcon, ImageType.MARKETPLACE_APP)) {
                def typesImageLoc2 = "${COMMON_IMAGES_PATH}/themes/default/market_64x64.png"
                log.info "Attempting to load a default Marketplace Icon image from '${typesImageLoc2}'..."
                if (!handleAddNewImageFromFile(DefinedDefaultTypes.MARKETPLACE_ICON, typesImageLoc2, ImageType.MARKETPLACE_APP)) {
                    log.info "Could not find a default Marketplace Icon image. Not loading."
                }
            }
        } else if (isOldMarketplaceImage(defaultMarketplaceImage)) {
            File imageFile = imagesService.loadImageFileFromSystem(defaultMarketplaceIcon)
            defaultMarketplaceImage.imageSize = imageFile.length()
            defaultMarketplaceImage.bytes = imageFile.getBytes()
            defaultMarketplaceImage.contentType = Utils.getMimeType(imageFile.getName())
            defaultMarketplaceImage.save()
            log.info "Updated old Marketplace Icon image..."
        }
    }

    boolean isOldMarketplaceImage(Images image) {
        if (image)
            return image.type == ImageType.MARKETPLACE_APP && image.imageSize == Constants.OLD_MARKETPLACE_ICON_SIZE
        else
            return false
    }

    private boolean handleAddNewImageFromFile(definedDefaultType, imageFileName, imageType) {
        File f = imagesService.loadImageFileFromSystem(imageFileName)
        if (f && f.exists()) {
            def okcontents = ['image/png', 'image/jpeg', 'image/gif']
            if (!okcontents.contains(Utils.getMimeType(imageFileName))) {
                log.info "Image file '${imageFileName}' must be of content type: '${okcontents}'"
                return false
            } else {
                addNewDefaultImages(definedDefaultType, f, imageType)
                return true
            }
        } else {
            return false
        }
    }

    private addNewDefaultImages(definedDefaultType, imageFile, imageType) {
        try {
            def di = new DefaultImages(definedDefaultType, imageFile, imageType)
            di.save()
        }
        catch (Exception e) {
            // duplicate, just means server has been started before
            log.error "ERROR Adding Default Image. MSG: " + e
        }
    }

    private boolean handleAddNewAvatarFromFile(imageFileName, addDefaultAvatar, contentType) {
        File f = imagesService.loadImageFileFromSystem(imageFileName)
        if (f && f.exists()) {
            def avatar
            if (addDefaultAvatar) {
                avatar = new Avatar(contentType: contentType, isDefault: true)
            } else {
                avatar = Avatar.findByIsDefault(true)
            }

            if (avatar) {
                log.info "Loading Default Avatar"
                avatar.pic = f.getBytes()
                avatar.save(flush: true)
            } else {
                log.info "Could not find/create a default Avatar record. Not loading."
            }
            return true
        } else {
            return false
        }
    }

    def preloadAvatars() {
        log.info "Checking to load default Avatars"
        def addDefaultAvatar = false
        def updateDefaultAvatar = false

        if (Avatar.count() > 0) {
            def avatar = Avatar.findByIsDefault(true)
            if (avatar) {
                if (!avatar.pic) {
                    updateDefaultAvatar = true
                }
            }

        } else {
            addDefaultAvatar = true
        }

        if (addDefaultAvatar || updateDefaultAvatar) {
            def avatarLoc1 = 'resources/images/default_avatar.jpg'
            def avatarLoc2 = "${COMMON_IMAGES_PATH}/default/default_avatar.jpg"
            log.info "Loading a default Avatar..."
            log.info "Attempting to load a default avatar from '${avatarLoc1}'..."
            if (!handleAddNewAvatarFromFile(avatarLoc1, addDefaultAvatar, "image/jpeg")) {
                log.info "Attempting to load a default avatar from '${avatarLoc2}'..."
                if (!handleAddNewAvatarFromFile(avatarLoc2, addDefaultAvatar, "image/jpeg")) {
                    log.info "Could not find a default Avatar image. Not loading."
                }
            }
        }
    }

}
