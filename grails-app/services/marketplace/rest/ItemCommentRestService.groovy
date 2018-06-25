package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.security.access.AccessDeniedException

import grails.core.GrailsApplication

import marketplace.AccountService
import marketplace.Sorter

import marketplace.ServiceItem
import marketplace.ItemComment
import marketplace.Profile

import marketplace.Constants

@Service
class ItemCommentRestService extends ChildObjectRestService<ServiceItem, ItemComment> {
    @Autowired ProfileRestService profileRestService
    @Autowired AccountService accountService

    @Autowired
    ItemCommentRestService(GrailsApplication grailsApplication,
            ServiceItemRestService serviceItemRestService) {
        super(ServiceItem.class, 'serviceItem', 'itemComments',
            grailsApplication, ItemComment.class,
            serviceItemRestService, null,
            new Sorter<ItemComment>(Constants.SortDirection.DESC, 'editedDate'))
    }

    ItemCommentRestService() {}

    @Override
    public void deleteById(Long id) {
        ItemComment obj = getById(id)
        ServiceItem si = parentClassRestService.getById(obj.serviceItem.id)


        super.deleteById(id)

        //ensure that the ServiceItem's statistics are updated
        si.removeFromItemComments(obj)
        si.updateRatingStats()
    }

    /**
     * Get all ItemComments by the given author, ordered most-recent first.
     * Secondarily sorted by ServiceItem title
     */
    List<ItemComment> getAllByAuthor(Profile author) {
        //sort in the application because the primary sort will be all thats necessary almost
        //100% of the time.  If we sort in the database, it'll sort everything by the
        //serviceItem title first and then sort it all by the editedDate
        ItemComment.findAllByAuthor(author).grep { canView(it) }.sort { a, b ->
            //negative for desc
            -(a.editedDate <=> b.editedDate) ?: a.serviceItem.title <=> b.serviceItem.title
        }
    }

    @Override
    protected void authorizeUpdate(ItemComment existing) {
        super.authorizeUpdate(existing)

        //comment authors and admins are allowed
        if (profileRestService.currentUserProfile != existing.author) {
            accountService.checkAdmin("Attempt by non-admin to update another user's comment")
        }
    }

    /**
     * Anyone who can view comments on this listing can create new ones
     */
    @Override
    protected void authorizeCreate(ItemComment dto) {
        authorizeView(dto)
    }

    protected void postprocess(ItemComment updated, ItemComment original = null) {
        super.postprocess(updated, original)

        setAuthor(updated)

        if (original) {
            preventNonOwnerRatingChange(updated, original)
        }

        syncServiceItemStats(updated)
        updated.serviceItem.updateRatingStats()
    }

    private void setAuthor(ItemComment comment) {
        if (!comment.author) comment.author = profileRestService.currentUserProfile
    }

    private void syncServiceItemStats(ItemComment updated) {
        ServiceItem si = updated.serviceItem
        si.addToItemComments(updated)   //ensure that the collection is up to date
        si.updateRatingStats()
    }

    private void preventNonOwnerRatingChange(ItemComment updated, ItemComment original) {
        if (updated.rate != original.rate &&
                profileRestService.currentUserProfile != original.author) {
            throw new AccessDeniedException("Attempt by non-owner to change comment rating")
        }
    }
}
