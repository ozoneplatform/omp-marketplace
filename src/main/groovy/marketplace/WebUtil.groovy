package marketplace

import grails.web.context.ServletContextHolder
import org.grails.web.util.GrailsApplicationAttributes

import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder as RCH

import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*


class WebUtil {

    private static final AJAX_REQUESTS = [
    	'/affiliatedMarketplace/listAsJSON',
    	'/affiliatedMarketplace/save',
    	'/relationship/getOWFRequiredItems',
    	'/itemComment'
    ]
	private static final RESOURCE_REQUESTS = ['/images/', 'config.js']
	private static final Logger log = Logger.getLogger(this)
	def static showAccessAlert = false;
	def static accessAlertMsg = "";

	// TODO: Fix this horrible mess; why are we using a Map?
	static boolean isAccessAlertEnabled(Map<String, String> optionsConfig){
		def session = RCH?.getRequestAttributes()?.getSession()
		session.showAccessAlert =  showAccessAlert
		if(Boolean.valueOf(optionsConfig?.get("checkAndSetParams_accessAlertShown"))){
			if((optionsConfig?.get("params_accessAlertShown") != null)
				&& (!(optionsConfig?.get("params_accessAlertShown").toString().equalsIgnoreCase("null")))){
				session.accessAlertShown = Boolean.valueOf(optionsConfig?.get("params_accessAlertShown"))
			}
		}

		return((!Boolean.valueOf(session.accessAlertShown))
				&& (!Boolean.valueOf(optionsConfig?.get("params_accessAlertShown")))
				&& (Boolean.valueOf(session.showAccessAlert)));
	}

	/****
	 * Determines if showing the access alert is required (by config)
	 * and if it's needed (by the fact that it has not been shown yet)
	 * @return
	 */
	static boolean isShowAccessAlertRequiredAndNeeded(){
		return shouldDisplayAcessAlertForRequest() && isAccessAlertEnabled()
	}

	static boolean shouldDisplayAcessAlertForRequest() {
		String requestForwardServletPath = getRequestForwardServletPath()
		boolean isSpecialRequest = isRESTRequest(requestForwardServletPath) ||
								   isResourceRequest(requestForwardServletPath) ||
								   isAjaxRequest(requestForwardServletPath) ||
                                   isBannerRequest(requestForwardServletPath)
		return !isSpecialRequest
	}

	/****
	 * Is the HTTP a REST call (i.e. <protocol>://<host>:<port>/<contextPath>/<public>/...)
	 * @return
	 */
	static boolean isRESTRequest(String pathToTest) {
		pathToTest?.startsWith("/public/") || pathToTest?.startsWith("/api/")
	}

    static boolean isBannerRequest(String pathToTest) {
        def result = false
		return false
        def grailsAppContext = (ApplicationContext)ServletContextHolder.getServletContext().getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);

        def appConfigService = grailsAppContext.getBean("marketplaceApplicationConfigurationService")
        def urlList = []

        def customHeaderUrl = appConfigService.getApplicationConfiguration(CUSTOM_HEADER_URL)?.value
        if(customHeaderUrl?.size() > 0)
            urlList << customHeaderUrl

        def customFooterUrl = appConfigService.getApplicationConfiguration(CUSTOM_FOOTER_URL)?.value
        if(customFooterUrl?.size() > 0)
            urlList << customFooterUrl

        def customCssImports = appConfigService.getApplicationConfiguration(CUSTOM_CSS_IMPORTS)?.value?.split(",")
        customCssImports?.each { url ->
            if(url.size() > 0)
                urlList << url
        }

        def customJsImports = appConfigService.getApplicationConfiguration(CUSTOM_JS_IMPORTS)?.value?.split(",")
        customJsImports?.each { url ->
            if(url.size() > 0)
                urlList << url
        }

        urlList.each { url ->
            if(url.contains(pathToTest)) {
                result = true
            }
        }

