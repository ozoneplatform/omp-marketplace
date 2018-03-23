/*
   Copyright 2013 Next Century Corporation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package marketplace.service

import grails.testing.mixin.integration.Integration

import marketplace.*
import marketplace.controller.MarketplaceIntegrationTestCase
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Rollback
import ozone.marketplace.dataexchange.ProfileImporter
import ozone.marketplace.dataexchange.ServiceItemImporter

@Integration
@Rollback
class ServiceItemImporterTests extends MarketplaceIntegrationTestCase {

    ServiceItemImporter service
    def serviceItemService
    def extServiceItemService
    def marketplaceApplicationConfigurationService
    def itemCommentService
    def profileImporter
    def accountService
    Types defaultType

    void setup() {
//        super.setUp()
        switchAdmin('testAdmin1')

        itemCommentService.SERVICE_ITEM_RULES.allNoRestrictions = true

        this.defaultType = new Types(title: "Default type", hasLaunchUrl: false)
        defaultType.save()
        profileImporter = new ProfileImporter()
        service = new ServiceItemImporter(profileImporter, true, "")
        service.profileImporter = profileImporter
        service.accountService = accountService
        service.serviceItemService = serviceItemService
        service.extServiceItemService = extServiceItemService
        service.itemCommentService = itemCommentService
        service.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
    }

    void tearDown() {
        itemCommentService.SERVICE_ITEM_RULES.allNoRestrictions = false
    }

    void testImportItemCommentsFromJSON() {
        when:
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        then:
        0 == ServiceItem.count()

        when:
        // Import service item with comments
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        1 == stats.serviceItems.created
        0 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        listingJSON.title == serviceItem.title
        listingJSON.approvalStatus == serviceItem.approvalStatus
        2 == serviceItem.getItemComments().size()
        2 == serviceItem.totalVotes
        1 == serviceItem.totalRate3
        1 == serviceItem.totalRate5
        0 == serviceItem.totalRate1
        4 == serviceItem.avgRate

        when:
        // Test importing item with duplicate comments - only the one with the later edited date should be updated
        stats = new ImportStatus()
        listingJSON = updateItemComments(listingJSON)
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        0 == stats.serviceItems.created
        1 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        2 == serviceItem.getItemComments().size()
        2 == serviceItem.totalVotes
        0 == serviceItem.totalRate3
        1 == serviceItem.totalRate5
        1 == serviceItem.totalRate2
        0 == serviceItem.totalRate1
        3.5 == serviceItem.avgRate
    }

    void testIgnoreItemCommentsFromJSON() {
        when:
        service.importRatings = false
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        1 == stats.serviceItems.created
        0 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        serviceItem.getItemComments().size() == 0
    }

    void testCreateListingWithRequiredCustomField() {
        when:
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        String listingFieldValue = "foo"
        addCustomFieldValue(listingJSON, textCFD, listingFieldValue)
        ImportStatus stats = new ImportStatus()
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        1 == stats.serviceItems.created
        0 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        null != serviceItem
        null != serviceItem.customFields
        1 == serviceItem.customFields.size()
        listingFieldValue == serviceItem.customFields[0].getFieldValueText()
    }

    void testCreateListingMissingRequiredCustomField() {
        when:
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        1 == stats.serviceItems.created
        0 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        null != serviceItem
    }

    void testCreateListingWithEmptyRequiredCustomField() {
        when:
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        addCustomFieldValue(listingJSON, textCFD, null)
        ImportStatus stats = new ImportStatus()
        service.importFromJSON(listingJSON, stats.serviceItems)
        then:
        0 == stats.serviceItems.failed
        1 == stats.serviceItems.created
        0 == stats.serviceItems.updated
        0 == stats.serviceItems.notUpdated
        1 == ServiceItem.count()

        when:
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        then:
        null != serviceItem
//        assert null != serviceItem.customFields
//        assert 1 == serviceItem.customFields.size()
//        assert listingFieldValue == serviceItem.customFields[0].getFieldValueText()
    }

    private JSONObject getListingJSON() {

        return new JSONObject([
                uuid: UUID.randomUUID().toString(),
                title: "The title",
                approvalStatus: "In Progress",
                types: this.defaultType.asJSON(),
                editedDate: "2025-01-31T01:00:00",
                itemComments: new JSONArray([
                        new JSONObject(
                                text: "Comment 1",
                                rate: 5,
                                editedDate: "2012-09-10T19:01:17Z",
                                author: new JSONObject(
                                        id: 1,
                                        username: "testUser1",
                                        displayName: "Test User 1"
                                )
                        ),
                        new JSONObject(
                                text: "Comment 2",
                                rate: 3,
                                editedDate: "2012-09-10T19:01:17Z",
                                author: new JSONObject(
                                        id: 1,
                                        username: "testAdmin1",
                                        displayName: "Test Administrator 1"
                                )
                        )
                ]),
                owners: new JSONArray([
                        new JSONObject(
                                id: 2,
                                username: "testAdmin1",
                                editedDate: "2012-09-10T19:01:17Z",
                                name: "Test Administrator 1"
                        )
                ])
        ])
    }

    private JSONObject updateItemComments(JSONObject listingJSON) {
        listingJSON.itemComments = new JSONArray([
                new JSONObject(
                        text: "Comment 11",
                        rate: 4,
                        editedDate: "2012-09-10T19:01:17Z",
                        author: new JSONObject(
                                id: 1,
                                username: "testUser1",
                                displayName: "Test User 1"
                        )
                ),
                new JSONObject(
                        text: "Comment 22",
                        rate: 2,
                        editedDate: "2099-09-10T19:01:17Z",
                        author: new JSONObject(
                                id: 1,
                                username: "testAdmin1",
                                displayName: "Test Administrator 1"
                        )
                )
        ])
        listingJSON
    }

    private void addCustomFieldValue(JSONObject listingJSON, CustomFieldDefinition cfd, String value) {
        final String customFieldKey = "customFields"
        if (!listingJSON.has(customFieldKey)) {
            listingJSON.put(customFieldKey, new JSONArray())
        }
        listingJSON.get(customFieldKey).add(new JSONObject([
                customFieldDefinitionUuid: cfd.uuid,
                customFieldDefinitionId: cfd.id,
                fieldType: cfd.styleType.name(),
                value: value
        ]))
    }
}
