package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

@TestMixin(IntegrationTestMixin)
class TextServiceTests {

	def textService

    void testAuditTrail() {
        def startTime = new Date()
        Thread.sleep(1000)

        Text obj = new Text(name: 'test text')
        obj.save(failOnError: true, flush:true)

        obj = Text.get(obj.id)
        assert null != obj
        def createdDate1 = obj.createdDate
        def editedDate1 = obj.editedDate

        assert obj.metaClass.hasProperty(obj, "createdBy")
        assert obj.metaClass.hasProperty(obj, "createdDate")
        assert obj.metaClass.hasProperty(obj, "editedBy")
        assert obj.metaClass.hasProperty(obj, "editedDate")

        assert obj.createdDate.after(startTime)
        assert obj.editedDate.after(startTime)

        obj.name = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = Text.get(obj.id)
        assert obj.createdDate.equals(createdDate1)
        assert obj.editedDate.after(editedDate1)
    }

}
