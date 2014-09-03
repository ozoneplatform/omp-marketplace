package marketplace

class ImportTaskResult implements Comparable {

    Date runDate = new Date()
    Boolean result
    String message

    public static final int MESSAGE_MAX_SIZE = 4000

    static belongsTo = [task: ImportTask]
    static constraints = {
        message(nullable: false, maxSize: MESSAGE_MAX_SIZE)
    }

    public int compareTo(obj) {
        if (!obj.runDate) {
            return -1
        }

        if (runDate.after(obj.runDate)) {
            return 1
        } else if (runDate.before(obj.runDate)) {
            return -1
        } else {
            return 0
        }
    }

    public int compareByMessage(obj) {
        return message.compareTo(obj.message)
    }

    public int compareByResult(obj) {
        if (!result && obj.result) {
            return -1
        } else if ((!result && !obj.result) || (result && obj.result)) {
            return 0
        } else {
            return 1
        }
    }

    String toString() {
        "ImportTaskResult[id: $id, runDate: $runDate, result: $result, message: $message]"
    }

    /**
     * handle truncation of message
     */
    public void setMessage(String message) {
        this.message = message.size() <= MESSAGE_MAX_SIZE ? message :
            message[0..MESSAGE_MAX_SIZE - 4] + '...'
    }
}
