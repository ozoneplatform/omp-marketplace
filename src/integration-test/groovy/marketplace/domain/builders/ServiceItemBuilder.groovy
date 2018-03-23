package marketplace.domain.builders

import marketplace.OwfProperties
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types


class ServiceItemBuilder implements Builder<ServiceItem> {

    static final ApprovalStatus IN_PROGRESS = ApprovalStatus.IN_PROGRESS
    static final ApprovalStatus PENDING = ApprovalStatus.PENDING
    static final ApprovalStatus APPROVED = ApprovalStatus.APPROVED
    static final ApprovalStatus REJECTED = ApprovalStatus.REJECTED

    private final DomainModel domainModel

    ServiceItemBuilder(DomainModel domainModel) {
        this.domainModel = domainModel
    }

    String uuid
    String title
    String description
    String launchUrl

    List<Profile> owners
    Types types
    State state
    ApprovalStatus approvalStatus
    OwfProperties owfProperties

    ServiceItem build() {
        if (!uuid) uuid = UUID.randomUUID().toString()
        if (!approvalStatus) approvalStatus = IN_PROGRESS
        if (!launchUrl) launchUrl = "https://"

        if (!types && domainModel.defaultType) {
            types = domainModel.defaultType
        }

        new ServiceItem([uuid          : uuid,
                         title         : title,
                         description   : description,
                         launchUrl     : launchUrl,
                         owners        : owners,
                         types         : types,
                         state         : state,
                         approvalStatus: approvalStatus.status,
                         owfProperties : owfProperties])
    }


    void setType(Types type) {
        assert type != null

        types = type
    }

    void setOwner(Profile profile) {
        assert profile != null

        owners = [profile]
    }

}
