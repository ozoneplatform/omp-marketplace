/**
 * Helper functions related to using the bootstrap javascript plugins
 */

BootstrapUtil = {
    /**
     * Attach a bootstrap tooltip to the specified Ext component or jquery collection
     * @param $el A jquery collection to either attach the tooltip to, or to apply the selector to
     * @param options bootstrap tooltip option. See: http://getbootstrap.com/2.3.2/javascript.html#tooltips
     */
    attachTooltip: function($el, options) {
        var $ = jQuery;
        
        options = _.extend({
            placement: 'left',
            trigger: 'hover',
            delay: 675
        }, options);

        //ensure that the container has the class necessary to use bootstrap styles
        if (options.container) $(options.container).addClass('bootstrap-active');

        //activate bootstrap's tooltip plugin
        $el.tooltip(options);

        //HACK to address Bootstrap bug 7326 - left tooltips are not
        //positioned correctly
        $el.on('tbs.shown', function() {
            var tooltip = $el.data('tooltip'),
                $tip = tooltip.$tip,
                coords = $tip.offset();

            if(tooltip.options.placement === 'left') {
                //5px to address the bootstrap bug, and an additional
                //2px to get the exact position that we want in regards to
                //the parent element's border
                coords.left -= 7;

                $tip.offset(coords);
            }
        });
    }
}

//this file can be included in both old pages and new
if (typeof window.define === 'function') {
    //make BootstrapUtil available as a requirejs module
    define(function() {
        return BootstrapUtil;
    });
}
