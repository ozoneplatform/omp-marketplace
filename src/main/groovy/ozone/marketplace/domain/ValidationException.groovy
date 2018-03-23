package ozone.marketplace.domain


class ValidationException extends RuntimeException {

    String message
    def args = []
    def fieldErrors

    ValidationException() {
        super()
    }

    ValidationException(String message) {
        super(message)
    }

    public String getMessage() {
        return message ? fieldErrors ? "${message} : ${fieldErrors}" : "${message}" : "${fieldErrors}"
    }
}