        return result
    }

	static boolean isRESTRequest() {
		String requestForwardServletPath = getRequestForwardServletPath()
		isRESTRequest(requestForwardServletPath)
	}

	/****
	* Is this an AJAX request
	* @return true if it is
	*/
	static boolean isAjaxRequest(String pathToTest){
		def ajaxRequest = AJAX_REQUESTS.find {
			pathToTest?.contains(it)
		}
		ajaxRequest != null
	}

	/****
	* Is this an image request
	* @return true if it is
	*/
	static boolean isResourceRequest(String pathToTest){
		def resourceRequest = RESOURCE_REQUESTS.find {
			pathToTest?.contains(it)
		}
		resourceRequest != null
	}

	static String getRequestForwardServletPath() {
		def session = RCH?.getRequestAttributes()?.getSession()
		def fullRequest =  RCH?.getRequestAttributes()?.getRequest()
		def fullRequestURL = fullRequest.getRequestURL()
		return fullRequest.getAttribute("javax.servlet.forward.servlet_path")
	}

	/****
	* Is the HTTP a REST call (i.e. <protocol>://<host>:<port>/<contextPath>/<public>/...)
	* @return
	*/
   static boolean isResourceRequest(){
	   def session = RCH?.getRequestAttributes()?.getSession()
	   def fullRequest =  RCH?.getRequestAttributes()?.getRequest()
	   def fullRequestURL = fullRequest.getRequestURL()
	   def javaxServletForwardServletPathAttr = fullRequest.getAttribute("javax.servlet.forward.servlet_path")
	   def pathToTest = new StringBuffer(""+javaxServletForwardServletPathAttr).toString()
	   if(pathToTest.startsWith("/public/") || pathToTest.startsWith("/images/")){
			   log.debug "[YES!] CALL FROM REST URL: ${fullRequestURL} : ATTRIBUTE[javax.servlet.forward.servlet_path]=${javaxServletForwardServletPathAttr}"
			   return true
	   }else{
			   log.debug "[NO!] CALL NOT FROM REST URL: ${fullRequestURL} : ATTRIBUTE[javax.servlet.forward.servlet_path]=${javaxServletForwardServletPathAttr}"
			   return false
	   }
   }

	/****
	 * Prepare the Redirect to Show Access Alert, basically build up
	 * the requisite params for the action
	 * @return
	 */
	static boolean prepRedirectToShowAccessAlert(){
		def session = RCH?.getRequestAttributes()?.getSession()

        //TODO once it is determined that this variable isn't needed, remove code about it
		def springDefaultSavedRequest = null //session[DefaultSavedRequest.SPRING_SECURITY_SAVED_REQUEST_KEY]
		log.debug "Marketplace showAccessAlert: ${session.showAccessAlert}"
		session.accessAlertMsg = accessAlertMsg
		if(!springDefaultSavedRequest){
			def fullRequest =  RCH?.getRequestAttributes()?.getRequest()
			def fullRequestURL = fullRequest.getRequestURL()
			def parameterMap = fullRequest.getParameterMap()
			def parameterStringBuff = new StringBuffer("")
			parameterMap?.eachWithIndex{ it, idx -> parameterStringBuff << ((idx == 0) ? '?' : '&') + it.key + '=' + it.value[0] }
			String fullRequestURLString = fullRequestURL.toString()
			fullRequestURLString = fullRequestURLString.replace(".dispatch", "")
			fullRequestURLString = fullRequestURLString.replace("/grails/","/")
			String javaxServletForwardServletPathAttr = (new StringBuffer(""+fullRequest.getAttribute("javax.servlet.forward.servlet_path"))).toString()
			//Alignment and Finding missing information
			//look into the fullRequestURLString to find where the servlet_path starts
			List<String> servletPathItemNotInFullRequestURL = new ArrayList<String>()
			String matchRegionSection = javaxServletForwardServletPathAttr
			boolean isRegionMatch = false
			StringBuffer missingInformationBuff = new StringBuffer() //This information will be gathered in reverse order
			for(int idx = javaxServletForwardServletPathAttr.length(); idx > 0; idx--){
				matchRegionSection = matchRegionSection.substring(0, idx)
				isRegionMatch = fullRequestURLString.regionMatches((fullRequestURLString.length() - idx),
												   matchRegionSection,
												   0,
												   idx)
				if(isRegionMatch){
					//break out, done
					break
				}else{
					//Add missing information
					missingInformationBuff.append(matchRegionSection.charAt((idx - 1)))
				}
			}
			if(isRegionMatch){
				if(missingInformationBuff.length() == 1){
					fullRequestURLString = fullRequestURLString + missingInformationBuff.toString()
				}else if(missingInformationBuff.length() > 1){
					fullRequestURLString = fullRequestURLString + missingInformationBuff.reverse().toString()
				}
			}
			fullRequestURLString = fullRequestURLString + parameterStringBuff.toString()
			session.initalRequestURL = fullRequestURLString
		}else{
			def fullRequestURLString = springDefaultSavedRequest.getRequestURL()
			def requestQueryString = springDefaultSavedRequest.getQueryString()
			if(requestQueryString && StringUtils.isNotBlank(requestQueryString)){
				fullRequestURLString = fullRequestURLString + "?" + requestQueryString
			}
			session.initalRequestURL = fullRequestURLString
		}
		log.debug "Marketplace session.initalRequestURL: ${session.initalRequestURL}"
		session.allowShowAccessAlert = true
		//Warning: without this, a constant loop redirect will occur when Access Alert is enabled.
		session.redirectToShowAlertViewInitiated = true
	}

    /**
     * @return whether the passed-in URLs are in the same context for the purposes of
     * inside/outside listings
     */
	static boolean isSameDomain(URL a, URL b) {
		int aPort = (a.port == -1) ? a.defaultPort : a.port
		int bPort = (b.port == -1) ? b.defaultPort : b.port

		(a.protocol == b.protocol) && (a.host == b.host) && (aPort == bPort)
	}
}
