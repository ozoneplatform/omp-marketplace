package marketplace.rest

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import grails.validation.ValidationException
import org.springframework.security.access.AccessDeniedException
import marketplace.rest.DomainObjectNotFoundException
import marketplace.ServiceItem
import marketplace.Types
import marketplace.ServiceItemDocumentationUrl
import marketplace.Contact
import marketplace.Screenshot
import marketplace.CustomField
import marketplace.OwfProperties
import marketplace.Category
import marketplace.Relationship
import marketplace.ServiceItemTag
import marketplace.Tag
import marketplace.Profile
import marketplace.Intent
import marketplace.Constants
import marketplace.validator.DomainValidator

import marketplace.testutil.FakeAuditTrailHelper

import ozone.marketplace.enums.RelationshipType

@TestMixin(DomainClassUnitTestMixin)
class RestServiceUnitTest {
    GrailsApplication grailsApplication

    RestService<ServiceItem> restService
    DomainValidator<ServiceItem> validator


    private static final exampleServiceItemProps = [
        id: 1,
        title: "test service item",
        types: [ id: 1 ],
        relationships: [[
           relationshipType: RelationshipType.REQUIRE,
            relatedItems: []
        ]],
        description: "a test service item",
        launchUrl: "https://localhost/asf",
        owners: [ id: 1 ],
        versionName: '1',
        approvalStatus: Constants.APPROVAL_STATUSES['IN_PROGRESS'],
        owfProperties: [
            height: 100,
            width: 100,
            universalName: 'test.service.item'
        ]
    ]

    //a simple sublass of RestService that specializes in ServiceItems
    private static class TestService extends RestService<ServiceItem> {
        TestService(GrailsApplication grailsApplication) {
            super(grailsApplication, ServiceItem.class, null, null)
        }

        @Override
        void authorizeUpdate(ServiceItem si) {}
    }

    void setUp() {
        //mock createCriteria.  It needs to return an object with a functioning list() method
        ServiceItem.metaClass.static.createCriteria = {
            [ list: { params, closure -> ServiceItem.list(params) } ]
        }

        def owner = new Profile(username: 'testUser')
        owner.id = 1

        def newOwner = new Profile(username: 'testUser2')
        newOwner.id = 2

        def type = new Types(title: 'Test Type')
        type.id = 1

        def exampleServiceItem = new ServiceItem(exampleServiceItemProps + [
            owners: [owner],
            types: type
        ])

        exampleServiceItem.id = 1

        assertNotNull exampleServiceItem

        FakeAuditTrailHelper.install()

        mockDomain(Screenshot.class)
        mockDomain(Category.class)
        mockDomain(Relationship.class)
        mockDomain(ServiceItem.class, [exampleServiceItem])
        mockDomain(Profile.class, [owner, newOwner])
        mockDomain(Types.class, [type])


        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.refresh()

        //necessary to get reflection-based marshalling to work
        grailsApplication.addArtefact(CustomField.class)
        grailsApplication.addArtefact(ServiceItemDocumentationUrl.class)
        grailsApplication.addArtefact(Screenshot.class)
        grailsApplication.addArtefact(OwfProperties.class)
        grailsApplication.addArtefact(Category.class)
        grailsApplication.addArtefact(Relationship.class)
        grailsApplication.addArtefact(Intent.class)
        grailsApplication.addArtefact(ServiceItem.class)
        grailsApplication.addArtefact(Contact.class)
        grailsApplication.addArtefact(ServiceItemTag.class)
        grailsApplication.addArtefact(Tag.class)
        restService = new TestService(grailsApplication)
    }

    void testGetAll() {
        Collection<ServiceItem> retval = restService.getAll(null, null)

        assert retval instanceof Collection
        assert retval.size() == 1

        ServiceItem si = retval.iterator().next()
        assert si instanceof ServiceItem
        assert si.title == exampleServiceItemProps.title
        assert si.id == exampleServiceItemProps.id
    }

    void testGetById() {
        def id = restService.getAll(0, 1).iterator().next().id

        ServiceItem retval = restService.getById(id)
        assert retval instanceof ServiceItem
        assert retval.title == exampleServiceItemProps.title
        assert retval.id == id
    }

    void testGetByIdInvalidId() {
        def goodId = restService.getAll(0, 1).iterator().next().id
        def badId = ~goodId //bitwise NOT

        shouldFail(DomainObjectNotFoundException) {
            restService.getById(badId)
        }
    }

