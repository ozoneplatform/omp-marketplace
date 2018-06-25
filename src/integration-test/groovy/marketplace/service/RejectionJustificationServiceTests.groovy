package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.RejectionJustification
import marketplace.OwfSpecMixin
import ozone.marketplace.domain.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class RejectionJustificationServiceTests extends Specification implements OwfSpecMixin{
	def rejectionJustificationService
	def serviceItemService
	def rejectionListingService

	void testBogusDelete() {
        when:
		rejectionJustificationService.delete(427358374529952)
		then:
        def ve = thrown(ValidationException)
        'objectNotFound' == ve.message
	}

	void testDelete() {
        when:
		RejectionJustification rejectionJustification = new RejectionJustification(title: 'rejection').save(failOnError:true)
		rejectionJustification.save(flush:true)
		then:
        null != RejectionJustification.get(rejectionJustification.id)

        when:
        rejectionJustificationService.delete(rejectionJustification.id)
		RejectionJustification stateAfterSoftDelete = RejectionJustification.get(rejectionJustification.id)
		then:
        null == stateAfterSoftDelete
	}

    void testAuditTrail() {
        when:
        def startTime = new Date()
        Thread.sleep(1000)

        RejectionJustification obj = new RejectionJustification(title: 'rejection').save(failOnError:true)
        obj.save(flush:true)

        obj = RejectionJustification.get(obj.id)
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
        obj.title = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = RejectionJustification.get(obj.id)
        then:
        obj.createdDate.equals(createdDate1)
        obj.editedDate.after(editedDate1)
    }

	//Check that once createRequired runs the list of objects is > 0
	void testCreateRequired(){
        when:
        def numRejectionJustifications = RejectionJustification.count()

        // check required justs. were created during bootstrap as expected.
        then:
        numRejectionJustifications > 0

        // check no new justs. are created if createRequired runs again
        when:
        rejectionJustificationService.createRequired()
	    then:
        numRejectionJustifications == RejectionJustification.count()
	}
}
