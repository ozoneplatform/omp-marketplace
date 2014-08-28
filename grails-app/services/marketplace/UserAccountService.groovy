package marketplace

class UserAccountService extends OzoneService {

    boolean transactional = false

    def findByUsername(def username) {
        return UserAccount.findByUsername(username, [cache: false])
    }

    def get(def params) {
        return UserAccount.get(params.id)
    }

    def list(def params) {
        return UserAccount.list(params)
    }

    def total() {
        return UserAccount.count()
    }
}