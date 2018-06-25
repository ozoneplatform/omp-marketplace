package marketplace.domain.builders

import marketplace.Profile


class ProfileBuilder implements Builder<Profile> {

    static final USER = UserRoles.USER
    static final ADMIN = UserRoles.ADMIN
    static final EXTADMIN = UserRoles.EXTADMIN

    String username
    String displayName
    String uuid
    UserRoles userRoles

    Profile build() {
        if (!uuid) uuid = UUID.randomUUID().toString()
        if (!userRoles) userRoles = USER

        new Profile(uuid: uuid,
                    username: username,
                    displayName: displayName ?: username,
                    userRoles: userRoles.value)
    }

    void setRole(UserRoles role) {
        userRoles = role
    }

}
