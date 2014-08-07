import grails.converters.JSON
import grails.util.*

import marketplace.*

import org.apache.log4j.helpers.*
import org.apache.log4j.xml.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as confHolder
import org.codehaus.groovy.grails.web.json.*
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import ozone.marketplace.enums.DefinedDefaultTypes;
import ozone.marketplace.enums.ImageType;
import ozone.utils.Utils

class BootStrap {
    def serviceItemService
    def marketplaceConversionService
	def searchableService
	def imagesService
	def marketplaceApplicationConfigurationService
    def profileService
    def textService
    def scheduledImportSchedulingService
    def grailsApplication

    def messageSource
    def commonImagesLoc = '/themes/common/images'
    def sessionFactory

    def init = { servletContext ->

        // setting it to a million clauses by default
        // http://stackoverflow.com/questions/1534789/help-needed-figuring-out-reason-for-maxclausecount-is-set-to-1024-error
        org.apache.lucene.search.BooleanQuery.setMaxClauseCount(10 ** 6);

        ApplicationContext apc = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)

        //Register an alias to the configuration service. This provides a common convention for accessing
        //the service from, for example, a grails plugin
        grailsApplication.mainContext.registerAlias('marketplaceApplicationConfigurationService', 'ozoneConfiguration')

        if (GrailsUtil.environment == 'production') {
            def log4jConfigure
            URL url = Loader.getResource('mp-override-log4j.xml')
            String fileName = url.toString()
            if (fileName.startsWith('file:/')) {

                File file;
                try {
                    file = new File(url.toURI());
                } catch (URISyntaxException e) {
                    file = new File(url.getPath());
                }

                //if the file does not exist -- this really shouldn't happen
                //set url to null thus causing the default log4j file to be loaded
                if (!file.exists()) {
                    url = null
                }
                log4jConfigure = {
                    def watchTime = apc?.getBean('OzoneConfiguration')?.getLog4jWatchTime();
                    println "########## Found mp-override-log4j.xml at: ${file.getAbsolutePath()} ${watchTime}"
                    DOMConfigurator.configureAndWatch(file.getAbsolutePath(), watchTime ? watchTime : 180000)
                }
            } else
                url = null
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
        preload()
        if (!marketplaceConversionService.upgrade11To20()) {
            throw new Exception("Failed to upgrade database to 2.0")
        }
        marketplaceConversionService.setOwfPropertiesDefault()
        marketplaceConversionService.setUUIDs()

		log.info "BootStrap init; GrailsUtil.environment: ${GrailsUtil.environment}"
        if (GrailsUtil.environment == "test" || GrailsUtil.environment.startsWith('with_')) {
			def username = System.properties.user ?: "testUser1"
			//Create the user for integration tests since no one will physically log in
			new Profile(username:username).save()
		}

        registerObjectMashaller(marketplace.ServiceItem)
        registerObjectMashaller(marketplace.ExtServiceItem)
        registerObjectMashaller(marketplace.Category)
        registerObjectMashaller(marketplace.CustomFieldDefinition)
        registerObjectMashaller(marketplace.RejectionJustification)
        registerObjectMashaller(marketplace.RejectionListing)
        registerObjectMashaller(marketplace.State)
        registerObjectMashaller(marketplace.Text)
        registerObjectMashaller(marketplace.Images)
        registerObjectMashaller(marketplace.Profile)
        registerObjectMashaller(marketplace.ExtProfile)
        registerObjectMashaller(marketplace.IntentAction)
        registerObjectMashaller(marketplace.IntentDataType)
        registerObjectMashaller(marketplace.OwfWidgetTypes)
        registerObjectMashaller(marketplace.Types)
        registerObjectMashaller(marketplace.Agency)
        registerObjectMashaller(marketplace.ItemComment)
        registerObjectMashaller(marketplace.AffiliatedMarketplace, "${servletContext.contextPath}")
        registerObjectMashaller(marketplace.Contact)
        registerObjectMashaller(marketplace.ContactType)
        registerObjectMashaller(marketplace.ScoreCardItem)

        [
            marketplace.ServiceItemActivity,
            marketplace.ModifyRelationshipActivity,
            marketplace.RejectionActivity,
            marketplace.ServiceItemTag,
            marketplace.rest.ItemCommentServiceItemDto,
            marketplace.rest.ProfileServiceItemTagDto
        ].each { Class ->
            JSON.registerObjectMarshaller(Class, { it.asJSON() })
        }

        def importTasks
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
            importTasks = ImportTask.findByName(Constants.FILE_BASED_IMPORT_TASK)
            if (!importTasks) {
                // Find the correlated InterfaceConfig
                def fileConfig = InterfaceConfiguration.findByName(Constants.FILE_BASED_IMPORT_EXECUTOR)
                new ImportTask(name: Constants.FILE_BASED_IMPORT_TASK, updateType: Constants.IMPORT_TYPE_FULL,
                    enabled: false, interfaceConfig: fileConfig).save(flush: true, failOnError: true)
            }
        }

