package marketplace.rest

import org.grails.web.json.JSONObject

import marketplace.ServiceItemTag
import marketplace.ToJSON


class ProfileServiceItemTagDto implements ToJSON {

    private ServiceItemTag serviceItemTag

    ProfileServiceItemTagDto(ServiceItemTag serviceItemTag) {
        this.serviceItemTag = serviceItemTag
    }

    @Override
    JSONObject asJSON() {
        if (!serviceItemTag) return null

        new JSONObject([id         : serviceItemTag.id,
                        tag        : [id   : serviceItemTag.tag.id,
                                      title: serviceItemTag.tag.title],
                        serviceItem: [id   : serviceItemTag.serviceItem.id,
                                      title: serviceItemTag.serviceItem.title]])
    }
}
