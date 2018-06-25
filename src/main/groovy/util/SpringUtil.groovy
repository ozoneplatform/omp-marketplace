/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util

import org.springframework.context.ApplicationContext;
import grails.web.context.ServletContextHolder;
import org.grails.web.util.GrailsApplicationAttributes;

public class SpringUtil {

    public static ApplicationContext getCtx() {
        return getApplicationContext()
    }

    public static ApplicationContext getApplicationContext() {
        return (ApplicationContext) ServletContextHolder.getServletContext().getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
    }

    public static <T> T getBean(String beanName) {
        return (T) getApplicationContext().getBean(beanName)
    }

}

