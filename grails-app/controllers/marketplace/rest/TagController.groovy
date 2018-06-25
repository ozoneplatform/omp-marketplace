package marketplace.rest

import groovy.transform.CompileStatic

import grails.converters.JSON

import org.springframework.http.HttpStatus

import marketplace.Tag


@CompileStatic
class TagController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    TagRestService tagRestService

    def index(Integer offset, Integer max) {
        List<Tag> results = tagRestService.getAll(offset, max)

        render(results as JSON)
    }

    def show(Long id) {
        Tag result = tagRestService.getById(id)

        render(result as JSON)
    }

    def save(Tag tag) {
        Tag result = tagRestService.createFromDto(tag)

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def update(Tag tag) {
        Tag result = tagRestService.updateById(tag.id, tag)

        render(result as JSON)
    }

    def delete(Long id) {
        tagRestService.deleteById(id)

        render(status: HttpStatus.NO_CONTENT.value())
    }

    def findByTitle(String title) {
        List<Tag> results = tagRestService.findAllLikeTitle(title)

        render(results as JSON)
    }

}
