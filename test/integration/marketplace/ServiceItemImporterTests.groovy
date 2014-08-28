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
package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.ProfileImporter
import ozone.marketplace.dataexchange.ServiceItemImporter

@TestMixin(IntegrationTestMixin)
class ServiceItemImporterTests extends MarketplaceIntegrationTestCase {

    ServiceItemImporter serviceItemImporter
    def serviceItemService
    def extServiceItemService
    def marketplaceApplicationConfigurationService
    def itemCommentService
    def profileImporter
    Types defaultType

    void setUp() {
        super.setUp()
        switchAdmin('testAdmin1')

        itemCommentService.serviceItemRules.allNoRestrictions = true

        this.defaultType = new Types(title: "Default type", hasLaunchUrl: false)
        defaultType.save()
        profileImporter = new ProfileImporter()
        serviceItemImporter = new ServiceItemImporter(profileImporter, true, "")
        serviceItemImporter.accountService = accountService
        serviceItemImporter.serviceItemService = serviceItemService
        serviceItemImporter.extServiceItemService = extServiceItemService
        serviceItemImporter.itemCommentService = itemCommentService
        serviceItemImporter.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
    }

    void tearDown() {
        itemCommentService.serviceItemRules.allNoRestrictions = false
    }

    void testImportItemCommentsFromJSON() {
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        assert 0 == ServiceItem.count()

        // Import service item with comments
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 1 == stats.serviceItems.created
        assert 0 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated

        assert 1 == ServiceItem.count()
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert listingJSON.title == serviceItem.title
        assert listingJSON.approvalStatus == serviceItem.approvalStatus
        assert 2 == serviceItem.getItemComments().size()
        assert 2 == serviceItem.totalVotes
        assert 1 == serviceItem.totalRate3
        assert 1 == serviceItem.totalRate5
        assert 0 == serviceItem.totalRate1
        assert 4 == serviceItem.avgRate

        // Test importing item with duplicate comments - only the one with the later edited date should be updated
        stats = new ImportStatus()
        listingJSON = updateItemComments(listingJSON)
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 0 == stats.serviceItems.created
        assert 1 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated

        assert 1 == ServiceItem.count()
        serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert 2 == serviceItem.getItemComments().size()
        assert 2 == serviceItem.totalVotes
        assert 0 == serviceItem.totalRate3
        assert 1 == serviceItem.totalRate5
        assert 1 == serviceItem.totalRate2
        assert 0 == serviceItem.totalRate1
        assert 3.5 == serviceItem.avgRate
    }

    void testIgnoreItemCommentsFromJSON() {
        serviceItemImporter.importRatings = false
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 1 == stats.serviceItems.created
        assert 0 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated
        assert 1 == ServiceItem.count()
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert null == serviceItem.getItemComments()
    }

    void testCreateListingWithRequiredCustomField() {
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        String listingFieldValue = "foo"
        addCustomFieldValue(listingJSON, textCFD, listingFieldValue)
        ImportStatus stats = new ImportStatus()
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 1 == stats.serviceItems.created
        assert 0 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated
        assert 1 == ServiceItem.count()
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert null != serviceItem
        assert null != serviceItem.customFields
        assert 1 == serviceItem.customFields.size()
        assert listingFieldValue == serviceItem.customFields[0].getFieldValueText()
    }

    void testCreateListingMissingRequiredCustomField() {
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        ImportStatus stats = new ImportStatus()
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 1 == stats.serviceItems.created
        assert 0 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated
        assert 1 == ServiceItem.count()
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert null != serviceItem
    }

    void testCreateListingWithEmptyRequiredCustomField() {
        // A required custom field will be present but empty in the JSON if the listing was edited
        // while the custom field was present but not required, and then the custom field was
        // changed to be required.
        TextCustomFieldDefinition textCFD = new TextCustomFieldDefinition(name: "Text field", label: "Text field", isRequired: true, allTypes: true)
        textCFD.save(failOnError: true, flush: true)
        JSONObject listingJSON = getListingJSON()
        addCustomFieldValue(listingJSON, textCFD, null)
        ImportStatus stats = new ImportStatus()
        serviceItemImporter.importFromJSON(listingJSON, stats.serviceItems)
        assert 0 == stats.serviceItems.failed
        assert 1 == stats.serviceItems.created
        assert 0 == stats.serviceItems.updated
        assert 0 == stats.serviceItems.notUpdated
        assert 1 == ServiceItem.count()
        ServiceItem serviceItem = ServiceItem.findByUuid(listingJSON.uuid)
        assert null != serviceItem
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
