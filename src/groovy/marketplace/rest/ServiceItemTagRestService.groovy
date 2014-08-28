package marketplace.rest

import marketplace.*
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
    public ServiceItemTagRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, ServiceItemTag.class, null, null)
    }

    ServiceItemTagRestService(){}

    //This had to be overridden so that we could create a service item activity and change detail
    //Also, note - creating the serviceItem -> activity relationship is what triggers the search reindex
    @Override
    @Transactional
    public void deleteById(Long id) {
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

    @Override
    @Transactional
    protected void postprocess(ServiceItemTag updated, ServiceItemTag original = null) {
        super.postprocess(updated, original)

        if (original == null) {
            serviceItemActivityInternalService.addServiceItemTagActivity(
                Constants.Action.TAG_CREATED, updated)
        }
    }

    @Transactional(readOnly=true)
    public ServiceItemTag getByServiceItemIdAndTagId(long serviceItemId, long tagId){
        ServiceItem serviceItem = serviceItemRestService.getById(serviceItemId)
        Tag tag = Tag.read(tagId)
        ServiceItemTag.findByServiceItemAndTag(serviceItem, tag)
    }

    @Transactional(readOnly=true)
    public Collection<ServiceItemTag> getAllByServiceItemId(long serviceItemId){
        ServiceItem serviceItem = serviceItemRestService.getById(serviceItemId)
        ServiceItemTag.findAllByServiceItem(serviceItem).sort { it.tag }
    }

    /**
     * @return all ServiceItemTags created by the given profile sorted by Tag
     */
    @Transactional(readOnly=true)
    public List<ServiceItemTag> getAllByProfileId(long profileId){
        Profile profile = profileRestService.getById(profileId)

        ServiceItemTag.findAllByCreatedBy(profile)
            .grep { serviceItemRestService.canView(it.serviceItem) }.sort { it.tag }
    }
}
