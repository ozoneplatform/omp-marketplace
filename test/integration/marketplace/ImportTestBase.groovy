package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import java.text.SimpleDateFormat

import marketplace.data.ImportExecutorService
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import ozone.decorator.JSONDecoratorService


@TestMixin(IntegrationTestMixin)
class ImportTestBase extends MarketplaceIntegrationTestCase {

    def grailsApplication

	def marketplaceApplicationConfigurationService
    def changeLogService
	def customFieldDefinitionService
    def importExecutorService
	def extServiceItemService
	def imagesService
	def importService
	def importTaskService
	def profileService
	def itemCommentService
    def relationshipService
	def serviceItemService
	def serviceItemActivityService
	def typesService
    def jsonDecoratorService
    def owfWidgetTypesService

    def json
    def mockedConfig
    def sessionFactory

    def sdf  // SimpleDateFormat to use when putting dates into JSON with our std format

    /** Base test file for all tests; also required by util methods in this class */
    final String FULL_TEST_STRICT1 = "resources/marketplaceImportStub-strict1.json"
    final String DELTA_TEST_STRICT1 = "resources/marketplaceImportStub-delta1-strict.json"


	void setUp() {
        super.setUp()

        importService = new ImportService()
        imagesService = new ImagesService()

        importTaskService = new ImportTaskService()
        importTaskService.sessionFactory = sessionFactory

		profileService = new ProfileService()

        typesService = new TypesService()
        jsonDecoratorService = new JSONDecoratorService()
        jsonDecoratorService.applicationContext = grailsApplication.mainContext
        typesService.sessionFactory = sessionFactory
        typesService.imagesService = imagesService

		customFieldDefinitionService = new CustomFieldDefinitionService()
		changeLogService = new ChangeLogService()
		changeLogService.profileService = profileService

	    serviceItemActivityService = new ServiceItemActivityService()
	    serviceItemActivityService.sessionFactory = sessionFactory
	    serviceItemActivityService.changeLogService = changeLogService
	    serviceItemActivityService.profileService = profileService
	    serviceItemActivityService.accountService = accountService

		serviceItemService = new ServiceItemService()
		serviceItemService.sessionFactory = sessionFactory
		serviceItemService.accountService = accountService
		//serviceItemService.changeLogService = changeLogService
		serviceItemService.customFieldDefinitionService = customFieldDefinitionService
		serviceItemService.imagesService = imagesService
		serviceItemService.profileService = profileService
		serviceItemService.itemCommentService = itemCommentService
		serviceItemService.typesService = typesService
		//serviceItemService.serviceItemActivityService = serviceItemActivityService
        serviceItemService.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
        serviceItemService.owfWidgetTypesService = owfWidgetTypesService


		extServiceItemService = new ExtServiceItemService()
		extServiceItemService.accountService = accountService
		extServiceItemService.serviceItemService = serviceItemService
		extServiceItemService.customFieldDefinitionService = customFieldDefinitionService
        extServiceItemService.changeLogService = changeLogService
        extServiceItemService.JSONDecoratorService = jsonDecoratorService
        extServiceItemService.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
        extServiceItemService.owfWidgetTypesService = owfWidgetTypesService


        relationshipService.serviceItemService = serviceItemService
        relationshipService.serviceItemActivityService = serviceItemActivityService
        relationshipService.changeLogService = changeLogService

        importExecutorService = new ImportExecutorService()
        importExecutorService.accountService = accountService
        importExecutorService.extServiceItemService = extServiceItemService
        importExecutorService.serviceItemService = serviceItemService
        importExecutorService.customFieldDefinitionService = customFieldDefinitionService
        importExecutorService.relationshipService = relationshipService
        importExecutorService.JSONDecoratorService = jsonDecoratorService
        importExecutorService.sessionFactory = sessionFactory
        importExecutorService.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService


        // Create mock Config with default content
        mockedConfig = ConfigObject.newInstance()
        mockedConfig.marketplace.mpTypesImageMaxSize = 500000;

        // Ensure we have standard InterfaceConfigurations defined
        def configs = InterfaceConfiguration.list()
        if (!configs || configs.size() <= 0) {
            importExecutorService.log.debug "inserting standard InterfaceConfiguration records"
            InterfaceConfiguration.FILE_IMPORT.save(flush:true)
            InterfaceConfiguration.OMP_INTERFACE.save(flush:true)
        }

        // Configure our default External DateFormat for JSON date updates
        sdf = new SimpleDateFormat(Constants.EXTERNAL_DATE_FORMAT, Locale.US)
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
    }


    /**
     * Given a file on the classpath, read and return JSON
     * @param fileName
     * @return
     */
    def fileToJSON(def fileName) {
        def reader, data
        // Load file from classpath
        Resource resource = new ClassPathResource(fileName)
        def cpfile = resource.getFile()
        if (cpfile?.exists()) {
            reader = new FileReader(cpfile)
        }

        if (reader) {
            StringBuilder sb = new StringBuilder(4096)
            def l
            int cnt = 0
            try {
               while ((l = reader.read()) != -1) {
                   ++cnt
                   sb.append((char)l)
               }
            } finally {
                reader.close()
            }

            // Convert to JSON
            data = new JSONObject(sb.toString())
        }
        return data
    }

    /**
     * Given a String containing JSON and a filename, write the JSON to the file.
     * @param json
     * @param fileName
     * @return
     */
    def jsonToFile(String json, def prefix = "ImportRsp") {
        def file = File.createTempFile(prefix, ".json")
        importExecutorService.log.debug "Created file: ${file.absolutePath}"
        if (file.exists()) {
            file.delete()
        }
        file << (json)

        // File should be removed after use
        return file
    }

}
