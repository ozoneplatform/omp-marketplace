package marketplace.domain.builders

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormValidateable

import org.springframework.beans.factory.annotation.Autowired

import org.hibernate.SessionFactory

import marketplace.CustomFieldDefinition
import marketplace.Intent
import marketplace.IntentAction
import marketplace.IntentDataType
import marketplace.ItemComment
import marketplace.OwfProperties
import marketplace.Profile
import marketplace.Category
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types


trait DomainBuilderMixin {

    @Autowired
    SessionFactory sessionFactory

    private boolean flush = true
    private boolean failOnError = true

    private DomainModel model = new DomainModel()

    DomainModel get$domain() {
        model
    }

    Category $category(@DelegatesTo(value = CategoryBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        model.add(invokeBuilder(new CategoryBuilder(), closure))
    }

    Intent $intent(@DelegatesTo(value = IntentBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(new IntentBuilder(), closure)
    }

    IntentAction $intentAction(@DelegatesTo(value = IntentActionBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(new IntentActionBuilder(), closure)
    }

    IntentDataType $intentDataType(@DelegatesTo(value = IntentDataTypeBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(new IntentDataTypeBuilder(), closure)
    }

    ItemComment $itemComment(@DelegatesTo(value = ItemCommentBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def comment = invokeBuilder(new ItemCommentBuilder(), closure)

        if (comment.serviceItem != null) {
            comment.serviceItem.addToItemComments(comment)
            save comment.serviceItem
        }

        comment
    }

    OwfProperties $owfProperties(@DelegatesTo(value = OwfPropertiesBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        $owfProperties(null, closure)
    }

    OwfProperties $owfProperties(Map options, @DelegatesTo(value = OwfPropertiesBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(options, new OwfPropertiesBuilder(), closure)
    }

    ServiceItem $serviceItem(@DelegatesTo(value = ServiceItemBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        model.add(invokeBuilder(new ServiceItemBuilder(model), closure))
    }

    Types $type(@DelegatesTo(value = TypeBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        model.add(invokeBuilder(new TypeBuilder(), closure))
    }

    Types $default(Types type) {
        model.defaultType = type
        type
    }

    State $state(@DelegatesTo(value = StateBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(new StateBuilder(), closure)
    }

    CustomFieldDefinition $fieldDefinition(@DelegatesTo(value = FieldDefinitionBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        invokeBuilder(new FieldDefinitionBuilder(), closure)
    }

    Profile $userProfile(@DelegatesTo(value = ProfileBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def builder = new ProfileBuilder(userRoles: UserRoles.USER)

        model.add(invokeBuilder(builder, closure))
    }

    Profile $adminProfile(@DelegatesTo(value = ProfileBuilder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def builder = new ProfileBuilder(userRoles: UserRoles.ADMIN)

        model.add(invokeBuilder(builder, closure))
    }

    void flushSession() {
        sessionFactory.currentSession.flush()
    }

    def <T extends GormEntity<T>> T save(T entity) {
        entity.save(flush: true, failOnError: true)
    }

    boolean validateAndPrintErrors(GormValidateable object) {
        def isValid = object.validate()
        if (!isValid) {
            object.errors.allErrors.each { println it }
        }
        isValid
    }

    Date now(Long offset = 0) {
        new Date(System.currentTimeMillis() + offset)
    }

    void sleep(Long milliseconds) {
        try {
            Thread.currentThread().sleep(milliseconds)
        } catch (Exception ignored) {}
    }

    private <T extends GormEntity<T>> T invokeBuilder(Builder<T> builder, Closure closure) {
        invokeBuilder(null, builder, closure)
    }

    private <T extends GormEntity<T>> T invokeBuilder(Map options, Builder<T> builder, Closure closure) {
        invokeDelegateFirst(builder, closure)

        def entity = builder.build()

        if (options.getBoolean('save', true)) {
            entity.save(flush: options.getBoolean('flush', true),
                        failOnError: options.getBoolean('failOnError', true))
        }

        entity
    }

    private void invokeDelegateFirst(Object delegate, Closure closure) {
        closure.delegate = delegate
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

}
