package marketplace

import grails.gorm.transactions.Transactional


class InitializationService {

    ProfileService profileService

    StateService stateService

    TypesService typesService

    RejectionJustificationService rejectionJustificationService

    OwfWidgetTypesService owfWidgetTypesService

    IntentActionService intentActionService

    IntentDataTypeService intentDataTypeService

    IntentDirectionService intentDirectionService

    @Transactional
    void createRequired() {
        profileService.createRequired()
        stateService.createRequired()
        typesService.createRequired()
        rejectionJustificationService.createRequired()
        owfWidgetTypesService.createRequired()
        intentActionService.createRequired()
        intentDataTypeService.createRequired()
        intentDirectionService.createRequired()
        CustomFieldUtility.createPreConfiguredCustomFields()
    }

}
