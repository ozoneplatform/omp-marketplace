package marketplace

class StopWatch {
    def startTime
    def elapsedTime
    def tag

    def lapHigh = 0
    def lapLow = 0
    def lapAverage = 0
    def lapCount = 0
    def lapTotal = 0

    def showTimeInSeconds = false

    public StopWatch(def tagIn) {
        tag = tagIn
        start()
    }

    public void start() {
        startTime = System.currentTimeMillis();
        elapsedTime = -1L;
    }

    public String stop() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return this.toString();
    }

    String toString() {
        return "${tag} - ${elapsedTime} millis"
    }

    // withStopWatch(closure)

    public String lap(message) {
        stop();
        lapCount += 1
        lapTotal += elapsedTime
        if (lapLow == 0 || elapsedTime < lapLow) {
            lapLow = elapsedTime
        }
        if (elapsedTime > lapHigh) {
            lapHigh = elapsedTime
        }
        lapAverage = lapTotal / lapCount;

        if (message == null) {
            message = "lap ${lapCount}"
        }
        return "${tag}:${message} - ${elapsedTime} millis"
    }

    public String lapStats(message) {
        return "${tag}:${message} - low = ${lapLow} high = ${lapHigh} avg = ${lapAverage} count = ${lapCount} total = ${lapTotal} millis"
    }

    public String split(message) {
        def splitTime = System.currentTimeMillis() - startTime;

        if (message == null) {
            message = "split"
        }
        return "${tag}:${message} - ${splitTime} millis"
    }
}
