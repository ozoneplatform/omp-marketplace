package grails.plugins.elasticsearch.mapping

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.plugins.elasticsearch.util.IndexNamingUtils
import grails.test.mixin.Mock
import org.grails.core.DefaultGrailsDomainClass
import spock.lang.Specification
import test.Photo
import test.upperCase.UpperCase

@Mock([Photo, UpperCase])
class SearchableClassMappingSpec extends Specification {

    def "indexing and querying index are calculated based on the index name"() {
        given:
        def domainClass = Mock(DomainEntity)
        domainClass.getPackageName() >> packageName

        when:
        SearchableClassMapping scm = new SearchableClassMapping(null, domainClass, [])

        then:
        scm.indexName == packageName
        scm.queryingIndex == IndexNamingUtils.queryingIndexFor(packageName)
        scm.indexingIndex == IndexNamingUtils.indexingIndexFor(packageName)
        scm.queryingIndex != scm.indexingIndex
        scm.indexName != scm.queryingIndex
        scm.indexName != scm.indexingIndex

        where:
        packageName << ["test.scm", "com.mapping"]
    }

    void testGetIndexName() throws Exception {
        GrailsDomainClass dc = new DefaultGrailsDomainClass(Photo.class)
        dc.grailsApplication = [:] as GrailsApplication
        SearchableClassMapping mapping = new SearchableClassMapping(dc, null)
        assert 'test' == mapping.getIndexName()
    }

    void testManuallyConfiguredIndexName() throws Exception {
        GrailsDomainClass dc = new DefaultGrailsDomainClass(Photo.class)
        dc.grailsApplication = [:] as GrailsApplication
        config.elasticSearch.index.name = 'index-name'
        SearchableClassMapping mapping = new SearchableClassMapping(dc, null)
        assert 'index-name' == mapping.getIndexName()
    }

    void testIndexNameIsLowercaseWhenPackageNameIsLowercase() throws Exception {
        GrailsDomainClass dc = new DefaultGrailsDomainClass(UpperCase.class)
        dc.grailsApplication = [:] as GrailsApplication
        SearchableClassMapping mapping = new SearchableClassMapping(dc, null)
        String indexName = mapping.getIndexName()
        assert 'test.uppercase' == indexName
    }

    void cleanup() {
        config.elasticSearch.index.name = null
    }
}
