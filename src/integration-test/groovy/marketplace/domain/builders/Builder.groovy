package marketplace.domain.builders

import org.grails.datastore.gorm.GormEntity


interface Builder<T extends GormEntity<T>> {

    T build()

}
