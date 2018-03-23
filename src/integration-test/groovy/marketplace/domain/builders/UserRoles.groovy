package marketplace.domain.builders

import marketplace.Constants


enum UserRoles {

    USER(Constants.USER),
    ADMIN(Constants.ADMIN),
    EXTADMIN(Constants.EXTERNADMIN)

    final String value

    private UserRoles(String value) {
        this.value = value
    }

}
