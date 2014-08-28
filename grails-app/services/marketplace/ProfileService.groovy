package marketplace

import ozone.marketplace.domain.ValidationException
import org.springframework.transaction.annotation.Transactional
import ozone.utils.User
import static org.codehaus.groovy.grails.commons.ConfigurationHolder.config
import marketplace.AccountService
import static marketplace.Constants.*
class ProfileService extends OzoneService {


    AccountService accountService

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    @Transactional(readOnly = true)
    def findByUsername(def username) {
        return Profile.findByUsername(username, [cache: true])
    }

    @Transactional(readOnly=true)
    def getCurrentUserProfile(){
         findByUsername(accountService.loggedInUsername)
    }

    @Transactional
    def saveProfile(def profile) {
        def session = getSession()
        if (!session.isAdmin && profile.username != session.username) {
            throw new ValidationException(message: "profile.edit.accessDenied")
        }
        profile.scrubCR()
        profile.save(flush: true)

        return profile
    }

    @Transactional(readOnly = true)
    def get(def params) {
        return Profile.get(params.id)
    }

    @Transactional(readOnly = true)
    def list(def params) {
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

    @Transactional(readOnly = true)
    def total() {
        return Profile.count()
    }

    @Transactional(readOnly = true)
    def getAllowableUser(def id, def sessionParams) throws AccessControlException {
        def user = Profile.get(id)
        def isUser = false
        def matchesRule = false

        if (user) {
            isUser = (sessionParams?.username == user.username)

            if (sessionParams?.isAdmin) {
                matchesRule = true
            }
            if (isUser) {
                matchesRule = true
            }

            if (matchesRule) {
                return user
            } else {
                throw new AccessControlException('User is not authorized to access this user');
            }
        }
        return null;
    }

    // Reindex serviceItems in the Compass index to account for a change to the author
    void reindexServiceItemsByUser(def id) {
        log.info "reindexServiceItemsByUsers for Profile ${id} (with query)"

        def criteria = ServiceItem.createCriteria()
        def serviceItems = criteria.list {
            owners {
                eq('id', new Long(id))
            }
        }
        serviceItems.each() {
            log.info("reindexing ${it}")
            it.index() // index() is used as it appears more reliable than reindex().
        }
    }

    def search(def params) {

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

    @Transactional
    Profile createProfile(User user, Date creationDate) {
        log.debug "Adding Profile for ${user.username}"
        def profile = new Profile()
        profile.username = user.username
        profile.displayName = user.name ?: user.username
        profile.email = user.email
        profile.createdDate = creationDate
        profile.userRoles = accountService.getloggedInUserAuthorities()
        profile.save()
        profile
    }

    @Transactional
    Profile updateProfile(Profile profile, User user, Date creationDate) {
        log.debug "Updating display name and email for ${user.username}"
        if (profile) {
            def oldName = profile.displayName
            profile.displayName = user.name ?: user.username
            profile.email = user.email
            profile.userRoles = accountService.getloggedInUserAuthorities()
            profile.save()
            if (oldName != profile.displayName) {
                reindexServiceItemsByUser(profile.id)
            }
        }
        profile
    }


    @Transactional(readOnly = false)
    def createRequired() {
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

    @Transactional(readOnly=true)
    def getProfilesWithAdminRole(){
        return Profile.findByUserRolesLike("%ADMIN%").findAll{ person ->
            person.userRoles =~ Constants.ADMIN ||  person.userRoles =~ Constants.EXTERNADMIN
        }
    }
}
