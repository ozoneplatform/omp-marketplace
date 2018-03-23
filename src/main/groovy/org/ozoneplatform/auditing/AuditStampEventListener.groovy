package org.ozoneplatform.auditing

import groovy.transform.CompileStatic

import grails.events.annotation.gorm.Listener
import org.grails.datastore.mapping.engine.EntityAccess
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent

import org.springframework.beans.factory.annotation.Autowired

import marketplace.AccountService
import marketplace.AuditStamped
import marketplace.Profile


@CompileStatic
class AuditStampEventListener {

    @Autowired
    AccountService accountService


    @Listener(AuditStamped)
    void onPreInsert(PreInsertEvent event) {
        Object object = event.entityObject

        if (!(object instanceof AuditStamped)) return

        AuditStamped entity = (AuditStamped) object
        EntityAccess entityAccess = event.entityAccess

        Long currentUserId = accountService.getLoggedInUserProfileId()
        Date now = new Date()

        if (!entity.createdBy) {
            entityAccess.setProperty('createdBy', currentUserId)
            entityAccess.setProperty('createdDate', now)
        }

        entityAccess.setProperty('editedBy', currentUserId)
        entityAccess.setProperty('editedDate', now)
    }

    @Listener(AuditStamped)
    void onPreUpdate(PreUpdateEvent event) {
        Object object = event.entityObject

        if (!(object instanceof AuditStamped)) return

        EntityAccess entityAccess = event.entityAccess

        Long currentUserId = accountService.getLoggedInUserProfileId()

        entityAccess.setProperty('editedBy', currentUserId)
        entityAccess.setProperty('editedDate', new Date())
    }

}
