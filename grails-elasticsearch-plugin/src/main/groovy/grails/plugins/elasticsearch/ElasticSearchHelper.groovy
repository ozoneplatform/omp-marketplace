package grails.plugins.elasticsearch

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import org.elasticsearch.client.Client

class ElasticSearchHelper {

    Client elasticSearchClient

    def <R> R withElasticSearch(@ClosureParams(value=SimpleType.class, options="org.elasticsearch.client.Client") Closure<R> callable) {
        callable.call(elasticSearchClient)
    }

}
