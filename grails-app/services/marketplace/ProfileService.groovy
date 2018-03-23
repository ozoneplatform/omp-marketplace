package marketplace

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

import marketplace.data.ServiceItemDataService

import ozone.marketplace.domain.ValidationException
import ozone.utils.User


@Transactional
class ProfileService extends OzoneService {

    AccountService accountService

    ServiceItemDataService serviceItemDataService

    @ReadOnly
    Profile findByUsername(String username) {
        Profile.findByUsername(username, [cache: true])
    }

    @ReadOnly
    Profile getCurrentUserProfile(){
        findByUsername(accountService.loggedInUsername)
    }

    Profile saveProfile(def profile) {
        def session = getSession()
        if (!session.isAdmin && profile.username != session.username) {
            throw new ValidationException(message: "profile.edit.accessDenied")
        }
        profile.scrubCR()
        profile.save(flush: true)

        return profile
    }

    @ReadOnly
    Profile get(Map params) {
        Profile.get(params.id as Long)
    }

    @ReadOnly
    List<Profile> list(Map params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = Profile.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = Profile.list(params)
        }
        return results
    }

    @ReadOnly
    int total() {
        Profile.count()
    }

    @ReadOnly
    Profile getAllowableUser(Long id, Map sessionParams) throws AccessControlException {
        def user = Profile.get(id)

        if (!user) return null

        def isSameUser = (sessionParams?.username == user.username)
        if (!(sessionParams?.isAdmin || isSameUser)) {
            throw new AccessControlException('User is not authorized to access this user');
        }

        user
    }

    @ReadOnly
    Map search(Map params) {
        def model = [:]
        def c = Profile.createCriteria()
        if (params.containsKey('query') && !params.query) params.remove('query')
        if (params.containsKey('sort') && !params.sort) params.remove('sort')
        def results = c.list(params) {
            if (params.query) {
                or {
                    ilike("displayName", "%${params.query}%")
                    ilike("username", "%${params.query}%")
                }
            }
            order("displayName", params.order ?: "asc")
            order("username", params.order ?: "asc")
        }

        model.put("data", results)
        model.put("total", results.totalCount)
        model.put("params", params)

        return model
    }

    Profile createProfile(User user, Date creationDate) {
        return accountService.runAsSystemUser {
            log.debug "Adding Profile for ${user.username}"
            Profile profile = new Profile()
            profile.username = user.username
            profile.displayName = user.name ?: user.username
            profile.email = user.email
            profile.createdDate = creationDate
            profile.userRoles = accountService.getloggedInUserAuthorities()
            profile.save()
            profile
        }
    }

    Profile updateProfile(Profile profile, User user, Date creationDate) {
        log.debug "Updating display name and email for ${user.username}"

        if (!profile) return null

        return accountService.runAsSystemUser {
            def oldName = profile.displayName
            profile.displayName = user.name ?: user.username
            profile.email = user.email
            profile.userRoles = accountService.getloggedInUserAuthorities()
            profile.save()
            if (oldName != profile.displayName) {
                serviceItemDataService.reindexAllByOwnerId(profile.id)
            }
            profile
        }
    }

    @ReadOnly
    List<Profile> getProfilesWithAdminRole(){
        return Profile.findByUserRolesLike("%ADMIN%").findAll{ person ->
            person.userRoles =~ Constants.ADMIN ||  person.userRoles =~ Constants.EXTERNADMIN
        }
    }

    @Transactional
    void createRequired() {
        def profilesInConfig = config.marketplace.metadata.profiles

        if (profilesInConfig) {
            profilesInConfig.each { Map profileInfo ->
                String username = profileInfo.username
                if (!Profile.findByUsername(username)) {
                    log.debug("#### Creating profile: $username")
                    new Profile(username: username, displayName: profileInfo.displayName).save()
                } else {
                    log.info("#### Found user: $username")
                }
            }
        } else {
            log.error "Profiles metadata info was not found in the loaded config files."
        }
	}

}
