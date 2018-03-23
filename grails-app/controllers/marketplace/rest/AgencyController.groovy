package marketplace.rest

import grails.converters.JSON

import org.springframework.http.HttpStatus

import marketplace.Agency


class AgencyController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    AgencyRestService agencyRestService

    def index(Integer offset, Integer max) {
        List<Agency> results = agencyRestService.getAll(offset, max)

        render(results as JSON)
    }

    def show(Long id) {
        Agency result = agencyRestService.getById(id)
        render(result as JSON)
    }

    def save(Agency agency) {
        Agency result = agencyRestService.createFromDto(agency)
        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def update(Agency agency) {
        Agency result = agencyRestService.updateById(agency.id, agency)
        render(result as JSON)
    }

    def delete(Long id) {
        agencyRestService.deleteById(id)
        render(status: HttpStatus.NO_CONTENT.value())
    }

}
