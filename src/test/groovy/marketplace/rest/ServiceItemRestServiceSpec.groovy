package marketplace.rest

import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.testing.spring.AutowiredTest
import grails.web.databinding.DataBinder
import org.grails.datastore.gorm.GormEntity
import grails.plugins.executor.PersistenceContextExecutorWrapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException

import org.hibernate.SessionFactory

import marketplace.AccountService
import marketplace.Agency
import marketplace.ChangeDetail
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

import ozone.marketplace.enums.RelationshipType
import ozone.utils.User
import org.ozoneplatform.appconfig.server.service.api.ApplicationConfigurationService

import static marketplace.Constants.*
import static ozone.marketplace.enums.MarketplaceApplicationSetting.ALLOW_OWNER_TO_EDIT_APPROVED_LISTING
import static ozone.marketplace.enums.MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR


class ServiceItemRestServiceSpec
        extends Specification
        implements ServiceUnitTest<ServiceItemRestService>, AutowiredTest, DataTest, DataBinder
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
    ServiceItemValidator serviceItemValidator

    @Autowired @Detached
    AccountService accountService

    @Autowired @Detached
    ApplicationConfigurationService marketplaceApplicationConfigurationService

    private static final String APPROVED = APPROVAL_STATUSES['APPROVED']
    private static final String IN_PROGRESS = APPROVAL_STATUSES['IN_PROGRESS']
    private static final String PENDING = APPROVAL_STATUSES['PENDING']
    private static final String REJECTED = APPROVAL_STATUSES['REJECTED']

    Profile currentUser
    Profile owner
    Profile nonOwner
    Profile externalAdmin
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
        owner = new Profile(username: 'owner').save()
        nonOwner = new Profile(username: 'nonOwner').save()
        admin = new Profile(username: 'admin').save()
        externalAdmin = new Profile(username: 'external').save()
        currentUser = admin

        type1 = save new Types(title: 'Test Type', ozoneAware: true)

        activeState = save new State(title: 'Active')
        inactiveState = save new State(title: 'Inactive')

        new Intent(action: new IntentAction(title: 'run', description: 'run'),
                   dataType: new IntentDataType(title: 'text/plain', description: 'plain text')).save()

        new CustomFieldDefinition(name: "test custom field definition",
                                  label: 'Custom Field Def Label',
                                  types: [type1]).save()

        agency1 = new Agency(title: 'Test Agency').save(failOnError: true, flush: true)
    }

    private static Object firstArg(Object[] args, Object defaultValue = null) {
        (args == null || args.size() < 1) ? defaultValue : args[0] ?: defaultValue
    }

    private void loggedInAs(Profile profile, Map userExtra = [:]) {
        def user = new User([username: profile.username, org: "SHIELD"] + userExtra)
        accountService.getLoggedInUser() >> user
        accountService.getProperty('loggedInUser') >> user

        accountService.findLoggedInUserProfile() >> { profile }
        accountService.getLoggedInUserProfileId() >> { profile.id }
        accountService.getProperty('loggedInUserProfileId') >> { profile.id }

        accountService.checkAdmin(*_) >> { Object[] args ->
            String message = firstArg(args, "Attempt to access Admin-only functionality")
            if (profile.id != admin.id) throw new AccessDeniedException(message)
        }

        accountService.isAdmin() >> { profile.is(admin) }
        accountService.isExtAdmin() >> { profile.is(externalAdmin) }
    }

    private void isOwnerAllowedToEditApprovedListing(boolean value) {
        marketplaceApplicationConfigurationService.is(ALLOW_OWNER_TO_EDIT_APPROVED_LISTING) >> value
    }

    private static <T extends GormEntity> T save(T entity) {
        entity.save(failOnError: true)
    }

    def "createFromDto(): user can create new listing"() {
        given:
        loggedInAs owner

        when:
        def result = service.createFromDto(makeServiceItem())

        then:
        notThrown(Exception)

        result != null
    }

    def "createFromDto(): new listing creates CREATED activity"() {
        given:
        loggedInAs owner

        when:
        def result = service.createFromDto(makeServiceItem())

        then:
        def activities = ServiceItemActivity.findAll()
        activities.size() == 1

        if (IS_VERBOSE) println(Formatters.formatServiceItemActivity(activities[0]))

        with(activities[0]) {
            action == Action.CREATED
            serviceItemId == result.id
            authorId == this.owner.id
            changeDetails == null
        }
    }

    def "createFromDto(): admin can create new listing"() {
        given:
        loggedInAs admin

        when:
        def result = service.createFromDto(makeServiceItem())

        then:
        notThrown(Exception)

        result != null
    }

    void "createFromDto(): when INSIDE_OUTSIDE_ALL_OUTSIDE is set, isOutside becomes true"() {
        given:
        loggedInAs owner

        when:
        def serviceItem = makeServiceItem([isOutside: false])
        def result = service.createFromDto(serviceItem)

        then:
        marketplaceApplicationConfigurationService.valueOf(INSIDE_OUTSIDE_BEHAVIOR) >> INSIDE_OUTSIDE_ALL_OUTSIDE

        and:
        result != null
        result.isOutside
    }

    void "createFromDto(): when INSIDE_OUTSIDE_ALL_INSIDE is set, isOutside becomes false"() {
        given:
        loggedInAs owner

        when:
        def serviceItem = makeServiceItem([isOutside: true])
        def result = service.createFromDto(serviceItem)

        then:
        marketplaceApplicationConfigurationService.valueOf(INSIDE_OUTSIDE_BEHAVIOR) >> INSIDE_OUTSIDE_ALL_INSIDE

        and:
        result != null
        !result.isOutside
    }

    void "createFromDto(): default properties are created if required"() {
        given:
        loggedInAs owner, [org: "My Org"]

        when:
        def serviceItem = makeServiceItem([owners: null, techPocs: null, organization: null, state: null])

        def result = service.createFromDto(serviceItem)

        then:
        result.owners == [owner]
        result.techPocs == [owner.username] as Set
        result.organization == "My Org"
        result.state == activeState
    }

    void "createFromDto(): default properties do not overwrite explicit properties"() {
        given:
        loggedInAs owner, [org: "My Org"]

        when:
        def serviceItem =
                makeServiceItem([owners: [nonOwner], techPocs: ["foo"], organization: "some org", state: inactiveState])

        def result = service.createFromDto(serviceItem)

        then:
        result.owners == [nonOwner]
        result.techPocs == ["foo"] as Set
        result.organization == "some org"
        result.state == inactiveState
    }

    def "updateById(): user can update own listing"() {
        given:
        loggedInAs owner
        isOwnerAllowedToEditApprovedListing false

        def original = save makeServiceItem([owners: [owner]])

        when:
        def updated = clone(original)

        service.updateById(original.id, updated)

        then:
        notThrown(AccessDeniedException)
    }

    def "updateById(): when Edit Approved Listing disallowed, owner can not update after Approved"() {
        given:
        loggedInAs owner
        isOwnerAllowedToEditApprovedListing false

        def original = save makeServiceItem([owners        : [owner],
                                             approvalStatus: APPROVED])

        when:
        service.updateById(original.id, clone(original))

        then:
        thrown(AccessDeniedException)
    }

    def "updateById(): when Edit Approved Listing allowed, owner can update after Approved"() {
        given:
        loggedInAs owner
        isOwnerAllowedToEditApprovedListing true

        def original = save makeServiceItem([owners        : [owner],
                                             approvalStatus: APPROVED])

        when:
        bindData(original, [title: "my new title"])
        service.updateById(original.id, original)

        then:
        notThrown(AccessDeniedException)
    }

    def "updateById(): user can not update other user listing"() {
        given:
        loggedInAs nonOwner

        def original = save makeServiceItem([owners: [owner]])

        when:
        service.updateById(original.id, clone(original))

        then:
        thrown(AccessDeniedException)
    }

    def "updateById(): admin can update other user listing"() {
        given:
        loggedInAs admin

        def original = save makeServiceItem([owners: [owner]])

        when:
        service.updateById(original.id, clone(original))

        then:
        notThrown(AccessDeniedException)
    }

    def "updateById(): external admin can not update other user listing"() {
        given:
        loggedInAs externalAdmin

        def original = save makeServiceItem([owners: [owner]])

        when:
        bindData(original, [title: "my new title"])
        service.updateById(original.id, original)

        then:
        thrown(AccessDeniedException)
    }

    def "updateById(): external admin can update own listing"() {
        given:
        loggedInAs externalAdmin

        def original = save makeServiceItem([createdBy: externalAdmin.id])

        when:
        bindData(original, [title: "my new title"])
        service.updateById(original.id, original)

        then:
        notThrown(AccessDeniedException)
    }

    void "updateById(): user can not approve a listing"() {
        given:
        loggedInAs owner

        def original = save makeServiceItem([approvalStatus: PENDING])

        when:
        bindData(original, [approvalStatus: APPROVED])
        service.updateById(original.id, original)

        then:
        thrown(AccessDeniedException)

        and: "no activity created"
        ServiceItemActivity.count() == 0
    }

    void "updateById(): admin can approve a listing"() {
        given:
        loggedInAs admin

        def original = save makeServiceItem([approvalStatus: PENDING])

        when:
        bindData(original,[approvalStatus: APPROVED])
        def result = service.updateById(original.id, original)

        then: "result has updated approval status and date"
        notThrown(AccessDeniedException)

        result.approvalStatus == APPROVED
        result.approvalDate.time > (new Date()).time - 1000

        and: "change persisted to database"
        ServiceItem.get(original.id)?.approvalStatus == APPROVED
    }

    void "updateById(): approve listing creates APPROVED activity"() {
        given:
        loggedInAs admin

        def original = save makeServiceItem([approvalStatus: PENDING])

        when:
        bindData(original,[approvalStatus: APPROVED])
        service.updateById(original.id, original)

        then:
        def activities = ServiceItemActivity.findAll()
        activities.size() == 1

        if (IS_VERBOSE) println(Formatters.formatServiceItemActivity(activities[0]))

        with(activities[0]) {
            action == Action.APPROVED
            serviceItemId == original.id
            authorId == this.admin.id
            changeDetails == null
        }
    }


    def "reject(): user can not reject a listing"() {
        given:
        loggedInAs owner

        def original = save makeServiceItem([approvalStatus: PENDING])

        when:
        def rejection = save makeRejectionListing([serviceItem: original, author: owner])

        service.reject(original, rejection)

        then:
        thrown(AccessDeniedException)

        and: "no change persisted to database"
        ServiceItem.get(original.id)?.approvalStatus == PENDING

        and: "no activity created"
        ServiceItemActivity.count() == 0
    }

    void "reject(): admin can reject a listing"() {
        given:
        loggedInAs admin

        def original = save makeServiceItem([approvalStatus: PENDING])

        when:
        def rejection = makeRejectionListing([serviceItem: original, author: admin])

        service.reject(original, rejection)

        then: "change persisted to database"
        notThrown(AccessDeniedException)

        def serviceItem = ServiceItem.get(original.id)
        serviceItem?.approvalStatus == REJECTED

        and:
        def activities = ServiceItemActivity.findAll()
        activities.size() == 1

        def activity = activities[0]

        if (IS_VERBOSE) println(Formatters.formatServiceItemActivity(activity))

        with(activities[0], RejectionActivity) {
            action == Action.REJECTED
            serviceItemId == original.id
            authorId == this.admin.id
            changeDetails.isEmpty()

            rejectionListing != null
            rejectionListing.serviceItemId == original.id
            rejectionListing.authorId == this.admin.id
            rejectionListing.description == rejection.description

            rejectionListing.justification != null
            rejectionListing.justification.title == rejection.justification.title
            rejectionListing.justification.description == rejection.justification.description

            serviceItem.rejectionListings == [rejectionListing] as Set
        }
    }

    @Unroll
    void "updateById(): changing isOutside from '#initial' to '#toValue' creates #actionType activity"(Boolean initial,
                                                                                                       Boolean toValue,
                                                                                                       Action actionType)
    {
        given:
        loggedInAs owner

        def original = save makeServiceItem([isOutside: initial])

        when:
        bindData(original, [isOutside: toValue])
        def result = service.updateById(original.id, original)

        then:
        result?.isOutside == toValue

        and:
        def activities = ServiceItemActivity.findAll()
        activities.size() == 1

        if (IS_VERBOSE) println(Formatters.formatServiceItemActivity(activities[0]))

        with(activities[0]) {
            action == actionType
            serviceItemId == original.id
            authorId == this.owner.id
            changeDetails == null
        }

        where:
        initial | toValue || actionType
        null    | true    || Action.OUTSIDE
        false   | true    || Action.OUTSIDE
        null    | false   || Action.INSIDE
        true    | false   || Action.INSIDE
    }

    @Unroll
    void "updateById(): changing isEnabled from '#initial' to '#toValue' creates #actionType activity"(Boolean initial,
                                                                                                       Boolean toValue,
                                                                                                       Action actionType)
    {
        given:
        loggedInAs owner

        def original = save makeServiceItem([isEnabled: initial])

        when:
        bindData(original,[isEnabled: toValue])
        def result = service.updateById(original.id, original)

        then:
        result?.isEnabled == toValue

        and:
        def activities = ServiceItemActivity.findAll()
        activities.size() == 1

        if (IS_VERBOSE) println(Formatters.formatServiceItemActivity(activities[0]))

        with(activities[0]) {
            action == actionType
            serviceItemId == original.id
            authorId == this.owner.id
            changeDetails == null
        }

        where:
        initial | toValue || actionType
        false   | true    || Action.ENABLED
        true    | false   || Action.DISABLED
    }

    void testUpdateRelationshipServiceItemActivity() {
        given:
        loggedInAs owner

        def serviceItem1 = service.createFromDto(makeServiceItem())

        def relatedItem1 = service.createFromDto(makeServiceItem())
        def relatedItem2 = service.createFromDto(makeServiceItem())

        def ignoredActivities = ServiceItemActivity.findAll() as Set

        when:
        def relationship = makeRelationship(serviceItem1, [relatedItem1, relatedItem2])

        bindData(serviceItem1, [relationships: [relationship]])
        def result = service.updateById(serviceItem1.id, serviceItem1)

        then:
        def activities = ((ServiceItemActivity.findAll() as Set) - ignoredActivities) as List

        if (IS_VERBOSE) activities.each { println Formatters.formatServiceItemActivity(it) }

        activities.size() == 3

        activities[0].serviceItemId == serviceItem1.id
        activities[0].action == Action.ADDRELATEDITEMS

        activities[1].serviceItemId == relatedItem1.id
        activities[1].action == Action.ADDRELATEDTOITEM

        activities[2].serviceItemId == relatedItem2.id
        activities[2].action == Action.ADDRELATEDTOITEM
    }

    void testUpdateRelationshipServiceItemActivity2() {
        given:
        loggedInAs owner

        def serviceItem1 = service.createFromDto(makeServiceItem())

        def relatedItem1 = service.createFromDto(makeServiceItem())
        def relatedItem2 = service.createFromDto(makeServiceItem())

        def relationship1 = makeRelationship(serviceItem1, [relatedItem1, relatedItem2])
        def relationship2 = makeRelationship(serviceItem1, [relatedItem1])

        def ignoredActivities = ServiceItemActivity.findAll() as Set

        when:
        bindData(serviceItem1, [relationships: [relationship1]])
        def result1 = service.updateById(serviceItem1.id, serviceItem1)

        then:
        def activities = ((ServiceItemActivity.findAll() as Set) - ignoredActivities) as List

        if (IS_VERBOSE) activities.each { println Formatters.formatServiceItemActivity(it) }

        activities.size() == 3
        activities*.action == [Action.ADDRELATEDITEMS, Action.ADDRELATEDTOITEM, Action.ADDRELATEDTOITEM]
    }

    def <T extends GormEntity> T bind(T entity, Object source) {
        def persistent = entity.class.get(entity.ident())
        if (persistent == null) return null

        bindData(persistent, source)

        (T) persistent
    }

    void "updateById(): remove a relationship adds activity"() {
        given:
        loggedInAs owner

        def relatedItem1 = save makeServiceItem()
        def relatedItem2 = save makeServiceItem()

        def serviceItem1 = makeServiceItem([isOutside: true])
        def relationship = save makeRelationship(serviceItem1, [relatedItem1, relatedItem2])
        serviceItem1.addToRelationships(relationship)
        save serviceItem1

        def ignoredActivities = ServiceItemActivity.findAll() as Set

        when:
        bindData(serviceItem1, [relationships: [[id: relationship.id, relatedItems: [relatedItem1]]]])

        def result = service.updateById(serviceItem1.id, serviceItem1)

        then:
        def activities = ((ServiceItemActivity.findAll() as Set) - ignoredActivities) as List

        if (IS_VERBOSE) activities.each { println Formatters.formatServiceItemActivity(it) }

        activities.size() == 2

        activities[0].serviceItemId == serviceItem1.id
        activities[0].action == Action.REMOVERELATEDITEMS

        activities[1].serviceItemId == relatedItem2.id
        activities[1].action == Action.REMOVERELATEDTOITEM
    }

    private Set<Long> getRequired(Long id, boolean blockInsideListings = false) {
        service.getAllRequiredServiceItemsByParentId(id, blockInsideListings)
               .collect { it.id } as Set
    }

    private Relationship makeRelationship(ServiceItem owner, List<ServiceItem> related = null) {
        new Relationship(owningEntity: owner,
                         relationshipType: RelationshipType.REQUIRE,
                         relatedItems: related ?: [])
    }

    void testGetAllRequiredServiceItemsByParentId() {
        given:
        loggedInAs owner

        when:
        def serviceItem1 = service.createFromDto(makeServiceItem())

        def relatedItem1 = service.createFromDto(makeServiceItem())
        def relatedItem2 = service.createFromDto(makeServiceItem([isOutside: false]))

        def relationship = save makeRelationship(serviceItem1, [relatedItem1, relatedItem2])

        def dto = clone(serviceItem1, [relationships: [relationship]])
        def result = service.updateById(serviceItem1.id, dto)

        then:
        getRequired(serviceItem1.id) == [relatedItem1.id, relatedItem2.id] as Set
        getRequired(serviceItem1.id, true) == [relatedItem1.id] as Set
    }

    // TODO: Come back later
    @Ignore
    void testGetAllRequiredServiceItemsByParentIdCyclicDependency() {
        given:
        loggedInAs owner

        when:
        def makeRelationship = { ids = null ->
            new Relationship(relationshipType: RelationshipType.REQUIRE,
                             relatedItems: ids.collect {
                                 def si = new ServiceItem()
                                 si.id = it
                                 si
                             } ?: [])
        }

        def id

        def getRequired = { blockInsideListings ->
            service.getAllRequiredServiceItemsByParentId(id, blockInsideListings)
                   .collect { it.id } as Set
        }

        def relatedItem1Id = service.createFromDto(makeServiceItem()).id
        def relatedItem2Id = service.createFromDto(makeServiceItem()).id

        def relatedItem1Dto = makeServiceItem()
        relatedItem1Dto.id = relatedItem1Id
        relatedItem1Dto.relationships = [makeRelationship([relatedItem2Id])]

        def relatedItem2Dto = makeServiceItem()
        relatedItem2Dto.id = relatedItem2Id
        relatedItem2Dto.relationships = [makeRelationship([relatedItem1Id])]

        def relatedItem1 = service.updateById(relatedItem1Id, relatedItem1Dto)
        def relatedItem2 = service.updateById(relatedItem2Id, relatedItem2Dto)

        def dto = makeServiceItem()
        dto.relationships = [makeRelationship([relatedItem1Id])]
        id = service.createFromDto(dto).id

        then:
        getRequired() == [relatedItem1.id, relatedItem2.id] as Set
    }

    def "getAllByAuthorId(): for other user, returns only approved and enabled listings"() {
        given:
        loggedInAs nonOwner

        save makeServiceItem([approvalStatus: IN_PROGRESS])
        save makeServiceItem([approvalStatus: PENDING])
        save makeServiceItem([approvalStatus: APPROVED, isEnabled: false])
        ServiceItem approved = makeServiceItem([approvalStatus: APPROVED, isEnabled: true]).save(flush: true)

        when:
        def result = service.getAllByAuthorId(owner.id)

        then:
        result.size() == 1
        result == [approved] as Set
    }

    private static final DEFAULT_SERVICE_ITEM_PROPS = [title         : "test service item",
                                                       description   : "a test service item",
                                                       launchUrl     : "https://localhost/asf",
                                                       versionName   : '1',
                                                       isEnabled     : true,
                                                       isOutside     : true,
                                                       approvalStatus: IN_PROGRESS,
                                                       techPocs      : ['poc']]

    private static ServiceItem clone(ServiceItem serviceItem, Map changes = [:]) {
        ServiceItem clone = [:] as ServiceItem
        clone.properties = serviceItem.properties + changes
        clone.id = serviceItem.id
        clone
    }

    private static Relationship clone(Relationship relationship) {
        Relationship clone = [:] as Relationship
        clone.properties = relationship.properties
        clone.id = relationship.id
        clone
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

    private static RejectionListing makeRejectionListing(Map extra = [:]) {
        def props = [justification: new RejectionJustification(title: 'Test Justification',
                                                               description: 'A test justification'),
                     description  : 'bad listing']

        return new RejectionListing(props + extra)
    }


}
