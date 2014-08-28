package marketplace.rest

import org.springframework.context.support.StaticApplicationContext

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import grails.converters.JSON
import marketplace.Contact
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import org.springframework.security.access.AccessDeniedException

import marketplace.ServiceItem
import marketplace.State
import marketplace.Types
import marketplace.ServiceItemDocumentationUrl
import marketplace.Screenshot
import marketplace.OwfProperties
import marketplace.Profile
import marketplace.ServiceItemActivity
import marketplace.RejectionActivity
import marketplace.RejectionJustification
import marketplace.RejectionListing
import marketplace.Intent
import marketplace.Relationship
import marketplace.IntentDataType
import marketplace.IntentAction
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.Constants
import marketplace.ServiceItemTag
import marketplace.Tag
import marketplace.ChangeDetail
import marketplace.validator.ServiceItemValidator
import marketplace.AccountService
import marketplace.ScoreCardService
import marketplace.configuration.MarketplaceApplicationConfigurationService

import ozone.utils.User
import ozone.marketplace.enums.RelationshipType

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class ServiceItemRestServiceUnitTest {

    GrailsApplication grailsApplication

    ServiceItemRestService service

    Profile currentUser, owner, nonOwner, externalAdmin, admin
    Types type1
    State state1

    private static final exampleServiceItemProps = [
        id: 1,
        title: "test service item",
        types: [ id: 1 ],
        state: [ id: 1 ],
        description: "a test service item",
        launchUrl: "https://localhost/asf",
        owners: [ id: 1 ],
        versionName: '1',
        isEnabled: true,
        isOutside: true,
        approvalStatus: Constants.APPROVAL_STATUSES['IN_PROGRESS'],
        owfProperties: [
            height: 200,
            width: 300
        ]
    ]

    private createGrailsApplication() {
        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.refresh()

        //necessary to get reflection-based marshalling to work
        grailsApplication.addArtefact(CustomField.class)
        grailsApplication.addArtefact(ServiceItemDocumentationUrl.class)
        grailsApplication.addArtefact(Screenshot.class)
        grailsApplication.addArtefact(OwfProperties.class)
        grailsApplication.addArtefact(Relationship.class)
        grailsApplication.addArtefact(Intent.class)
        grailsApplication.addArtefact(ServiceItem.class)
        grailsApplication.addArtefact(Contact.class)
        grailsApplication.addArtefact(Profile.class)
        grailsApplication.addArtefact(Tag.class)
        grailsApplication.addArtefact(ServiceItemTag.class)

        FakeAuditTrailHelper.install()
    }

    private makeServiceItem() {
        OwfProperties owfProps = new OwfProperties(exampleServiceItemProps.owfProperties)

        def exampleServiceItem = new ServiceItem(exampleServiceItemProps + [
            owners: [owner],
            types: type1,
            state: state1,
            owfProperties: owfProps
        ])
        exampleServiceItem.id = exampleServiceItemProps.id
        owfProps.serviceItem = exampleServiceItem

        return exampleServiceItem
    }

    void setUp() {
        def owner = new Profile(username: 'owner')
        owner.id = 1
        def nonOwner = new Profile(username: 'nonOwner')
        nonOwner.id = 2
        def admin = new Profile(username: 'admin')
        admin.id = 3
        def externalAdmin = new Profile(username: 'external')
        externalAdmin.id = 4

        def type = new Types(title: 'Test Type', ozoneAware: true)
        type.id = 1

        type1 = type

        def state1 = new State(title: 'Active')
        def state2 = new State(title: 'Inactive')
        this.state1 = state1

        def intent = new Intent(
            action: new IntentAction(
                title: 'run',
                description: 'run'
            ),
            dataType: new IntentDataType(
                title: 'text/plain',
                description: 'plain text'
            )
        )
        intent.id = 1

        def customFieldDefinition = new CustomFieldDefinition(
            name: "test custom field definition",
            label: 'Custom Field Def Label',
            types: [type]
        )
        customFieldDefinition.id = 1

        createGrailsApplication()

        mockDomain(OwfProperties.class)
        mockDomain(CustomField.class)
        mockDomain(ChangeDetail.class)
        mockDomain(ServiceItemActivity.class)
        mockDomain(Relationship.class)
        mockDomain(RejectionListing.class)

        mockDomain(Types.class, [type])
        mockDomain(State.class, [state1, state2])
        mockDomain(Intent.class, [intent])
        mockDomain(CustomFieldDefinition.class, [customFieldDefinition])
        mockDomain(Profile.class, [owner, nonOwner, admin, externalAdmin])

        currentUser = admin
        this.admin = admin
        this.owner = owner
        this.nonOwner = nonOwner
        this.externalAdmin = externalAdmin

        mockDomain(ServiceItem.class)
        makeServiceItem().save(failOnError: true)

        def serviceItemValidator = [
            validateNew: {},
            validateChanges: { a, b -> }
        ] as ServiceItemValidator

        service = new ServiceItemRestService(grailsApplication, serviceItemValidator)

        service.profileRestService = [
            getCurrentUserProfile: { currentUser },
            getById: { id -> Profile.get(id) }
        ] as ProfileRestService

        service.accountService = [
            isAdmin: { currentUser.username.toLowerCase().contains('admin') },
            checkAdmin: {
                if (!currentUser.username.toLowerCase().contains('admin')) {
                    throw new AccessDeniedException('access denied')
                }
            },
            isExternAdmin: { currentUser.username.toLowerCase().contains('external') },
            getLoggedInUser: { new User(username: currentUser.username) },
            getLoggedInUsername: { currentUser.username }
        ] as AccountService

        service.marketplaceApplicationConfigurationService = [
            is: { true },
            valueOf: {  _ -> null }
        ] as MarketplaceApplicationConfigurationService

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: {si, action -> }
        ] as ServiceItemActivityInternalService

        //dirty checking isn't mocked in unit tests, so we need to mock
        //the method that relies on it
        ServiceItem.metaClass.modifiedForChangeLog = { false }
    }

    void testAuthorizeUpdate() {
        def ownerCanAlwaysEdit
        ServiceItem si

        service.marketplaceApplicationConfigurationService = [
            is: { ownerCanAlwaysEdit },
            valueOf: {  _ -> null }
        ] as MarketplaceApplicationConfigurationService

        //this should work because the listing is in progress
        currentUser = Profile.findByUsername('owner')
        ownerCanAlwaysEdit = false
        service.updateById(exampleServiceItemProps.id, makeServiceItem())

        //this should fail because the owner is not an admin and the listing is already
        //approved
        shouldFail/*(AccessDeniedException)*/ {
            si = ServiceItem.get(1)
            si.approvalStatus = Constants.APPROVAL_STATUSES['APPROVED']
            si.save(failOnError: true)
            service.updateById(exampleServiceItemProps.id, makeServiceItem())
        }

        //this should succeed because the configuration to allow the owner to edit after approval
        //is set to true
        ownerCanAlwaysEdit = true
        ServiceItem updates = makeServiceItem()
        service.populateDefaults(updates)
        updates.approvalStatus = Constants.APPROVAL_STATUSES['APPROVED']
        service.updateById(exampleServiceItemProps.id, updates)

        //this should fail because an non-admin, non-owner user is trying to update
        shouldFail(AccessDeniedException) {
            si = ServiceItem.get(1)
            si.approvalStatus = Constants.APPROVAL_STATUSES['IN_PROGRESS']
            si.save(failOnError: true)
            currentUser = nonOwner
            service.updateById(exampleServiceItemProps.id, makeServiceItem())
        }

        //this should succeed because admins can always edit
        currentUser = admin
        service.updateById(exampleServiceItemProps.id, makeServiceItem())

        //this should fail because external admins can only edit listings that they own or created
        shouldFail(AccessDeniedException) {
            currentUser = externalAdmin
            service.updateById(exampleServiceItemProps.id, makeServiceItem())
        }

        //this should succeed because the external admin is now the creator of the listing
        si = ServiceItem.get(1)
        si.createdBy = currentUser
        si.save(failOnError: true)
        service.updateById(exampleServiceItemProps.id, makeServiceItem())
    }

    void testAuthorizeCreate() {

        //dirty checking isn't mocked in unit tests, so we need to mock
        //the method that relies on it
        ServiceItem.metaClass.modifiedForChangeLog = { false }

        //ensure that normal users can create
        currentUser = Profile.findByUsername('nonOwner')
        service.createFromDto(makeServiceItem())

        //ensure that admins can create
        currentUser = Profile.findByUsername('admin')
        ServiceItem dto = service.createFromDto(makeServiceItem())

        assertNotNull dto
    }

    void testPreprocess() {
        String insideOutsideConfig = Constants.INSIDE_OUTSIDE_ALL_OUTSIDE
        def expectedInsideOutsideUpdate

        def serviceItemMock = mockFor(ServiceItem)

        //demand that thes two methods get called for each ServiceItem that we create
        serviceItemMock.demand.processCustomFields(3..3) {}
        serviceItemMock.demand.checkOwfProperties(3..3) {}

        service.marketplaceApplicationConfigurationService = [
            valueOf: { _ -> insideOutsideConfig }
        ] as MarketplaceApplicationConfigurationService

        ServiceItem.metaClass.updateInsideOutsideFlag = { String flag ->
            expectedInsideOutsideUpdate = flag
        }

        service.createFromDto(makeServiceItem())
        assert expectedInsideOutsideUpdate == insideOutsideConfig

        insideOutsideConfig = Constants.INSIDE_OUTSIDE_ALL_INSIDE
        service.createFromDto(makeServiceItem())
        assert expectedInsideOutsideUpdate == insideOutsideConfig

        insideOutsideConfig = null
        service.createFromDto(makeServiceItem())
        assert expectedInsideOutsideUpdate == insideOutsideConfig


    }

    void testApprove() {
        ServiceItemActivity activity
        ServiceItem dto
        def id

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action ->
                //creation of the changelog uses the other signature
                if(action instanceof ServiceItemActivity)
                    return
                activity = new ServiceItemActivity(action: action, serviceItem: si)
            }
        ] as ServiceItemActivityInternalService

        def approve = {
            dto = makeServiceItem()
            dto.id = id
            dto.owfProperties.stackDescriptor = '{}'
            dto.approvalStatus = Constants.APPROVAL_STATUSES['APPROVED']
            dto = service.updateById(dto.id, dto)
        }

        dto = makeServiceItem()
        dto.owfProperties.stackDescriptor = '{}'

        id = service.createFromDto(dto).id
        dto = makeServiceItem()
        dto.id = id
        dto.owfProperties.stackDescriptor = '{}'
        dto.approvalStatus = Constants.APPROVAL_STATUSES['PENDING']
        dto = service.updateById(dto.id, dto)

        //users cannot approve
        shouldFail(AccessDeniedException) {
            currentUser = this.owner
            approve()
        }

        currentUser = admin

        //need to reset the approval status because the unit tests aren't transactional and
        //the preceding failed change did not get rolled back
        ServiceItem.get(dto.id).approvalStatus = Constants.APPROVAL_STATUSES['PENDING']
        approve()

        assert JSON.parse(dto.owfProperties.stackDescriptor).approved
        assert activity.action == Constants.Action.APPROVED
        assert activity.serviceItem == dto

        //make sure it was approved within the last second
        assert dto.approvedDate.time > (new Date()).time - 1000
    }

    void testReject() {
        ServiceItem created
        ServiceItemActivity activity

        service.serviceItemActivityInternalService = [
            addRejectionActivity: { si, rejectionListing ->
                activity = new RejectionActivity(
                    serviceItem: si,
                    rejectionListing: rejectionListing
                )
            },
            addServiceItemActivity: { si, action -> }
        ] as ServiceItemActivityInternalService

        def reject = {
            service.reject(created, new RejectionListing(
                justification: new RejectionJustification(
                    title: 'Test Justification',
                    description: 'A test justification'
                ),
                description: 'bad listing'
            ))
        }

        def id = service.createFromDto(makeServiceItem()).id

        //make a fresh dto
        created = makeServiceItem()
        service.populateDefaults(created)
        created.id = id
        created.approvalStatus = Constants.APPROVAL_STATUSES['PENDING']
        created = service.updateById(created.id, created)

        //in grails 2, shouldFail with a specific exception
        //type doesn't seem to work
        shouldFail(AccessDeniedException) {
            currentUser = this.owner
            reject()
        }

        currentUser = admin
        reject()

        assert created.approvalStatus == Constants.APPROVAL_STATUSES['REJECTED']
        assert activity.rejectionListing.description == 'bad listing'
        assert activity instanceof RejectionActivity
        assert created.rejectionListings == [activity.rejectionListing] as SortedSet
    }

    void testPopulateDefaults() {
        def org = 'My Organization'
        def poc = 'The POC'

        service.accountService = [
            isAdmin: { currentUser.username.toLowerCase().contains('admin') },
            checkAdmin: {
                if (!currentUser.username.toLowerCase().contains('admin')) {
                    throw new AccessDeniedException('access denied')
                }
            },
            isExternAdmin: { currentUser.username.toLowerCase().contains('external') },
            getLoggedInUser: {
                new User(username: currentUser.username, org: org)
            }
        ] as AccountService

        //create a dto with no defaults filled in
        ServiceItem dto = makeServiceItem()
        dto.with {
            owners = null
            techPocs = null
            organization = null
            state = null
        }

        ServiceItem created = service.createFromDto(dto)

        assert created.owners == [currentUser]
        assert created.techPocs == [currentUser.username] as Set
        assert created.organization == org
        assert created.state == State.findByTitle('Active')

        //create a dto with defaults filled in and ensure that ey are preserved
        dto = makeServiceItem()
        dto.with {
            owners = [Profile.findByUsername('nonOwner')]
            techPocs = [poc]
            organization = org + '1'
            state = State.findByTitle('Inactive')
        }

        created = service.createFromDto(dto)

        assert created.owners == [Profile.findByUsername('nonOwner')]
        assert created.techPocs == [poc] as Set
        assert created.organization == org + '1'
        assert created.state == State.findByTitle('Inactive')
    }

    void testUpdateInsideOutsideServiceItemActivity() {
        def initialListing = makeServiceItem()
        initialListing.isOutside = null
        initialListing.id = 2
        service.createFromDto(initialListing) //this gets around some goofiness with ids on the
                                              //mocked domain
        def id = service.createFromDto(initialListing).id
        def activity

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action ->
                activity = new ServiceItemActivity(action: action, serviceItem: si)
            }
        ] as ServiceItemActivityInternalService

        ServiceItem dto = makeServiceItem()
        dto.id = id
        dto.isOutside = true

        dto = service.updateById(id, dto)
        assertNotNull activity //isOutside should create an activity when changing from null to true/false

        dto = makeServiceItem()
        dto.id = id
        dto.isOutside = false

        dto = service.updateById(id, dto)
        assertNotNull activity
        assert activity.action == Constants.Action.INSIDE
        assert activity.serviceItem == dto

        dto = makeServiceItem()
        dto.id = id
        dto.isOutside = true

        dto = service.updateById(id, dto)
        assertNotNull activity
        assert activity.action == Constants.Action.OUTSIDE
        assert activity.serviceItem == dto
    }

    void testUpdateHiddenServiceItemActivity() {
        //isEnabled = true is the default
        def id = service.createFromDto(makeServiceItem()).id
        def activity

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action ->
                activity = new ServiceItemActivity(action: action, serviceItem: si)
            }
        ] as ServiceItemActivityInternalService

        ServiceItem dto = makeServiceItem()
        service.populateDefaults(dto)
        dto.id = id
        dto.isEnabled = false

        dto = service.updateById(id, dto)
        assertNotNull activity
        assert activity.action == Constants.Action.DISABLED
        assert activity.serviceItem == dto


        dto = makeServiceItem()
        service.populateDefaults(dto)
        dto.id = id
        dto.isEnabled = true

        dto = service.updateById(id, dto)
        assertNotNull activity
        assert activity.action == Constants.Action.ENABLED
        assert activity.serviceItem == dto
    }

    void testUpdateRelationshipServiceItemActivity() {
        def makeRelationship = { related=null ->
            new Relationship(
                relationshipType: RelationshipType.REQUIRE,
                relatedItems: related ?: []
            )
        }

        currentUser = Profile.findByUsername('admin')

        def id = service.createFromDto(makeServiceItem()).id
        def parent, added, removed
        def relationship = makeRelationship()
        def relatedItem1 = service.createFromDto(makeServiceItem())
        def relatedItem2 = service.createFromDto(makeServiceItem())

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action -> },
            addRelationshipActivities: { p, a, r ->
                parent = p
                added = a
                removed = r
            }
        ] as ServiceItemActivityInternalService

        ServiceItem dto = makeServiceItem()
        dto.id = id
        dto.relationships = [makeRelationship([relatedItem1, relatedItem2])]
        service.updateById(id, dto)

        assert parent.id == dto.id
        assert added.collect { it.id } as Set == [relatedItem1.id, relatedItem2.id] as Set
        assert removed as Set == [] as Set

        dto.relationships = [makeRelationship([relatedItem1])]
        service.updateById(id, dto)

        assert added as Set == [] as Set
        assert removed.collect {it.id} as Set == [relatedItem2.id] as Set

        dto.relationships = [makeRelationship([relatedItem2])]
        service.updateById(id, dto)

        assert added.collect {it.id} as Set == [relatedItem2.id] as Set
        assert removed.collect {it.id} as Set == [relatedItem1.id] as Set

        dto.relationships = [makeRelationship([relatedItem2])]
        service.updateById(id, dto)

        assert added as Set == [] as Set
        assert removed as Set == [] as Set
    }

    void testGetAllRequiredServiceItemsByParentId() {
        def makeRelationship = { related=null ->
            new Relationship(
                relationshipType: RelationshipType.REQUIRE,
                relatedItems: related ?: []
            )
        }

        def id = service.createFromDto(makeServiceItem()).id

        def getRequired = { blockInsideListings=false ->
            service.getAllRequiredServiceItemsByParentId(id, blockInsideListings)
                .collect {it.id} as Set
        }

        def relationship = makeRelationship()
        def relatedItem1 = service.createFromDto(makeServiceItem())
        def relatedItem2 = makeServiceItem()
        relatedItem2.isOutside = false
        relatedItem2 = service.createFromDto(relatedItem2)

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action -> },
            addRelationshipActivities: { p, a, r -> }
        ] as ServiceItemActivityInternalService

        ServiceItem dto = makeServiceItem()
        dto.id = id
        dto.relationships = [makeRelationship([relatedItem1, relatedItem2])]
        service.updateById(id, dto)

        assert getRequired() == [relatedItem1.id, relatedItem2.id] as Set
        assert getRequired(true) == [relatedItem1.id] as Set

        dto.relationships = [makeRelationship([relatedItem1])]
        service.updateById(id, dto)

        assert getRequired() == [relatedItem1.id] as Set
        assert getRequired(true) == [relatedItem1.id] as Set

        dto.relationships = [makeRelationship([relatedItem2])]
        service.updateById(id, dto)

        assert getRequired() == [relatedItem2.id] as Set
        assert getRequired(true) == [] as Set

        dto.relationships = [makeRelationship()]
        service.updateById(id, dto)

        assert getRequired() == [] as Set
        assert getRequired(true) == [] as Set
    }

    void testGetAllRequiredServiceItemsByParentIdCyclicDependency() {
        def makeRelationship = { ids=null ->
            new Relationship(
                relationshipType: RelationshipType.REQUIRE,
                relatedItems: ids.collect {
                    def si = new ServiceItem()
                    si.id = it
                    si
                } ?: []
            )
        }

        def id

        def getRequired = { blockInsideListings ->
            service.getAllRequiredServiceItemsByParentId(id, blockInsideListings)
                .collect {it.id} as Set
        }

        service.serviceItemActivityInternalService = [
            addServiceItemActivity: { si, action -> },
            addRelationshipActivities: { p, a, r -> }
        ] as ServiceItemActivityInternalService

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

        assert getRequired() == [relatedItem1.id, relatedItem2.id] as Set
    }

    void testGetAllByAuthorId() {
        ServiceItem.metaClass.static.findAllByAuthor = { Profile a ->
            ServiceItem.list().grep { it.owners.contains(a) }
        }

        ServiceItem existing = ServiceItem.get(1)
        //create a serviceitem that is approved. The default one is not
        ServiceItem approved = makeServiceItem()
        approved.approvalStatus = 'Approved'
        approved.id = 2
        approved.save(failOnError: true)

        def author = Profile.findByUsername('owner')
        def authorId = author.id

        currentUser = Profile.findByUsername('nonOwner')

        def serviceItems = service.getAllByAuthorId(authorId)

        assert serviceItems.size() == 1
        assert serviceItems == [approved] as Set
    }
}
