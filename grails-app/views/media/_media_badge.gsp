<%
    def fileName = fileInfo[0]
    def fileDisplayName = fileName.replace('_',' ')
    def fileType = fileInfo[1]
    def fileLength = fileInfo[2]
    def fileIconSrc = fileName+'/'+fileName
    def mediaIconSrc = 'time_icon_small.png'

    def mediaBack = 'videoBack'
    def mediaFront = 'videoFront'

    if(fileType.toLowerCase() == "guide"){
        mediaBack = 'guideBack'
        mediaFront = 'guideFront'
        mediaIconSrc = 'page_icon_small.png'
        fileIconSrc = '../static/themes/common/images/media_page/doc_placeholder'
    }

    if(fileType.toLowerCase() == "slideshow"){
        mediaBack = 'slideshowBack'
        mediaFront = 'slideshowFront'
        mediaIconSrc = 'page_icon_small.png'
        fileIconSrc = '../static/themes/common/images/media_page/ppt_placeholder'
    }
%>
<div class="media_badge_container" style="height:150px; width:150px;" >

    <input type="hidden" value="${fileName}" id="nameData" name="nameData" />
    <input type="hidden" value="${fileInfo[3]}" id="descData" name="descData" />
    <input type="hidden" value="${fileLength}" id="lengthData" name="lengthData" />
    <input type="hidden" value="${fileType}" id="typeData" name="typeData" />
    <input type="hidden" value="${fileDisplayName}" id="displayNameData"
        name="displayNameData" />

    <div id="media_badge" class="${mediaBack}"><!---<image src="${fileIconSrc}.png" title="${fileName}" style="padding-top: 6px;"/>---></div>
    <div width="150px">
        <div class="${mediaFront}"></div>
        <div class="media_length"><p:image src="media_page/${mediaIconSrc}" class="media_type_icon"/> ${fileLength.encodeAsHTML()}</div>
        <div class="listing_short_title_text media_title" >${fileDisplayName.encodeAsHTML()}</div>
    </div>
</div>
