Number.prototype.round = function() {
  return Math.round(this);
};

String.prototype.trim = function() {
    return jQuery.trim(this);
};

String.prototype.escapeHTML = function () {
    return this.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
};
String.prototype.escapeHtml = function() {
    return this.replace(/&quot;/g, "\"")
            .replace(/&amp;/g, "&")
            .replace(/&gt;/g, ">")
            .replace(/&lt;/g, "<");
};
String.prototype.mpEscapeHtml = function() {
    return this.replace(/\?/g,"%3F").replace(/&/g,"%26")
        .replace(/@/g,"%40")
        .replace(/\//g,"&#47;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/\n/g,"<br>")
        .replace(/  /g,"&nbsp; ");
};

String.prototype.cleanEscapeHTML = function(cleanOptions) {
    var thisString = this.trim().escapeHTML().replace(/'/g, "&#39;").replace(/"/g,"&quot;");//USES PROTOTYPE LIB'S ESCAPEHTML!
    if(cleanOptions && cleanOptions.removeHtmlSpaces){
        thisString = thisString.replace(/&amp;nbsp;/ig, " ");
    }
    if(cleanOptions && cleanOptions.removeHtmlBreaks){
        thisString = thisString.replace(/&lt;br&gt;/ig, "\n").replace(/&lt;br\/&gt;/ig, "\n");
    }
    return thisString;
};

String.prototype.cleanUnescapeHTML = function(cleanOptions) {
    var thisString = this.trim();
    if(cleanOptions && cleanOptions.removeHtmlSpaces){
        thisString = thisString.replace(/&nbsp;/ig, " ");
    }
    if(cleanOptions && cleanOptions.removeHtmlBreaks){
        thisString = thisString.replace(/<br>/ig, "\n").replace(/<br\/>/ig, "\n");
    }
    thisString = thisString.unescapeHTML();//USES PROTOTYPE LIB'S UNESCAPEHTML!

    return thisString;
};

String.prototype.stripTags = function stripTags(){
    return this.replace(/<\w+(\s+("[^"]*"|'[^']*'|[^>])+)?>|<\/\w+>/gi,"");
};

String.prototype.unescapeHTML = function () {
    return this.stripTags().replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g,"&");
};

String.prototype.requiredLabel= function()
{
    return "<span class='required-label'>" + this + " <span class='required-indicator'>*</span></span>";
};

// function for converting an ISO8601 date string (e.g. "2011-10-28T15:54:00Z") into a Date
Date.prototype.setISO8601 = function (string) {
    var regexp = "([0-9]{4})(-([0-9]{2})(-([0-9]{2})" +
        "(T([0-9]{2}):([0-9]{2})(:([0-9]{2})(\.([0-9]+))?)?" +
        "(Z|(([-+])([0-9]{2}):([0-9]{2})))?)?)?)?";
    var d = string.match(new RegExp(regexp));

    var offset = 0;
    var date = new Date(d[1], 0, 1);

    if (d[3]) { date.setMonth(d[3] - 1); }
    if (d[5]) { date.setDate(d[5]); }
    if (d[7]) { date.setHours(d[7]); }
    if (d[8]) { date.setMinutes(d[8]); }
    if (d[10]) { date.setSeconds(d[10]); }
    if (d[12]) { date.setMilliseconds(Number("0." + d[12]) * 1000); }
    if (d[14]) {
        offset = (Number(d[16]) * 60) + Number(d[17]);
        offset *= ((d[15] == '-') ? 1 : -1);
    }

    offset -= date.getTimezoneOffset();
    time = (Number(date) + (offset * 60 * 1000));
    this.setTime(Number(time));
};

//TODO this and other functions like this should be broke out to a true utility file
Marketplace.getTruncatedValue = function(val, textMaxLength, lineCount){
    if(!_.isString(val)) { return ''; }

    //First check the number of lines
    if(lineCount != undefined){
        var linesInVal = val.split("\n");
        //If the number of lines is greater than the line count we need to pair it down
        if(linesInVal.length > lineCount){
            var returnVal = "";
            for(var i = 0; i < lineCount ; i++){
                returnVal = returnVal + linesInVal[i] + "\n";
            }
            //Its possible this text length is greater than the allowed length so we need to continue processing
            val = returnVal;
        }
    }

    //At this point if the length of the text is < the length of the allowed text just return the inital val
    if(val.length <= textMaxLength)
        return val;

    //Otherwise truncate the string....
    return val.substring(0, textMaxLength)  + "...";
}

//Taken from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/indexOf
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (searchElement, fromIndex) {
      if ( this === undefined || this === null ) {
        throw new TypeError( '"this" is null or not defined' );
      }

      var length = this.length >>> 0; // Hack to convert object.length to a UInt32

      fromIndex = +fromIndex || 0;

      if (Math.abs(fromIndex) === Infinity) {
        fromIndex = 0;
      }

      if (fromIndex < 0) {
        fromIndex += length;
        if (fromIndex < 0) {
          fromIndex = 0;
        }
      }

      for (;fromIndex < length; fromIndex++) {
        if (this[fromIndex] === searchElement) {
          return fromIndex;
        }
      }

      return -1;
    };
  }