        scheduledImportSchedulingService.updateImportTasksFromConfig()
    }

    //private method to register marshallers for domain objects
    def registerObjectMashaller(def clazz, def contextPath = null) {
        JSON.registerObjectMarshaller(clazz) { object ->
            JSONObject returnValue = contextPath != null ? object.asJSON(contextPath) : object.asJSON()
            // TODO: probably should just do this in the asJSON methods!
            JSONUtil.addCreatedAndEditedInfo(returnValue, object)
            return returnValue
        }
    }

    def destroy = { servletContext ->
        log.info "Bootstrap destroy"
        ApplicationContext apc = servletContext?.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        def quartzScheduler = apc?.getBean('quartzScheduler')
        quartzScheduler?.shutdown()
    }

    //TODO ideally all this would be in one service, probably in applicationConfigurationService so they are all in one transaction
    def preload = { db ->
        log.info "Preloading Database: ${db}"

        marketplaceApplicationConfigurationService.checkThatConfigsExist()
        messageSource.setBaseNameOrder(marketplaceApplicationConfigurationService.isFranchiseStore())

        marketplaceApplicationConfigurationService.createRequired()
        marketplaceApplicationConfigurationService.initializeConfigDependentServices()

        def system_user = Profile.getSystemUser()
        confHolder.config.system_user_id = system_user.id  //What is this?  conf holder is a singleton, this is bad

        preloadAvatars()
        preloadDefaultServiceItemIcon()
        preloadDefaultMarketplaceIcon()

        //Update isOutside flag if needed
        marketplaceConversionService.updateIsOutsideFlag()

        textService.manageRequiredTexts()
    }

    def preloadDefaultServiceItemIcon() {
        log.info "Checking to load a default ServiceItem Icon image"
        if (Images.countByTypeAndIsDefault(ImageType.SERVICEITEM, true) == 0) {
            log.info "Loading a default ServiceItem Icon image..."
            def typesImageLoc1 = 'resources/images/default_serviceitem_icon.png'
            log.info "Attempting to load a default ServiceItem Icon image from '${typesImageLoc1}'..."
            if (!handleAddNewImageFromFile(DefinedDefaultTypes.SERVICEITEM_ICON, typesImageLoc1, ImageType.SERVICEITEM)) {
                def typesImageLoc2 = "${commonImagesLoc}/default/default_serviceitem_icon.png"
                log.info "Attempting to load a default ServiceItem Icon image from '${typesImageLoc2}'..."
                if (!handleAddNewImageFromFile(DefinedDefaultTypes.SERVICEITEM_ICON, typesImageLoc2, ImageType.SERVICEITEM)) {
                    log.info "Could not find a default Service Item Icon image. Not loading."
                }
            }
        }
    }



    def preloadDefaultMarketplaceIcon() {
        log.info "Checking to load a default Marketplace Icon image"
        Images defaultMarketplaceImage = Images.findByTypeAndIsDefault(ImageType.MARKETPLACE_APP, true)
        def defaultMarketplaceIcon = 'resources/images/default_market_64x64.png'
        if (defaultMarketplaceImage == null) {
            log.info "Loading a default Marketplace Icon image..."
            log.info "Attempting to load a default Marketplace Icon image from '${defaultMarketplaceIcon}'..."
            if (!handleAddNewImageFromFile(DefinedDefaultTypes.MARKETPLACE_ICON, defaultMarketplaceIcon, ImageType.MARKETPLACE_APP)) {
                def typesImageLoc2 = "${commonImagesLoc}/themes/default/market_64x64.png"
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
            def avatarLoc2 = "${commonImagesLoc}/default/default_avatar.jpg"
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
