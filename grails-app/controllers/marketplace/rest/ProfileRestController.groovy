package marketplace.rest

import grails.converters.JSON

import org.springframework.http.HttpStatus

import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity


class ProfileRestController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    ProfileRestService profileRestService

    ServiceItemRestService serviceItemRestService
    ItemCommentRestService itemCommentRestService
    ServiceItemTagRestService serviceItemTagRestService
    ServiceItemActivityRestService serviceItemActivityRestService

    def index(Integer offset, Integer max) {
        List<Profile> results = profileRestService.getAll(offset, max)

        render(results as JSON)
    }

    def show() {
        Profile result = getSelfOrAuthorFromParamId()

        render(result as JSON)
    }

    def save(Profile profile) {
        Profile result = profileRestService.createFromDto(profile)

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def update(Profile profile) {
        Profile result = profileRestService.updateById(profile.id, profile)

        render(result as JSON)
    }

    def delete(Long id) {
        profileRestService.deleteById(id)

        render(status: HttpStatus.NO_CONTENT.value())
    }

    def serviceItems() {
        Profile author = getSelfOrAuthorFromParamId()
        Set<ServiceItem> results = serviceItemRestService.getAllByAuthor(author)

        render(results as JSON)
    }

    def itemComments() {
        Profile author = getSelfOrAuthorFromParamId()

        def results = itemCommentRestService.getAllByAuthor(author).collect {
            new ItemCommentServiceItemDto(it)
        }

        render(results as JSON)
    }

    def tags() {
        Profile author = getSelfOrAuthorFromParamId()

        def results = serviceItemTagRestService.getAllByProfile(author).collect {
            new ProfileServiceItemTagDto(it)
        }

        render(results as JSON)
    }

    def activities(Integer offset, Integer max) {
        Profile author = getSelfOrAuthorFromParamId()

        List<ServiceItemActivity> results = serviceItemActivityRestService.getAllByProfile(author, offset, max)

        render(results as JSON)
    }

    def serviceItemActivities(Integer offset, Integer max) {
        Profile owner = getSelfOrAuthorFromParamId()

        List<ServiceItemActivity> results = serviceItemActivityRestService.getAllByServiceItemOwner(owner, offset, max)

        render(results as JSON)
    }

    private Profile getSelfOrAuthorFromParamId() {
        params.id == 'self' ? currentUserProfile : profileRestService.getById(params.long('id'))
    }

    private Profile getCurrentUserProfile() {
        profileRestService.currentUserProfile
    }

}