    void testUpdateById() {
        final newTitle = 'Updated Name', newUniversalName = 'test.service.item.2'

        def ownerDto = new Profile()
        ownerDto.id = 2

        def typeDto = new Types()
        typeDto.id = 1

        def relationship = new Relationship(
            relationshipType: RelationshipType.REQUIRE,
            relatedItems: [ id: 1 ]
        )

        def id = restService.getAll(0, 1).iterator().next().id
        ServiceItem updates = new ServiceItem(exampleServiceItemProps + [
            title: newTitle,
            owners: [ownerDto],
            types: typeDto,
            //use Relationship test updating a modifiableReferenceProperty collection
            relationships: [relationship],
            owfProperties: new OwfProperties(exampleServiceItemProps.owfProperties + [
                universalName: newUniversalName
            ])
        ])
        updates.id = id

        ServiceItem retval = restService.updateById(id, updates)
        assert retval instanceof ServiceItem
        assert retval.title == newTitle
        assert retval.owfProperties.universalName == newUniversalName

        //ensure that properties we didn't change do not change
        assert retval.description == exampleServiceItemProps.description
        assert retval.owners == [Profile.get(2)]
        assert retval.owfProperties.height == exampleServiceItemProps.owfProperties.height
        assert retval.types == Types.get(1)

        assert retval.id == id

        //ensure that the changes were actually saved
        ServiceItem fromGet = restService.getById(id)
        assert fromGet.title == newTitle
        assert fromGet.owfProperties.universalName == newUniversalName
        assert fromGet.owners == [Profile.get(2)]
    }


    void testUpdateByIdInvalidId() {
        final newTitle = 'Updated Name', newUniversalName = 'test.service.item.2'

        def ownerDto = new Profile()
        ownerDto.id = 1

        def typeDto = new Types()
        typeDto.id = 1

        def goodId = restService.getAll(0, 1).iterator().next().id
        def badId = ~goodId //bitwise NOT
        ServiceItem updates = new ServiceItem(exampleServiceItemProps + [
            title: newTitle,
            owners: [ownerDto],
            types: typeDto,
            owfProperties: new OwfProperties(exampleServiceItemProps.owfProperties + [
                universalName: newUniversalName
            ])
        ])
        updates.id = badId

        shouldFail(DomainObjectNotFoundException) {
            restService.updateById(badId, updates)
        }
    }

    void testUpdateByIdInvalidDto() {
        def id = restService.getAll(0, 1).iterator().next().id
        ServiceItem updates = new ServiceItem()
        updates.id = id

        shouldFail(ValidationException) {
            def retval = restService.updateById(id, updates)

            //shouldn't get here
            throw new RuntimeException("Retval: ${retval.dump()}")
        }
    }

    void testUpdateByIdInvalidDtoId() {
        final newTitle = 'Updated Name', newUniversalName = 'test.service.item.2'

        def ownerDto = new Profile()
        ownerDto.id = 1

        def typeDto = new Types()
        typeDto.id = 1

        def goodId = restService.getAll(0, 1).iterator().next().id
        def badId = ~goodId //bitwise NOT
        ServiceItem updates = new ServiceItem(exampleServiceItemProps + [
            id: badId,
            title: newTitle,
            owners: [ownerDto],
            types: typeDto,
            owfProperties: new OwfProperties(exampleServiceItemProps.owfProperties + [
                universalName: newUniversalName
            ])
        ])

        shouldFail(IllegalArgumentException) {
            //using goodId for the first arg, badId in the second
            restService.updateById(goodId, updates)
        }
    }

    void testDeleteById() {
        def id = restService.getAll(0, 1).iterator().next().id

        restService.deleteById(id)

        shouldFail(DomainObjectNotFoundException) {
            def retval = restService.getById(id)
        }
    }

    void testDeleteByIdInvalidId() {
        def goodId = restService.getAll(0, 1).iterator().next().id
        def badId = ~goodId //bitwise NOT

        shouldFail(DomainObjectNotFoundException) {
            restService.deleteById(badId)
        }
    }

    void testCreateFromDto() {
        OwfProperties newOwfProperties = new OwfProperties(exampleServiceItemProps.owfProperties)
        Relationship newRelationship = new Relationship(exampleServiceItemProps.relationships[0])

        //the  serviceitem dto will have an owner object that only specifies the id of the actual
        //profile to look up
        def ownerId = 1
        Profile ownerDto = new Profile()
        ownerDto.id = ownerId

        def typeId = 1
        Types typeDto = new Types()
        typeDto.id = typeId

        ServiceItem newServiceItem = new ServiceItem(exampleServiceItemProps + [
            owfProperties: newOwfProperties,
            owners: [ownerDto],
            types: typeDto,
            relationships: [newRelationship]
        ] - [id: 1])

        ServiceItem retval = restService.createFromDto(newServiceItem)
        assert retval instanceof ServiceItem
        assert retval.title == exampleServiceItemProps.title
        assert retval.owners == [Profile.get(ownerId)]
        assert retval.types == Types.get(typeId)
        assert retval.owfProperties.height == exampleServiceItemProps.owfProperties.height
        assert retval.owfProperties.width == exampleServiceItemProps.owfProperties.width
        assert retval.owfProperties.universalName ==
                exampleServiceItemProps.owfProperties.universalName

        assertNotNull retval.id
    }

    void testCreateFromDtoInvalidDto() {
        ServiceItem invalidDto = new ServiceItem()

        shouldFail(ValidationException) {
            restService.createFromDto(invalidDto)
        }
    }

