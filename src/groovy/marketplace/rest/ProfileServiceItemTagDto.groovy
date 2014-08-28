package marketplace.rest

import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.ServiceItemTag


class ProfileServiceItemTagDto {
    private ServiceItemTag serviceItemTag

    ProfileServiceItemTagDto(ServiceItemTag serviceItemTag) {
        this.serviceItemTag = serviceItemTag
    }

    JSONObject asJSON() {
        serviceItemTag == null ? JSONObject.NULL :
            new JSONObject(
                id: serviceItemTag.id,
                tag: new JSONObject(
                    id: serviceItemTag.tag.id,
                    title: serviceItemTag.tag.title
                ),
                serviceItem: new JSONObject(
                    id: serviceItemTag.serviceItem.id,
                    title: serviceItemTag.serviceItem.title
                )
            )
    }
}
