package ozone.utils

import marketplace.AuditStamped
import marketplace.UserDomainInstance
 
class User extends AuditStamped{
    String username
    String name
    String org
    String email
	
    UserDomainInstance domainInstance
}