    /**
     * Test that only properties that are allowed to be modified are actually modified
     */
    void testOnlyBindAllowedProperties() {
        final newTitle = 'Updated Name', newUniversalName = 'test.service.item.2'
        final newCreatedDate = new Date(0x7FFFFFFFFFFFFFFFL)

        def ownerDto = new Profile()
        ownerDto.id = 2

        def typeDto = new Types()
        typeDto.id = 1

        def id = restService.getAll(0, 1).iterator().next().id
        ServiceItem updates = new ServiceItem(exampleServiceItemProps + [
            title: newTitle,
            owners: [ownerDto],
            types: typeDto,
            relationships: [new Relationship(exampleServiceItemProps.relationships[0])],
            createdDate: newCreatedDate,    //trying to change createdDate to the far future
            owfProperties: new OwfProperties(exampleServiceItemProps.owfProperties + [
                universalName: newUniversalName
            ])
        ])
        updates.id = id

        ServiceItem retval = restService.updateById(id, updates)

        assert retval.createdDate != newCreatedDate
    }

    void testAuthorizeUpdate() {
        def authorizedUser = Profile.get(1), unauthorizedUser = Profile.get(2)

        def currentUser

        def restService = new RestService<Screenshot>(grailsApplication, Screenshot.class, null,
                null) {
            @Override
            void authorizeUpdate(Screenshot screenshot) {
                if (currentUser != authorizedUser) {
                    throw new AccessDeniedException('Denied')
                }
            }
        }

        def makeScreenshot = {
            def serviceItem = new ServiceItem()
            serviceItem.id = 1

            new Screenshot(
                smallImageUrl: 'https://localhost/small',
                largeImageUrl: 'https://localhost/large',
                serviceItem: serviceItem
            )
        }

        //test createFromDto.
        //default impl of authorizeCreate defers to authorizeUpdate
        shouldFail(AccessDeniedException) {
            currentUser = unauthorizedUser
            restService.createFromDto(makeScreenshot())
        }
        currentUser = authorizedUser
        def screenshotId = makeScreenshot().save(failOnError: true).id

        //test updateById
        def screenshot = makeScreenshot()
        shouldFail(AccessDeniedException) {
            currentUser = unauthorizedUser
            screenshot.id = screenshotId

            restService.updateById(screenshotId, screenshot)
        }
        currentUser = authorizedUser
        restService.updateById(screenshotId, screenshot)

        //test deleteById
        shouldFail(AccessDeniedException) {
            currentUser = unauthorizedUser
            restService.deleteById(screenshotId)
        }
        currentUser = authorizedUser
        restService.deleteById(screenshotId)
    }

    void testAuthorizeCreate() {
        def authorizedUser = Profile.get(1), unauthorizedUser = Profile.get(2)

        def currentUser

        def restService = new RestService<Screenshot>(grailsApplication, Screenshot.class, null,
                null) {
            @Override
            void authorizeCreate(Screenshot screenshot) {
                if (currentUser != authorizedUser) {
                    throw new AccessDeniedException('Denied')
                }
            }

            @Override
            void authorizeUpdate(Screenshot screenshot) {}
        }

        def makeScreenshot = {
            def serviceItem = new ServiceItem()
            serviceItem.id = 1

            new Screenshot(
                smallImageUrl: 'https://localhost/small',
                largeImageUrl: 'https://localhost/large',
                serviceItem: serviceItem
            )
        }

        //test createFromDto.
        //default impl of authorizeCreate defers to authorizeUpdate
        shouldFail(AccessDeniedException) {
            currentUser = unauthorizedUser
            restService.authorizeCreate(makeScreenshot())
        }
        currentUser = authorizedUser

        //should not fail
        restService.authorizeCreate(makeScreenshot())
    }

    void testValidator() {
        //make a category with only one field populated with a value
        def makeCategory = { fieldToPopulate, value ->
            def category = new Category()
            category[fieldToPopulate] = value

            return category
        };

        //a stubbed-out validator with contrived rules
        def validator = [
            //only allow new categories that have a title, and only allow modifications
            //that have a different title and description
            validateNew: { dto ->
                if (dto.title == null) {
                    throw new IllegalArgumentException('invalid')
                }
            },

            validateChanges: { toUpdate, dto ->
                if (dto.title == dto.description) {
                    throw new IllegalArgumentException('invalid')
                }
            }
        ] as DomainValidator

        def restService = new RestService<Category>(grailsApplication, Category.class,
                validator, null) {
            @Override
            void authorizeUpdate(Category category) {}
        }

        shouldFail(IllegalArgumentException) {
            restService.createFromDto(makeCategory('description', 'asdf'))
        }

        def successfulCategory = makeCategory('title', 'asdf')
        def categoryId = restService.createFromDto(successfulCategory).id

        def failureUpdate
        shouldFail(IllegalArgumentException) {
            failureUpdate = makeCategory('description', 'asdf')
            failureUpdate.title = failureUpdate.description
            failureUpdate.id = categoryId

            restService.updateById(categoryId, failureUpdate)
        }

        def successfulUpdate = makeCategory('description', 'qwerty')
        successfulUpdate.title = failureUpdate.title
        successfulUpdate.id = categoryId
        assert restService.updateById(categoryId, successfulUpdate).id == categoryId
    }
}
