package marketplace.rest

import marketplace.*
import grails.core.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import grails.gorm.transactions.Transactional

@Service
@Transactional
class ServiceItemTagRestService extends RestService<ServiceItemTag> {

    @Autowired
    AccountService accountService

    @Autowired
    ProfileRestService profileRestService

    @Autowired
    GrailsApplication grailsApplication

    @Autowired
    ServiceItemActivityInternalService serviceItemActivityInternalService

    @Autowired
    ServiceItemRestService serviceItemRestService

    @Autowired
    TagRestService tagRestService

    @Autowired
    ServiceItemTagRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, ServiceItemTag.class, null, null)
    }

    ServiceItemTagRestService(){}

    //This had to be overridden so that we could create a service item activity and change detail
    //Also, note - creating the serviceItem -> activity relationship is what triggers the search reindex
    @Override
    @Transactional
    void deleteById(Long id) {
        ServiceItemTag serviceItemTag = ServiceItemTag.read(id)
        ServiceItem serviceItem = serviceItemTag.serviceItem
        Tag t = serviceItemTag.tag

        serviceItemActivityInternalService.addServiceItemTagActivity(
            Constants.Action.TAG_DELETED, serviceItemTag)

        super.deleteById(id)


        Collection<ServiceItemTag> siTagsForId = ServiceItemTag.createCriteria().list {
            tag {
                eq('id', t.id)
            }
        }
        
        if (siTagsForId.isEmpty()) {
            t.delete(flush: true)
        }
    }

    //Admins can modify any tag for any service item.
    //Users can only modify tags they created
    @Override
    protected void authorizeUpdate(ServiceItemTag serviceItemTag) {
        if(this.accountService.isAdmin())
            return

        Profile currentUser = profileRestService.currentUserProfile

        if(!currentUser.equals(serviceItemTag?.createdBy)){
            //TODO CEF log?
            throw new AccessDeniedException("Unauthorized attempt to modify ServiceItem with " + "id ${serviceItemTag.serviceItem.id} by user ${currentUser.username}")
        }
    }

    @Override
    protected void populateDefaults(ServiceItemTag serviceItemTag) {
        serviceItemTag.createdBy = profileRestService.currentUserProfile
    }

    @Override
    protected void preprocess(ServiceItemTag serviceItemTag) {

        if(!serviceItemTag.tag.title) serviceItemTag.tag.title = ""

        Tag existingTag = Tag.findByTitle(serviceItemTag.tag.title)

        //If this tag exists (by title) then use it. Otherwise trim the incoming new tag.
        if(existingTag){
            serviceItemTag.tag.id = existingTag.id
        } else{
            serviceItemTag.tag.trim()
        }

        serviceItemTag.serviceItem = serviceItemRestService.getById(serviceItemTag.serviceItem.id)
    }

    @Transactional
    protected void postprocess(ServiceItemTag updated, ServiceItemTag original = null) {
        super.postprocess(updated, original)

        if (original == null) {
            serviceItemActivityInternalService.addServiceItemTagActivity(
                Constants.Action.TAG_CREATED, updated)
        }
    }

    @Transactional(readOnly=true)
    ServiceItemTag getByServiceItemIdAndTagId(long serviceItemId, long tagId){
        ServiceItem serviceItem = serviceItemRestService.getById(serviceItemId)
        Tag tag = tagRestService.getById(tagId)

        ServiceItemTag serviceItemTag = ServiceItemTag.findByServiceItemAndTag(serviceItem, tag)
        if (!serviceItemTag) {
            throw new DomainObjectNotFoundException("ServiceItemTag for ServiceItem $serviceItemId and Tag $tagId not found")
        }

        serviceItemTag
    }

    @Transactional(readOnly=true)
    Collection<ServiceItemTag> getAllByServiceItemId(long serviceItemId){
        ServiceItem serviceItem = serviceItemRestService.getById(serviceItemId)
        ServiceItemTag.findAllByServiceItem(serviceItem).sort { it.tag }
    }


    /**
     * @return all ServiceItemTags created by the given profile sorted by Tag
     */
    @Transactional(readOnly=true)
    List<ServiceItemTag> getAllByProfile(Profile profile){
        ServiceItemTag.findAllByCreatedBy(profile).grep { ServiceItemTag it ->
            serviceItemRestService.canView(it.serviceItem)
        }.sort { it.tag }
    }
}
