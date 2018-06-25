package ozone.marketplace.controller;

enum MarketplaceExceptionTypes{
	Authorization(401,'You are not authorized to access this entity.'),
	Database(500, 'An unexpected database error occurred'),
	NotFound(404, 'The requested entity was not found'),
	Validation(400, 'The requested entity failed to pass validation.'),
	JsonToDomainColumnMapping(500, 'The json property has not been mapped to a domain column')
	
	def normalReturnCode
	def generalMessage
	
	MarketplaceExceptionTypes(normalReturnCode, generalMessage)
	{
		this.normalReturnCode = normalReturnCode
		this.generalMessage = generalMessage
	}
}


class MarketplaceException extends RuntimeException {
	
	String message
	
	MarketplaceExceptionTypes exceptionType
	
	String logLevel = 'ERROR'
		
}