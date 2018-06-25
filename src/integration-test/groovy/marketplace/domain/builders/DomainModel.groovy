package marketplace.domain.builders

import groovy.transform.PackageScope

import marketplace.Category
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.Types


class DomainModel {

    Map<Long, ServiceItem> serviceItemsById = new LinkedHashMap<>()

    Map<Long, Category> categoriesById = new LinkedHashMap<>()

    Map<Long, ServiceItemActivity> serviceItemActivitiesById = new LinkedHashMap<>()

    Map<String, Profile> profilesByUsername = new LinkedHashMap<>()

    Map<String, Types> typesByTitle = new LinkedHashMap<>()

    Types defaultType

    List<Category> getCategories() {
        new ArrayList(categoriesById.values())
    }

    List<Profile> getProfiles() {
        new ArrayList(profilesByUsername.values())
    }

    List<Types> getTypes() {
        new ArrayList(typesByTitle.values())
    }

    List<ServiceItem> getServiceItems() {
        new ArrayList(serviceItemsById.values())
    }

    List<ServiceItemActivity> getServiceItemActivities() {
        new ArrayList(serviceItemActivitiesById.values())
    }

    @PackageScope Category add(Category category) {
        categoriesById.put(category.id, category)
        category
    }

    @PackageScope
    Profile add(Profile profile) {
        profilesByUsername.put(profile.username, profile)
        profile
    }

    @PackageScope
    Types add(Types type) {
        typesByTitle.put(type.title, type)
        type
    }

    @PackageScope
    ServiceItem add(ServiceItem serviceItem) {
        serviceItemsById.put(serviceItem.id, serviceItem)
        serviceItem
    }

    @PackageScope
    ServiceItemActivity add(ServiceItemActivity activity) {
        serviceItemActivitiesById.put(activity.id, activity)
        activity
    }

}
