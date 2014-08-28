package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

@TestMixin(IntegrationTestMixin)
class ProfileServiceTests {

	def profileService


    void testListByDate() {
        Profile profile = new Profile(username: 'AAA').save(failOnError:true)
        assert null != Profile.get(profile.id)
        assert null != profile.createdDate
        def firstDate = profile.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}

        profile = new Profile(username: 'BBB').save(failOnError:true)
        assert null != Profile.get(profile.id)
        assert null != profile.createdDate
        def secondDate = profile.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = profileService.list(params)
        assert null != r
        assert 2 == r.size()

        params = ['editedSinceDate':secondDate]
        r = profileService.list(params)
        assert null != r
        assert 1 == r.size()
    }

    void testRequiredProfilesAreCreated() {
        profileService.createRequired()

        def systemProfiles = Profile.all.findAll { it.username == 'System' }

        assert Profile.all.size() > 0
        assert systemProfiles.size() == 1
        assert systemProfiles[0].displayName == 'System'
    }
}
