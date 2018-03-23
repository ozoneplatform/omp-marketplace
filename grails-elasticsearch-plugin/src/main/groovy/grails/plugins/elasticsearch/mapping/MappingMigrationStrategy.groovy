package grails.plugins.elasticsearch.mapping

import groovy.transform.CompileStatic

/**
 * Created by @marcos-carceles on 22/12/14.
 */
@CompileStatic
enum MappingMigrationStrategy {
    none, delete, deleteIndex, alias
}