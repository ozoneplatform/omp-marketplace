define([
    'timeout/TimeoutWatcher',
    'marketplace'
], function(TimeoutWatcher, Marketplace) {
    'use strict';

    return new TimeoutWatcher(
        Marketplace.sessionTimeoutConfig.maxInactiveInterval,
        Marketplace.sessionTimeoutConfig.settingSessionDataErrorMsg,
        Marketplace.sessionTimeoutConfig.redirectUrl
    );
});
