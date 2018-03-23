package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.AuditStamped
import marketplace.Category
import marketplace.CustomFieldDefinition
import marketplace.Profile
import marketplace.SecurityMixin
import marketplace.State
import marketplace.domain.builders.DomainBuilderMixin


@Integration
@Rollback
class AuditStampSpec extends Specification implements DomainBuilderMixin, SecurityMixin {

    Profile creator
    Profile editor

    private void setupData() {
        creator = $userProfile { username = "Creator" }
        editor = $userProfile { username = "Editor" }
    }

    def "state created"() {
        given:
        setupData()
        loggedInAs(creator)

        when:
        def state = $state { title = 'Test State AAA' }

        then:
        hasAuditStamp(State.get(state.id), creator)
    }

    def "state edited"() {
        given:
        setupData()
        loggedInAs(creator)

        def state = $state { title = 'Test State AAA' }
        def stamp = copyStamp(state)

        sleep(200)

        when:
        loggedInAs(editor)

        state.title = 'Test State BBB'
        save(state)

        then:
        hasBeenEdited(State.get(state.id), stamp, editor)
    }

    def "category created"() {
        given:
        setupData()
        loggedInAs(creator)

        when:
        def category = $category { title = 'Test Category AAA' }

        then:
        hasAuditStamp(Category.get(category.id), creator)
    }

    def "category edited"() {
        given:
        setupData()
        loggedInAs(creator)

        def category = $category { title = 'Test Category AAA' }
        def stamp = copyStamp(category)

        sleep(200)

        when:
        loggedInAs(editor)

        category.title = 'Test Category BBB'
        save(category)

        then:
        hasBeenEdited(Category.get(category.id), stamp, editor)
    }

    def "profile created"() {
        given:
        setupData()
        loggedInAs(creator)

        when:
        def profile = $userProfile { username = 'Test User AAA' }

        then:
        hasAuditStamp(Profile.get(profile.id), creator)
    }

    def "profile edited"() {
        given:
        setupData()
        loggedInAs(creator)

        def profile = $userProfile { username = 'Test User AAA' }
        def stamp = copyStamp(profile)

        sleep(200)

        when:
        loggedInAs(editor)

        profile.username = 'Test User BBB'
        save(profile)

        then:
        hasBeenEdited(Profile.get(profile.id), stamp, editor)
    }

    def "CustomFieldDefinition created"() {
        given:
        setupData()
        loggedInAs(creator)

        when:
        def fieldDef = $fieldDefinition { name = 'Test Field Definition AAA' }

        then:
        hasAuditStamp(CustomFieldDefinition.get(fieldDef.id), creator)
    }

    def "CustomFieldDefinition edited"() {
        given:
        setupData()
        loggedInAs(creator)

        def fieldDef = $fieldDefinition { name = 'Test Field Definition AAA' }
        def stamp = copyStamp(fieldDef)

        sleep(200)

        when:
        loggedInAs(editor)

        fieldDef.name = 'Test Field Definition BBB'
        save(fieldDef)

        then:
        hasBeenEdited(CustomFieldDefinition.get(fieldDef.id), stamp, editor)
    }

    private static void hasAuditStamp(AuditStamped entity, Profile createdBy) {
        assert entity != null

        assert entity.createdBy == createdBy.id
        assert entity.createdDate != null

        assert entity.editedBy == createdBy.id
        assert entity.editedDate != null
    }

    private static void hasBeenEdited(AuditStamped entity, AuditStamp stamp, Profile editedBy) {
        assert entity != null

        assert entity.createdBy == stamp.createdBy
        assert entity.createdDate == stamp.createdDate

        assert entity.editedBy == editedBy.id
        assert entity.editedDate.after(stamp.editedDate)
    }

    private static AuditStamp copyStamp(AuditStamped stamped) {
        assert stamped != null

        new AuditStamp([createdBy  : stamped.createdBy,
                        createdDate: new Date(stamped.createdDate.time),
                        editedBy   : stamped.editedBy,
                        editedDate : new Date(stamped.editedDate.time)])
    }

    static class AuditStamp {

        Long createdBy
        Date createdDate

        Long editedBy
        Date editedDate
    }

}
