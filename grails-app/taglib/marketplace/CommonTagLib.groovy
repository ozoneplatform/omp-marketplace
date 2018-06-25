package marketplace

import marketplace.configuration.MarketplaceApplicationConfigurationService

import ozone.marketplace.enums.MarketplaceApplicationSetting

class CommonTagLib {

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    static namespace = "common"

    def convertListToGrid = { attrs, body ->
        def var = attrs.var ?: "returnedLists"
        def grid = []

        int listSize = attrs.elementList.size()
        int numColumns = Integer.parseInt(attrs.listSize)
        int numRows = Math.ceil(listSize / numColumns)

        StopWatch stopWatch = new StopWatch("Convert List to Grid")

        for (int row = 0; row < numRows; row++) {
            def gridRow = []
            for (column in 0..numColumns - 1) {
                int elementIndex = column + numColumns * row
                if (elementIndex < listSize)
                    gridRow.add(attrs.elementList.get(elementIndex))
                else
                    break
            }
            grid.add(gridRow)
        }

        stopWatch.stop()
        log.debug "Ended Conversion.  Timing info: " + stopWatch

        out << body((var): grid)
    }

    def truncateText = { attrs, body ->

        def var = attrs.var ?: "text"

        def textToTruncate = attrs.text
        int truncateAt = Integer.parseInt(attrs.truncateAt)
        int lineCount = Integer.parseInt(attrs?.lineCount ?: '0')

        def returnText = ""

        if (textToTruncate) {
            returnText = Helper.shorten(textToTruncate, truncateAt, "...", lineCount)
            out << body((var): returnText)
        } else {
            out << body()
        }

    }

    def freeTextWarning = { attrs, body ->
        def freeTextWarningValue = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.FREE_WARNING_CONTENT)
        if (freeTextWarningValue) {
            out << "<div class='warning'>${freeTextWarningValue}</div>"
        }
    }

    def requiredLabel = { attrs, body ->
        out << "<span class='required-label'>${body()}<span class='required-indicator'> *</span></span>"
    }

    def avatarImg = { attrs, body ->
        def profile = attrs.profile
        def avatar = profile?.avatar
        def avatarUrl = avatar ?
            createLink(controller: 'avatar', action: 'pic', id: avatar.ident()) :
            request.contextPath + '/images/default/default_avatar.jpg'

        out << "<img src='${avatarUrl}' width='100' height='100'/>"
    }
}
