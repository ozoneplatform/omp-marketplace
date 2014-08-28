package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import java.nio.file.AccessDeniedException
import marketplace.AccountService
import marketplace.Profile
import marketplace.ProfileService
import marketplace.Tag
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException

@Service
class TagRestService extends RestService<Tag> {

    @Autowired
    ServiceItemTagRestService serviceItemTagRestService

    @Autowired
    ProfileService profileService

    @Autowired
    GrailsApplication grailsApplication

    @Autowired
    AccountService accountService

    @Autowired
    public TagRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Tag.class, null, null)
    }

    TagRestService(){}

    @Override
    protected void authorizeUpdate(Tag tag) {
        if(this.accountService.isAdmin())
            return

        Profile currentUser = profileService.findByUsername(accountService.loggedInUsername)

        throw new AccessDeniedException("Unauthorized attempt to delete tag " +  " ${tag.title} by user ${currentUser.username}")
    }


    @Transactional(readOnly=true)
    public Collection<Tag> getAll(Integer offset, Integer max) {
        super.getAll(offset, max).collect{it.asJSON()}
    }

    @Transactional(readOnly=true)
    public Collection<Tag> findAllLikeTitle(String search) {
        def tags = Tag.findAllByTitleIlike("${search}%", [max: '10'])
        tags.sort { it.title.toLowerCase() }.collect{it.asJSON()}
    }
}
