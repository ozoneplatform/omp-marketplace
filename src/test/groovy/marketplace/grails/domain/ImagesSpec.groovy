package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(Images)
class ImagesSpec extends Specification implements DomainConstraintsUnitTest<Images>{
    def images
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    List<Class> getDomainClasses() {[Images]}
    void setup() {
//        FakeAuditTrailHelper.install()
//
//        mockForConstraintsTests(Images)
        images = new Images()
    }

    void testNullConstraints(){
        expect:
        propertyIsRequired('bytes')
        propertyIsRequired('type')
//        TestUtil.assertPropertyRequired('bytes',images)
//        TestUtil.assertPropertyRequired('type',images)
    }

//    void testSizeContraints(){
//        given:
//        int maxSize = 10*1024*1024
//
//        def testProperty = { size, assertFunc ->
//            def mockImageBytes = TestUtil.getStringOfLength(size).bytes
//            images.bytes = mockImageBytes
//            assertFunc()
//        }
//
//        expect:
//        //Test below max size
//        //TODO too large test is failing
//        propertyHasMaxSize('bytes', maxSize)
//        //Test max size exactly
////        testProperty(maxSize, TestUtil.&assertNoErrorOnProperty.curry('bytes', images))
////
////        //Test above max size
////        testProperty(maxSize + 1, TestUtil.&assertPropertyTooLarge.curry('bytes', images, "bytes should have failed with a length of ${maxSize}, but did not."))
//
//    }


}
