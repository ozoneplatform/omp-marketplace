package ${packageName}

import org.grails.plugins.jaxrs.provider.DomainObjectNotFoundException

class ${simpleName}ResourceService {

    def create(${simpleName} dto) {
        dto.save()
    }

    def read(id) {
        def obj = ${simpleName}.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${simpleName}.class, id)
        }
        obj
    }

    def readAll() {
        ${simpleName}.findAll()
    }

    def update(${simpleName} dto) {
        def obj = ${simpleName}.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${simpleName}.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ${simpleName}.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
