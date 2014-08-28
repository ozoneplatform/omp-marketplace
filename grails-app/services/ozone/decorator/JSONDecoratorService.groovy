package ozone.decorator

import org.codehaus.groovy.grails.web.json.JSONObject

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class JSONDecoratorService implements ApplicationContextAware {

    ApplicationContext applicationContext

    static transactional = false

    void postProcessJSON(JSONObject json) {
        def interceptor = getBean("mp_RESTInterceptorService")
        if (interceptor) {
            try {
                interceptor.decorateOutgoing(json)
            }
            catch (Exception ex) {
                log.info(ex, ex)
            }
        }
    }

    Map getJSONStatus(JSONObject json) {

        def mp_RESTInterceptorStatus = [continueProcessing: false, message: '']

        def interceptor = getBean("mp_RESTInterceptorService")

        if (interceptor) {
            try {
                mp_RESTInterceptorStatus = interceptor.processIncoming(json)
            }
            catch (Exception ex) {
                log.error(ex, ex)
                mp_RESTInterceptorStatus.continueProcessing = false
                mp_RESTInterceptorStatus.message = ex.getMessage()
            }
        } else {
            mp_RESTInterceptorStatus.continueProcessing = true
            mp_RESTInterceptorStatus.message = "No interceptor found"
        }

        return mp_RESTInterceptorStatus
    }

    void preProcessJSON(JSONObject json) {
        Map mp_RESTInterceptorStatus = getJSONStatus(json)
        if (!(mp_RESTInterceptorStatus.continueProcessing)) {
            throw new JSONDecoratorException(mp_RESTInterceptorStatus.message)
        }
    }

    def getBean(String beanName) {
        if (applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName)
        }
        return null
    }

}
