package grails.plugins.elasticsearch.mapping

import grails.core.GrailsApplication
import grails.plugins.elasticsearch.ElasticSearchContextHolder
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll
import test.Building
import test.Person
import test.Product
import test.mapping.migration.Catalog
import test.transients.Anagram
import test.transients.Palette

/**
 * Created by @marcos-carceles on 28/01/15.
 */
@Integration
class ElasticSearchMappingFactorySpec extends Specification {

    @Autowired
    GrailsApplication grailsApplication
    @Autowired
    SearchableClassMappingConfigurator searchableClassMappingConfigurator
    @Autowired
    ElasticSearchContextHolder elasticSearchContextHolder

    void setup() {
        grailsApplication.config.elasticSearch.includeTransients = true
        searchableClassMappingConfigurator.configureAndInstallMappings()
    }

    void cleanup() {
        grailsApplication.config.elasticSearch.includeTransients = false
        searchableClassMappingConfigurator.configureAndInstallMappings()
    }


    @Unroll('#clazz / #property is mapped as #expectedType')
    void "calculates the correct ElasticSearch types"() {
        given:
        def scm = elasticSearchContextHolder.getMappingContextByType(clazz)

        when:
        Map mapping = ElasticSearchMappingFactory.getElasticMapping(scm)

        then:
        mapping[clazz.simpleName.toLowerCase()]['properties'][property].type == expectedType

        where:
        clazz    | property          || expectedType

        Building | 'name'            || 'text'
        Building | 'date'            || 'date'
        Building | 'location'        || 'geo_point'

        Product  | 'price'           || 'float'
        Product  | 'json'            || 'object'

        Catalog  | 'pages'           || 'object'

        Person   | 'fullName'        || 'text'
        Person   | 'nickNames'       || 'text'

        Palette  | 'colors'          || 'text'
        Palette  | 'complementaries' || 'text'

        Anagram  | 'length'          || 'integer'
        Anagram  | 'palindrome'      || 'boolean'
    }
}
//['string', 'integer', 'long', 'float', 'double', 'boolean', 'null', 'date']
