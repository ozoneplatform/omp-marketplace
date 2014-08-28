package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.domain.ValidationException;

@TestMixin(IntegrationTestMixin)
class RejectionJustificationServiceTests {
	def rejectionJustificationService
	def serviceItemService
	def rejectionListingService

	void testBogusDelete() {
		boolean veThrown = false
		try{
			rejectionJustificationService.delete(427358374529952)
		}
		catch(ValidationException ve){
			veThrown = true
			assert 'objectNotFound' == ve.message
		}
		assert true ==  veThrown
	}

	void testDelete() {
		RejectionJustification rejectionJustification = new RejectionJustification(title: 'rejection').save(failOnError:true)
		rejectionJustification.save(flush:true)
		assert null != RejectionJustification.get(rejectionJustification.id)
		rejectionJustificationService.delete(rejectionJustification.id)
		RejectionJustification stateAfterSoftDelete = RejectionJustification.get(rejectionJustification.id)
		assert null == stateAfterSoftDelete
	}

    void testAuditTrail() {
        def startTime = new Date()
        Thread.sleep(1000)

        RejectionJustification obj = new RejectionJustification(title: 'rejection').save(failOnError:true)
        obj.save(flush:true)

        obj = RejectionJustification.get(obj.id)
        assert null != obj
        def createdDate1 = obj.createdDate
        def editedDate1 = obj.editedDate

        assert obj.metaClass.hasProperty(obj, "createdBy")
        assert obj.metaClass.hasProperty(obj, "createdDate")
        assert obj.metaClass.hasProperty(obj, "editedBy")
        assert obj.metaClass.hasProperty(obj, "editedDate")

        assert obj.createdDate.after(startTime)
        assert obj.editedDate.after(startTime)

        obj.title = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = RejectionJustification.get(obj.id)
        assert obj.createdDate.equals(createdDate1)
        assert obj.editedDate.after(editedDate1)
    }

	//Check that once createRequired runs the list of objects is > 0
	void testCreateRequired(){
        def numRejectionJustifications = RejectionJustification.count()

        // check required justs. were created during bootstrap as expected.
        assert numRejectionJustifications > 0

        // check no new justs. are created if createRequired runs again
        rejectionJustificationService.createRequired()
	    assert numRejectionJustifications == RejectionJustification.count()
	}
}
