package marketplace.service

import grails.testing.mixin.integration.Integration
import marketplace.Text
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class TextServiceSpec extends Specification{

    void testAuditTrail() {
        when:
        def startTime = new Date()
        Thread.sleep(1000)

        Text obj = new Text(name: 'test text')
        obj.save(failOnError: true, flush:true)

        obj = Text.get(obj.id)
        then:
        null != obj

        when:
        def createdDate1 = obj.createdDate
        def editedDate1 = obj.editedDate

        then:
        obj.metaClass.hasProperty(obj, "createdBy")
        obj.metaClass.hasProperty(obj, "createdDate")
        obj.metaClass.hasProperty(obj, "editedBy")
        obj.metaClass.hasProperty(obj, "editedDate")
        obj.createdDate.after(startTime)
        obj.editedDate.after(startTime)

        when:
        obj.name = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = Text.get(obj.id)
        then:
        obj.createdDate.equals(createdDate1)
        obj.editedDate.after(editedDate1)
    }

}
