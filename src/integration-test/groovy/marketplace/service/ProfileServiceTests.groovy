package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Profile
import marketplace.ProfileService
import marketplace.domain.builders.DomainBuilderMixin


@Integration
@Rollback
class ProfileServiceTests extends Specification implements DomainBuilderMixin {

    ProfileService profileService

    void testListByDate() {
        given:
        Profile profile1 = $userProfile { username = 'AAA' }

        sleep(2000)

        Profile profile2 = $userProfile { username = 'BBB' }

        when:
        def results1 = profileService.list([editedSinceDate: profile1.editedDate])
        def results2 = profileService.list([editedSinceDate: profile2.editedDate])

        then:
        results1?.size() == 2
        results2?.size() == 1
    }

}
