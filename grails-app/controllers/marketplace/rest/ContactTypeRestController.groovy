package marketplace.rest

import grails.converters.JSON

import org.springframework.http.HttpStatus

import marketplace.ContactType


class ContactTypeRestController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    ContactTypeRestService contactTypeRestService

    def index(Integer offset, Integer max) {
        List<ContactType> results = contactTypeRestService.getAll(offset, max)

        render(results as JSON)
    }

    def show(Long id) {
        ContactType result = contactTypeRestService.getById(id)

        render(result as JSON)
    }

    def save(ContactType contactType) {
        ContactType result = contactTypeRestService.createFromDto(contactType)

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def update(ContactType contactType) {
        ContactType result = contactTypeRestService.updateById(contactType.id, contactType)

        render(result as JSON)
    }

    def delete(Long id) {
        contactTypeRestService.deleteById(id)

        render(status: HttpStatus.NO_CONTENT.value())
    }

}
