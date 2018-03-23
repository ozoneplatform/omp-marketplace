import grails.plugins.elasticsearch.ElasticSearchBootStrapHelper

class ElasticsearchBootStrap {

    ElasticSearchBootStrapHelper elasticSearchBootStrapHelper

    def init = { servletContext ->
        elasticSearchBootStrapHelper?.bulkIndexOnStartup()
    }
}
