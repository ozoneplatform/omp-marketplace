;(function ($, undefined) {
    
    var isIE7 = (document.documentMode === 7),
    	getComputedStyle = function (el) {
            this.el = el;
            this.getPropertyValue = function (prop) {
                var re = /(\-([a-z]){1})/g;
                if(prop == 'float') {
                     prop = 'styleFloat';
                }
                if(re.test(prop)) {
                    prop = prop.replace(re, function (a, b) {
                        return b.toUpperCase();
                    });
                }
                return (this.el.currentStyle[prop]) ? this.el.currentStyle[prop] : null;
            };
            return this;
        };


    function BorderBox ($el) {
        this.$el = $el;
        this.styles = getComputedStyle(this.$el[0]);

        this.initialize();
    }

    BorderBox.prototype = {

        initialize: function () {
            this.reset();
            this._setWidth();
            this._setHeight();
        },

        update: function () {
            this.initialize();
        },

        _setWidth: function () {
            var width = this.getPropertyValue('width'),
                contentWidth = this.$el.width(),
                outerWidth = this.$el.outerWidth(),
                diff = outerWidth - contentWidth;

            if(width === 'auto' || contentWidth <= 0 || diff === 0) {
                return;
            }
            this.$el.width(contentWidth - diff);
        },

        _setHeight: function () {
            var height = this.getPropertyValue('height'),
                contentHeight = this.$el.height(),
                outerHeight = this.$el.outerHeight(),
                diff = outerHeight - contentHeight;

            if(height === 'auto' || contentHeight <= 0 || diff === 0) {
                return;
            }
            this.$el.height(contentHeight - diff);
        },

        getPropertyValue: function (prop) {
            return this.styles.getPropertyValue(prop);
        },

        reset: function (prop) {
            this.$el.css({
                width: '',
                height: ''
            });
        }
    };

    $.fn.borderbox = function (applyToEntireTree) {
        if(isIE7) {
            var $els = applyToEntireTree === true ? this.find('*').andSelf() : this;
            $els.each(function (i, el) {
                var $el = $(el),
                    bb = $el.data('__borderbox');

                if(bb) {
                    bb.update();
                }
                else if($el.css('box-sizing') === 'border-box' || $el.css('display') != 'inline') {
                    $el.data('__borderbox', new BorderBox($el));
                }
            });
        }
        return this;   
    };
    
})(jQuery);