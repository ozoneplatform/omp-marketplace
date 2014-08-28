package ozone3;

import java.io.Serializable;

public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    private long log4jWatchTime = 30000;
    private String marketplaceLocation = null;
    private Boolean allowFileUpload = true;
    private String freeTextEntryWarningMessage = null;

    public long getLog4jWatchTime() {
        return log4jWatchTime;
    }

    public void setLog4jWatchTime(long log4jWatchTime) {
        this.log4jWatchTime = log4jWatchTime;
    }

    public String getMarketplaceLocation() {
        return marketplaceLocation;
    }

    public void setMarketplaceLocation(String marketplaceLocation) {
        this.marketplaceLocation = marketplaceLocation;
    }

    public boolean isAllowFileUpload() {
        return this.getAllowFileUpload();
    }

    public Boolean getAllowFileUpload() {
        return this.allowFileUpload;
    }

    public void setAllowFileUpload(boolean allowFileUpload) {
        this.allowFileUpload = allowFileUpload;
    }

    public String getFreeTextEntryWarningMessage() {
        return this.freeTextEntryWarningMessage;
    }

    public void setFreeTextEntryWarningMessage(String freeTextEntryWarningMessage) {
        this.freeTextEntryWarningMessage = freeTextEntryWarningMessage;
    }
}
