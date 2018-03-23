package marketplace.rest

import grails.testing.services.ServiceUnitTest
import grails.plugins.executor.PersistenceContextExecutorWrapper

import org.springframework.beans.factory.annotation.Autowired

import org.hibernate.SessionFactory

import marketplace.AccountService
import marketplace.Agency
import marketplace.ChangeDetail
import marketplace.Constants.Action
import marketplace.Contact
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.Intent
import marketplace.IntentAction
import marketplace.IntentDataType
import marketplace.MarketplaceMessagingService
import marketplace.ModifyRelationshipActivity
import marketplace.OwfProperties
import marketplace.Profile
import marketplace.RejectionActivity
import marketplace.RejectionJustification
import marketplace.RejectionListing
import marketplace.Relationship
import marketplace.Screenshot
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemDocumentationUrl
import marketplace.ServiceItemTag
import marketplace.State
import marketplace.Tag
import marketplace.Types
import marketplace.data.ServiceItemDataService
import marketplace.validator.ServiceItemValidator

import org.ozoneplatform.appconfig.server.service.api.ApplicationConfigurationService

import static marketplace.Constants.INSIDE_OUTSIDE_ADMIN_SELECTED
import static ozone.marketplace.enums.MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR


class ServiceItemRestService_ChangeDetailsSpec
        extends RestServiceSpecification
        implements ServiceUnitTest<ServiceItemRestService>
{

    private static final boolean IS_VERBOSE = System.getenv("verbose")?.toBoolean() ?: false

    Closure doWithSpring() {
        { ->
            auditableDataBindingListener(AuditableDataBindingListener)
            serviceItemDataBindingListener(ServiceItemDataBindingListener)

            xmlns spock: 'http://www.spockframework.org/spring'

            spock.mock(id: 'serviceItemActivityInternalService', class: ServiceItemActivityInternalService.name)
            spock.mock(id: 'serviceItemDataService', class: ServiceItemDataService.name)

            spock.mock(id: 'serviceItemValidator', class: ServiceItemValidator.name)
            spock.mock(id: 'accountService', class: AccountService.name)
            spock.mock(id: 'marketplaceApplicationConfigurationService', class: ApplicationConfigurationService.name)
            spock.mock(id: 'executorService', class: PersistenceContextExecutorWrapper.name)
            spock.mock(id: 'sessionFactory', class: SessionFactory.name)
        }
    }

    ServiceItemActivityInternalService serviceItemActivityInternalService
    ServiceItemDataService serviceItemDataService

    @Autowired @Detached
    ApplicationConfigurationService marketplaceApplicationConfigurationService

    Profile owner
    Profile admin

    Types type1
    State activeState
    State inactiveState
    Agency agency1

    def setup() {
        mockDomain ServiceItem
        mockDomain ServiceItemActivity
        mockDomain ModifyRelationshipActivity
        mockDomain ChangeDetail
        mockDomain CustomField
        mockDomain ServiceItemDocumentationUrl
        mockDomain Screenshot
        mockDomain Contact
        mockDomain Tag
        mockDomain ServiceItemTag
        mockDomain OwfProperties
        mockDomain Relationship
        mockDomain RejectionListing
        mockDomain RejectionActivity
        mockDomain RejectionJustification
        mockDomain Types
        mockDomain State
        mockDomain Intent
        mockDomain CustomFieldDefinition
        mockDomain Profile

        setupMocks()
        setupData()
    }

    private void setupMocks() {
        serviceItemActivityInternalService = new ServiceItemActivityInternalService()
        serviceItemActivityInternalService.accountService = accountService
        serviceItemActivityInternalService.marketplaceMessagingService = Mock(MarketplaceMessagingService)
        service.serviceItemActivityInternalService = serviceItemActivityInternalService

        serviceItemDataService = new ServiceItemDataService()
        service.serviceItemDataService = serviceItemDataService

        marketplaceApplicationConfigurationService.valueOf(INSIDE_OUTSIDE_BEHAVIOR) >> INSIDE_OUTSIDE_ADMIN_SELECTED
    }

    private void setupData() {
        owner = save createUser('owner')
        admin = save createAdmin('admin')

        type1 = save new Types(title: 'Test Type', ozoneAware: true)

        activeState = save new State(title: 'Active')
        inactiveState = save new State(title: 'Inactive')

        saveStub new Intent(action: new IntentAction(title: 'run', description: 'run'),
                            dataType: new IntentDataType(title: 'text/plain', description: 'plain text'))

        saveStub new CustomFieldDefinition(name: "test custom field definition",
                                           label: 'Custom Field Def Label',
                                           types: [type1])

        agency1 = new Agency(title: 'Test Agency').save(failOnError: true, flush: true)
    }

    def "updateById: add to owners"() {
        given:
        loggedInAs admin

        def serviceItem = save makeServiceItem([owners: [owner], title: "foo"])

        assert ServiceItemActivity.count() == 0

        when:
        bindData(serviceItem, [owners: [owner, admin]])
        def result = service.updateById(serviceItem.id, serviceItem)

        def activities = ServiceItemActivity.findAll()

        if (IS_VERBOSE) activities.each { println Formatters.formatServiceItemActivity(it) }

        then:
        result != null

        activities.size() == 1
        def activity = activities[0]
        activity.id != null
        activity.action == Action.MODIFIED
        activity.serviceItem.id == serviceItem.id

        activity.changeDetails.size() == 1
        with(activity.changeDetails[0], ChangeDetail) {
            id != null
            fieldName == "owners"
            oldValue == "[owner]"
            newValue == "[owner, admin]"
        }
    }

    def "updateById: change OwfProperties"() {
        given:
        loggedInAs admin

        def serviceItem = save makeServiceItem([title: "foo"])

        when:
        bindData(serviceItem, [owfProperties: [background: true]])
        def result = service.updateById(serviceItem.id, serviceItem)

        def activities = ServiceItemActivity.findAll()

        if (IS_VERBOSE) activities.each { activity -> println(Formatters.formatServiceItemActivity(activity))
        }

        then:
        result != null

        activities.size() == 1
        def activity = activities[0]
        activity.id != null
        activity.action == Action.MODIFIED
        activity.serviceItem.id == serviceItem.id

        activity.changeDetails.size() == 1
        with(activity.changeDetails[0], ChangeDetail) {
            id != null
            fieldName == "background"
            oldValue == "false"
            newValue == "true"
        }
    }

    private ServiceItem makeServiceItem(Map extra = [:]) {
        def owfProps = makeOwfProperties((Map) extra.remove('owfProperties') ?: [:])

        def props = [owners       : [owner],
                     types        : type1,
                     state        : activeState,
                     owfProperties: owfProps,
                     agency       : agency1]

        def serviceItem = new ServiceItem(DEFAULT_SERVICE_ITEM_PROPS + props + extra)

        serviceItem.createdBy = (Long) extra['createdBy']

        owfProps.serviceItem = serviceItem

        serviceItem
    }

    private static OwfProperties makeOwfProperties(Map extra = [:]) {
        Map props = [height: 200, width: 300]

        return new OwfProperties(props + extra)
    }

    private static final DEFAULT_SERVICE_ITEM_PROPS = [title      : "test service item",
                                                       description: "a test service item",
                                                       launchUrl  : "https://localhost/asf",
                                                       versionName: '1',
                                                       isEnabled  : true,
                                                       isOutside  : true,
                                                       techPocs   : ['poc']]
}
