package marketplace

import javax.persistence.Transient


trait ChangeLogging {

    @Transient
    private transient Map<String, Object> $changed

    Object getOldValue(String propertyName) {
        if ($changed == null) return null

        $changed.get(propertyName)
    }

    def <T> T getOldValue(String propertyName, Class<T> type) throws ClassCastException {
        if ($changed == null) return null

        type.cast($changed.get(propertyName))
    }

    void setOldValue(String propertyName, Object value) {
        getChangeLog().put(propertyName, value)
    }

    boolean isPropertyChanged(String propertyName) {
        if ($changed == null) return false

        getChangeLog().containsKey(propertyName)
    }

    void clearChangelog() {
        $changed = null
    }

    private Map<String, Object> getChangeLog() {
        if ($changed == null) {
            $changed = new HashMap<>()
        }
        $changed
    }

}
