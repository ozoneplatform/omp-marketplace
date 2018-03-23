package marketplace.grails.domain

import grails.gorm.validation.ConstrainedProperty
import marketplace.Types
import org.grails.web.json.JSONObject
import spock.lang.Specification

class TypesSpec extends  Specification implements DomainConstraintsUnitTest<Types>{

    void testBlankConstraints(){
        expect:
        propertyIsRequired('title')
        propertyValueIsInvalid('title', "", ConstrainedProperty.BLANK_CONSTRAINT)
    }

    void testNullConstraints(){
        expect:
        propertyIsRequired('title')
        propertyValueIsInvalid('title', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
    }

    void testSizeContraints(){
        expect:
        propertyHasMaxSize('title', 50)
    }

    void testFindDuplicates(){
        given:
        def testTypes = new Types(title: "type1", uuid: "1234")
        mockDomain(Types, [testTypes])

        when:
        def duplicateUuidTypes = new JSONObject(title: "type2", uuid: "1234")
        then:
        assert testTypes.uuid == duplicateUuidTypes.uuid
        assert Types.findDuplicates(duplicateUuidTypes)

        when:
        def duplicateTitleTypes = new JSONObject(title: "type1", uuid: "4321")
        then:
        assert testTypes.title == duplicateTitleTypes.title
        assert Types.findDuplicates(duplicateTitleTypes)

        when:
        def uniqueTypes = new JSONObject(title: "type3", uuid: "4321")
        then:
        assert testTypes.uuid != uniqueTypes.uuid
        assert testTypes.title != uniqueTypes.title
        assert !Types.findDuplicates(uniqueTypes)
    }
}
