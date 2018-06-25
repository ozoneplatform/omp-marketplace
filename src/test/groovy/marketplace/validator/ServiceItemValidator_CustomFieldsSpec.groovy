package marketplace.validator

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import grails.testing.gorm.DataTest
import grails.testing.spring.AutowiredTest
import grails.web.databinding.DataBinder

import marketplace.AccountService
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomField
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import marketplace.ServiceItem
import marketplace.data.CustomFieldDefinitionDataService
import marketplace.rest.AuditableDataBindingListener
import marketplace.rest.ServiceItemDataBindingListener

import static java.util.Collections.emptySet
import static java.util.Collections.singleton


class ServiceItemValidator_CustomFieldsSpec
        extends Specification
        implements AutowiredTest, DataTest, DataBinder
{

    Closure doWithSpring() {
        { ->
            auditableDataBindingListener(AuditableDataBindingListener)
            serviceItemDataBindingListener(ServiceItemDataBindingListener)
        }
    }

    AccountService accountService = Mock()
    CustomFieldDefinitionDataService dataService = Mock()

    ServiceItemValidator validator

    Set<CustomFieldDefinition> $findAllRequiredByType = emptySet()

    @Shared FieldValue enabledValue

    @Shared FieldValue disabledValue

    @Shared CustomFieldDefinition dropdownDef

    @Shared CustomFieldDefinition requiredDef

    @Shared CustomFieldDefinition nonRequiredDef

    def setup() {
        mockDomain ServiceItem
        mockDomain CustomFieldDefinition
        mockDomain DropDownCustomFieldDefinition
        mockDomain DropDownCustomField
        mockDomain FieldValue

        // Data
        enabledValue = new FieldValue(displayText: "enabled")
        disabledValue = new FieldValue(displayText: "disabled", isEnabled: 0)

        dropdownDef = new DropDownCustomFieldDefinition(name: "Dropdown Field",
                                                        fieldValues: [enabledValue, disabledValue])

        requiredDef = new CustomFieldDefinition(isRequired: true, name: 'required', uuid: '1')
        nonRequiredDef = new CustomFieldDefinition(name: 'nonRequired', uuid: '2')

        // Mocks
        accountService.checkAdmin(*_) >> { List args -> return }

        dataService.findAllRequiredByType(*_) >> { List args -> $findAllRequiredByType }

        // Validator
        validator = new ServiceItemValidator()
        validator.accountService = accountService
        validator.customFieldDefinitionDataService = dataService
    }

    private void validateChanges(ServiceItem original, Map<String, ?> changes) {
        bindData(original, changes)
        validator.validateChanges(original)
    }

    @Unroll
    def "validateNew(): disabled fields, #desc is valid"(String desc, Closure selection) {
        when:
        validator.validateNew(new ServiceItem(customFields: selection()))

        then:
        notThrown(IllegalArgumentException)

        where:
        desc      | selection
        "empty"   | this.&emptySelection
        "enabled" | this.&enabledSelection
    }

    @Unroll
    def "validateNew(): disabled fields, #desc is invalid"(String desc, Closure selection) {
        when:
        validator.validateNew(new ServiceItem(customFields: selection()))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message =~ "Value <[^>]+> for Custom Field <[^>]+> is disabled"

        where:
        desc       | selection
        "disabled" | this.&disabledSelection
        "both"     | this.&bothSelection
    }

    @Unroll
    void "validateChanges(): disabled fields, #desc is valid"(String desc,
                                                              Closure oldFields,
                                                              Closure newFields)
    {
        given:
        def serviceItem = new ServiceItem(customFields: oldFields()).save(validate: false)

        when:
        validateChanges(serviceItem, [customFields: newFields()])

        then:
        notThrown(IllegalArgumentException)

        where:
        desc                   | oldFields               | newFields
        "empty -> empty"       | this.&emptySelection    | this.&emptySelection
        "empty -> enabled"     | this.&emptySelection    | this.&enabledSelection
        "enabled -> empty"     | this.&enabledSelection  | this.&emptySelection
        "enabled -> enabled"   | this.&enabledSelection  | this.&enabledSelection
        "disabled -> empty"    | this.&disabledSelection | this.&emptySelection
        "disabled -> enabled"  | this.&disabledSelection | this.&enabledSelection
        "disabled -> disabled" | this.&disabledSelection | this.&disabledSelection
        "disabled -> both"     | this.&disabledSelection | this.&bothSelection
        "both -> empty"        | this.&bothSelection     | this.&emptySelection
        "both -> enabled"      | this.&bothSelection     | this.&enabledSelection
        "both -> disabled"     | this.&bothSelection     | this.&disabledSelection
        "both -> both"         | this.&bothSelection     | this.&bothSelection
    }

    @Unroll
    void "validateChanges(): disabled fields, #desc is invalid"(String desc,
                                                                Closure oldFields,
                                                                Closure newFields)
    {
        given:
        def serviceItem = new ServiceItem(customFields: oldFields()).save(validate: false)

        when:
        validateChanges(serviceItem, [customFields: newFields()])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message =~ "Value <[^>]+> for Custom Field <[^>]+> is disabled"

        where:
        desc                  | oldFields              | newFields
        "empty -> disabled"   | this.&emptySelection   | this.&disabledSelection
        "empty -> both"       | this.&emptySelection   | this.&bothSelection
        "enabled -> disabled" | this.&enabledSelection | this.&disabledSelection
        "enabled -> both"     | this.&enabledSelection | this.&bothSelection
    }

    @Unroll
    void "validateNew(): required fields, #desc is valid"(String desc,
                                                          Closure fields)
    {
        given:
        $findAllRequiredByType = singleton(requiredDef)

        when:
        validator.validateNew(new ServiceItem(customFields: fields()))

        then:
        notThrown(IllegalArgumentException)

        where:
        desc   | fields
        "only" | this.&onlyRequired
        "both" | this.&bothRequired
    }

    @Unroll
    def "validateNew(): required fields, #desc is invalid"(String desc,
                                                           Closure fields)
    {
        given:
        $findAllRequiredByType = singleton(requiredDef)

        when:
        validator.validateNew(new ServiceItem(customFields: fields()))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Missing the following required Custom Fields: required"

        where:
        desc      | fields
        "none"    | this.&noFields
        "without" | this.&withoutRequired
    }

    @Unroll
    void "validateChanges(): required fields, #desc is valid"(String desc,
                                                              Closure oldFields,
                                                              Closure newFields)
    {
        given:
        $findAllRequiredByType = singleton(requiredDef)

        def serviceItem = new ServiceItem(customFields: oldFields()).save(validate: false)

        when:
        validateChanges(serviceItem, [customFields: newFields()])

        then:
        notThrown(IllegalArgumentException)

        where:
        desc                 | oldFields             | newFields
        "none -> none"       | this.&noFields        | this.&noFields
        "none -> without"    | this.&noFields        | this.&withoutRequired
        "none -> only"       | this.&noFields        | this.&onlyRequired
        "none -> both"       | this.&noFields        | this.&bothRequired

        "only -> only"       | this.&onlyRequired    | this.&onlyRequired
        "only -> both"       | this.&onlyRequired    | this.&bothRequired

        "without -> none"    | this.&withoutRequired | this.&noFields
        "without -> without" | this.&withoutRequired | this.&withoutRequired
        "without -> only"    | this.&withoutRequired | this.&onlyRequired
        "without -> both"    | this.&withoutRequired | this.&bothRequired

        "both -> only"       | this.&bothRequired    | this.&onlyRequired
        "both -> both"       | this.&bothRequired    | this.&bothRequired
    }

    @Unroll
    def "validateChanges(): required fields, #desc is invalid"(String desc,
                                                               Closure oldFields,
                                                               Closure newFields)
    {
        given:
        $findAllRequiredByType = singleton(requiredDef)

        def serviceItem = new ServiceItem(customFields: oldFields()).save(validate: false)

        when:
        validateChanges(serviceItem, [customFields: newFields()])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Missing the following required Custom Fields: required"

        where:
        desc              | oldFields          | newFields
        "only -> none"    | this.&onlyRequired | this.&noFields
        "only -> without" | this.&onlyRequired | this.&withoutRequired
        
        "both -> none"    | this.&bothRequired | this.&noFields
        "both -> without" | this.&bothRequired | this.&withoutRequired
    }

    CustomField makeDropDownCustomField(Map params = [:]) {
        new DropDownCustomField([customFieldDefinition: dropdownDef] + params)
    }

    List<CustomField> emptySelection() {
        [makeDropDownCustomField()]
    }

    List<CustomField> enabledSelection() {
        [makeDropDownCustomField(value: enabledValue)]
    }

    List<CustomField> disabledSelection() {
        [makeDropDownCustomField(value: disabledValue)]
    }

    List<CustomField> bothSelection() {
        [makeDropDownCustomField(fieldValueList: [enabledValue, disabledValue])]
    }

    CustomField makeCustomField(CustomFieldDefinition definition) {
        new CustomField(customFieldDefinition: definition)
    }

    List<CustomField> onlyRequired() {
        [makeCustomField(requiredDef)]
    }

    List<CustomField> withoutRequired() {
        [makeCustomField(nonRequiredDef)]
    }

    List<CustomField> bothRequired() {
        [makeCustomField(requiredDef), makeCustomField(nonRequiredDef)]
    }

    List<CustomField> noFields() {
        []
    }

}
