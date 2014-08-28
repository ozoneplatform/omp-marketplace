/**
 * @ignore
 */
var Ozone = Ozone ? Ozone : {};

/**
 * @ignore
 * @namespace
 */
Ozone.marketplace = Ozone.marketplace ? Ozone.marketplace : {};

Ozone.marketplace.AddWidgetClient = function (widgetEventingController) {
    if (Ozone.marketplace.AddWidgetClient.instance == null) {
        this.addWidgetChannelName = "_ADD_WIDGET_CHANNEL";
        this.addStackChannelName = "_ADD_STACK_CHANNEL";
        this.launchStackChannelName = "_LAUNCH_STACK_CHANNEL";
        this.widgetEventingController = widgetEventingController || Ozone.eventing.Widget.instance;
        this.version = Ozone.version.owfversion + Ozone.version.widgetLauncher;

        Ozone.marketplace.AddWidgetClient.instance = this;
    }

    return Ozone.marketplace.AddWidgetClient.instance;
};

Ozone.marketplace.AddWidgetClient.prototype = {

    addWidget:function (config, callback) {

        this.widgetEventingController.disableActivateWidget = true;

        //send message to launch a widget
        var jsonString = gadgets.json.stringify(config);

        gadgets.rpc.call('..', this.addWidgetChannelName, callback, this.widgetEventingController.getWidgetId(), jsonString);
    },

    addStack:function (config, callback) {

        this.widgetEventingController.disableActivateWidget = true;

        //send message to launch a widget
        var jsonString = gadgets.json.stringify(config);

        gadgets.rpc.call('..', this.addStackChannelName, callback, this.widgetEventingController.getWidgetId(), jsonString);
    },

    launchStack:function (config, callback) {

        this.widgetEventingController.disableActivateWidget = true;

        var jsonString = gadgets.json.stringify(config);

        gadgets.rpc.call('..', this.launchStackChannelName, callback, this.widgetEventingController.getWidgetId(), jsonString);
    }
};

Ozone.marketplace.AddWidgetClient.getInstance = function (widgetEventingController) {
    if (Ozone.marketplace.AddWidgetClient.instance == null) {
        Ozone.marketplace.AddWidgetClient.instance = new Ozone.marketplace.AddWidgetClient(widgetEventingController);
    }
    return Ozone.marketplace.AddWidgetClient.instance;
};
