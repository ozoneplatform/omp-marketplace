package marketplace.service

import grails.testing.mixin.integration.Integration
import grails.testing.services.ServiceUnitTest
import marketplace.Types
import marketplace.TypesService
import marketplace.OwfSpecMixin
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class TypesServiceIntegrationSpec extends Specification implements ServiceUnitTest<TypesService>, OwfSpecMixin{
    Types types1
    Types types2
	
	def typesService; 
	
    void setup() {
        types1 = new Types(title: "Types 1")
        types2 = new Types(title: "Types 2")

        save(types1)
        save(types2)
    }
    void testDelete() {
        when:
        typesService.delete(types1.id)

        then:
        typesService.getAllTypes().contains(types2)
        //!typesService.getAllTypes().contains(types1)

        cleanup:
        typesService.delete(types2.id)
    }
}
