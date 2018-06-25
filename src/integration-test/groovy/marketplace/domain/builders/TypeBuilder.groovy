package marketplace.domain.builders

import marketplace.Types


class TypeBuilder implements Builder<Types> {

    String title
    String uuid
    Boolean hasLaunchUrl

    Types build() {
        if (!uuid) uuid = UUID.randomUUID().toString()
        if (hasLaunchUrl == null) hasLaunchUrl = true

        new Types([uuid        : uuid,
                   title       : title,
                   hasLaunchUrl: hasLaunchUrl])
    }

}
