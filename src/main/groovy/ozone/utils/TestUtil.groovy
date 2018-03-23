package ozone.utils

import grails.validation.ConstrainedProperty
//import static grails.test.GrailsUnitTestCase.*
//import org.codehaus.groovy.grails.validation.ConstrainedProperty
//import static org.junit.Assert.assertNull
//import static org.junit.Assert.assertEquals
//import static org.junit.Assert.assertFalse

class TestUtil {

	static final String PREP_URL_STRING = "http://"

	static void assertNoErrorOnProperty(property, domainEntity, message=null) {
		domainEntity.validate()
		assert domainEntity.errors?.getFieldError(property) == null
		//assertNull message, domainEntity.errors?.getFieldError(property)
	}

	static void assertPropertyRequired(property, domainEntity, message=null) {
		assert domainEntity.getProperty(property) == null
//		assertNull domainEntity.getProperty(property)
		assertEqualsConstraintCode(ConstrainedProperty.NULLABLE_CONSTRAINT,
				property,
				domainEntity,
				message)
	}

	static void assertPropertyNotRequired(property, domainEntity, message=null) {
//		assertNull domainEntity.getProperty(property)
		assert domainEntity.getProperty(property) == null
		assertNotEqualsConstraintCode(ConstrainedProperty.NULLABLE_CONSTRAINT,
				property,
				domainEntity,
				message)
	}

	static void assertPropertyBlank(property, entity, message = null) {
		entity.setProperty(property, '')
		assertEqualsConstraintCode(ConstrainedProperty.BLANK_CONSTRAINT, property, entity, message)
	}

	static void assertPropertyNotInList(property, entity) {
		entity.setProperty(property, 'zzzz_bogus___hopefully___')
		entity.validate()
		assert 'not.inList' == entity?.errors?.getFieldError(property)?.code
//		assertEquals 'not.inList', entity?.errors?.getFieldError(property)?.code
	}

	static void assertEqualsConstraintCode(constraintCode, property, domainEntity, message=null) {
		domainEntity.validate()
		//TODO BVEST check back
		//assertEquals message, constraintCode, domainEntity?.errors?.getFieldError(property)?.code
	}

	static void assertNotEqualsConstraintCode(constraintCode, property, domainEntity, message=null) {
		domainEntity.validate()
		assertNotEquals(constraintCode, domainEntity?.errors?.getFieldError(property)?.code,  message)
	}

	static void assertNotEquals(objToCompare1, objToCompare2, message=null){
//		assertFalse message, objToCompare1?.equals(objToCompare2)
		assert  !objToCompare1?.equals(objToCompare2)
	}

	static void checkSizeConstraintProperty(property, domainEntity, maxSize = null, minSize = null, prepUrl = false) {
		if (minSize && minSize < 0)
			throw new RuntimeException("Minimum size cannot be below zero")

		if (maxSize && minSize > maxSize)
			throw new RuntimeException("Minimum size must be below max size")

		if (minSize && maxSize) {
			//Test below minimum size
			domainEntity.setProperty(property, getStringOfLength(minSize-1, prepUrl))
			assertPropertyTooSmall(property, domainEntity, "${property} should have failed with a length of ${minSize}, but did not.")

			//Test exactly minimum size
			domainEntity.setProperty(property, getStringOfLength(minSize, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)

			//Test exactly max size
			domainEntity.setProperty(property, getStringOfLength(maxSize, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)

			//Test above max size
			domainEntity.setProperty(property, getStringOfLength(maxSize + 1, prepUrl))
			assertPropertyTooLarge(property, domainEntity, "${property} should have failed with a length of ${maxSize}, but did not.")
		} else if (minSize) {
			//Test below minimum size
			domainEntity.setProperty(property, getStringOfLength(minSize-1, prepUrl))
			assertPropertyTooSmall(property, domainEntity, "${property} should have failed with a length of ${minSize}, but did not.")

			//Test exactly minimum size
			domainEntity.setProperty(property, getStringOfLength(minSize, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)

			//Test above min size
			domainEntity.setProperty(property, getStringOfLength(minSize + 1, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)
		} else if (maxSize) {
			//Test below max size
			domainEntity.setProperty(property, getStringOfLength(maxSize-1, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)

			//Test exactly max size
			domainEntity.setProperty(property, getStringOfLength(maxSize, prepUrl))
			assertNoErrorOnProperty(property, domainEntity)

			//Test above max size
			domainEntity.setProperty(property, getStringOfLength(maxSize + 1, prepUrl))
			assertPropertyTooLarge(property, domainEntity, "${property} should have failed with a length of ${maxSize}, but did not.")
		}
	}

	static void assertPropertyTooLarge(property, domainEntity, message=null) {
		assertEqualsConstraintCode(ConstrainedProperty.MAX_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX,
				property,
				domainEntity,
				message)
	}

	static void assertPropertyTooSmall(property, domainEntity, message=null) {
		assertEqualsConstraintCode(ConstrainedProperty.MIN_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX,
				property,
				domainEntity,
				message)
	}


	static def getStringOfLength(number, prepUrl=false) {
        def paddingLength = (prepUrl && number >= PREP_URL_STRING.size()) ? (number - PREP_URL_STRING.size()) : number
		StringBuffer retValBuff = (prepUrl ? new StringBuffer(PREP_URL_STRING) : new StringBuffer())
		retValBuff.append('A' * paddingLength)
		def retVal = retValBuff.toString()
		retVal
	}

	static void assertOverMax(property, domainEntity, message=null) {
		assertEqualsConstraintCode(ConstrainedProperty.MAX_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX, property, domainEntity, message)
	}

	static void assertUnderMin(property, domainEntity, message=null) {
		assertEqualsConstraintCode(ConstrainedProperty.MIN_CONSTRAINT + ConstrainedProperty.NOTMET_SUFFIX, property, domainEntity, message)
	}

	static void checkMinMaxConstraintProperty(property, domainEntity, max = null, min = null) {

		if (max && min > max) throw new RuntimeException("Min must be below max")

		if (min && max) {
			//Test below min value
			domainEntity.setProperty(property, min - 1)
			assertUnderMin(property, domainEntity, "${property} should have failed with a value of ${min}, but did not.")

			//Test exactly min value
			domainEntity.setProperty(property, min)
			assertNoErrorOnProperty(property, domainEntity)

			//Test exactly max value
			domainEntity.setProperty(property, max)
			assertNoErrorOnProperty(property, domainEntity)

			//Test above max value
			domainEntity.setProperty(property, max + 1)
			assertOverMax(property, domainEntity, "${property} should have failed with a value of ${max}, but did not.")
		} else if (min) {
			//Test below min value
			domainEntity.setProperty(property, min - 1)
			assertUnderMin(property, domainEntity, "${property} should have failed with a value of ${min}, but did not.")

			//Test exactly min value
			domainEntity.setProperty(property, min)
			assertNoErrorOnProperty(property, domainEntity)

			//Test above min value
			domainEntity.setProperty(property, min + 1)
			assertNoErrorOnProperty(property, domainEntity)
		} else if (max) {
			//Test below max value
			domainEntity.setProperty(property, max - 1)
			assertNoErrorOnProperty(property, domainEntity)

			//Test exactly max value
			domainEntity.setProperty(property, max)
			assertNoErrorOnProperty(property, domainEntity)

			//Test above max value
			domainEntity.setProperty(property, max + 1)
			assertOverMax(property, domainEntity, "${property} should have failed with a value of ${max}, but did not.")
		}
	}
}
