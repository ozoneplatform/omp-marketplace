package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(Images)
class ImagesTests {
    def images

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(Images)
        images = new Images()
    }

    void testNullConstraints(){
        TestUtil.assertPropertyRequired('bytes',images)
        TestUtil.assertPropertyRequired('type',images)
    }

    void testSizeContraints(){
        Long maxSize = 10*1024*1024

        def testProperty = { size, assertFunc ->
            def mockImageBytes = TestUtil.getStringOfLength(size).bytes
            images.bytes = mockImageBytes
            assertFunc()
        }

        //Test below max size
        testProperty(maxSize - 1, TestUtil.&assertNoErrorOnProperty.curry('bytes', images))

        //Test max size exactly
        testProperty(maxSize, TestUtil.&assertNoErrorOnProperty.curry('bytes', images))

        //Test above max size
        testProperty(maxSize + 1, TestUtil.&assertPropertyTooLarge.curry('bytes', images, "bytes should have failed with a length of ${maxSize}, but did not."))

    }


}
