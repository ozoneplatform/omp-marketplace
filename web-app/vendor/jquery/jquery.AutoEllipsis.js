/// <reference path="jquery-1.3.2-vsdoc.js"/>
/*
	by Homam Hosseini
	http://abstractform.wordpress.com
	bluesnowball@gmail.com

*/


jQuery.fn.autoEllipsis = function(options) {
    var get_AutoEllipsisScroller = function(id) {
        var aeScrollerId = "WingooliAutoEllipsisScroller_" + id
        if (!document.getElementById(aeScrollerId)) {
            var div = document.createElement("div");
            div.id = aeScrollerId + "_Container";
            div.innerHTML = "<span id=\"" + aeScrollerId + "\" style=\"overflow: visible; position: absolute; top: -2000px; color: orange\"></span>";
            document.body.appendChild(div);
        }
        return document.getElementById(aeScrollerId);
    };

    var StringEllipsesByMaxLetters = function(element, originalText, maxLettersAllowed) {
      //element.title = "";
        var text = originalText;
        
        if (text == null || text == "") text = jQuery(element).text();
        //alert("Text length = " + text.length + ", max = " + maxLettersAllowed + ", TEXT = " + text);
        
        var escapeText = function(currText){
        	return ((currText == undefined) ? currText : String(currText).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;"));//Handle Escaping HTML...
        };
        var maxAllowedLatterIndex = text.length - maxLettersAllowed;
        if (maxAllowedLatterIndex > 0) {
            text = text.substr(0, maxLettersAllowed-3);
            element.innerHTML = "<span>" + escapeText(text) + "&hellip;</span>";
           
        } else {
          
          	element.innerHTML = "<span>" + escapeText(text) + "</span>" ;
           
        }
    };

    var _this = this;

    var settings = jQuery.extend({}, options);
    this.each(function(i) {
        sthis = jQuery(this);
        sthis.css("display","inline-block");
        var ellipsisMaxLettersAllowed = ((settings == undefined) ? -1 : ((settings.forceMaxLettersAllowed == undefined) ? -1 : settings.forceMaxLettersAllowed));
        var origText = sthis.text();
        var element = this;
        
	    if(ellipsisMaxLettersAllowed == -1){
	    	var aeScroller = get_AutoEllipsisScroller(i);
        	saeScroller = jQuery(aeScroller);
        	saeScroller.text(sthis.text());
        
	    	var elementBounds = { width: element.offsetWidth, height: element.offsetHeight };
	
	        var jAeScroller = jQuery(aeScroller);
	        var jElement = jQuery(element);
	        
	        var props = ["font-size", "font-weight", "font-family", "font-style", "padding"];
	
	        for (var i = 0; i < props.length; i++) {
	            try {
	                jAeScroller.css(props[i], jElement.css(props[i]));
	            } catch (ex) { }
	        }
	        jElement.css("overflow", "visible");
	
	
	        jAeScroller.width(jElement.width());
	
	        var isIe = (document.all != undefined);
	        var scrollerWidth = jAeScroller.width();
	        var scrollerHeight = jAeScroller.height();
	        var fitText = saeScroller.text();
	
	        while (scrollerHeight > elementBounds.height && fitText != "") {
	            fitText = fitText.substr(0, fitText.length - 2);
	            var autoScrollerInnerHTML = fitText + "&hellip;";
	            saeScroller.html(autoScrollerInnerHTML);
	            scrollerHeight = jAeScroller.height();
	        }
	        if (fitText == "") {
	            fitText = origText;
	            saeScroller.html(fitText);
	            jElement.css("whiteSpace", "nowrap");
	            jAeScroller.width("");
	            var scrollerWidth = jAeScroller.width();
	        }
	
	        while (scrollerWidth > elementBounds.width && fitText != "") {
	            fitText = fitText.substr(0, fitText.length - 2);
	            var autoScrollerInnerHTML = fitText + "&hellip;";
	            saeScroller.html(autoScrollerInnerHTML);
	            scrollerWidth = jAeScroller.width();
	        }
	        var scrollerHeight = aeScroller.offsetHeight;
	        var r = (Math.ceil(elementBounds.height / scrollerHeight) - 1);
	        r += (r == 0) ? 1 : 0;
	        var maxLettersAllowed = fitText.length * r;
	        ellipsisMaxLettersAllowed = maxLettersAllowed + (r + 1);
        }
       
        StringEllipsesByMaxLetters(element, origText, ellipsisMaxLettersAllowed);
    });
};
