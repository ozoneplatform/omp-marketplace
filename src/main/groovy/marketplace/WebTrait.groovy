package marketplace

import groovy.transform.CompileStatic
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import org.grails.web.servlet.mvc.GrailsWebRequest

import org.springframework.web.context.request.RequestContextHolder


@CompileStatic
trait WebTrait {

    static GrailsWebRequest getRequestAttributes() {
        (GrailsWebRequest) RequestContextHolder.currentRequestAttributes()
    }

    static HttpServletRequest getRequest() {
        getRequestAttributes()?.getCurrentRequest()
    }

    static String getRequestURL() {
        getRequest()?.getRequestURL()?.toString()
    }

    static HttpSession getSession() {
        getRequestAttributes()?.getSession()
    }

    static String getRequestForwardServletPath() {
        getRequest()?.getAttribute("javax.servlet.forward.servlet_path")
    }

}
