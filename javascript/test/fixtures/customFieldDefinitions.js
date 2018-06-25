define([
    'underscore'
], function (_) {
    var customFieldDefinitions = [
        {
            "fieldType": "DROP_DOWN",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.DropDownCustomFieldDefinition",
            "label": "<b> a BUG!</b> <button> test </button>",
            "isMultiSelect": false,
            "section": "primaryCharacteristics",
            "isPermanent": false,
            "id": 2,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": "<b>You may have found a BUG!</b> <button> test </button> <span onmouseover=\"alert(1);\">Mouse over this text</span>",
            "name": "<b> a BUG!</b> <button> test </button>",
            "uuid": "eea35a03-b0d4-4764-8062-f83f3a1ee35a",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "fieldValues": [
                {
                    "displayText": "<button>tst</button>",
                    "isEnabled": 1
                },
                {
                    "displayText": "hi",
                    "isEnabled": 1
                },
                {
                    "displayText": "bye",
                    "isEnabled": 1
                },
                {
                    "displayText": "xss suks",
                    "isEnabled": 1
                }
            ],
            "tooltip": "<b> a BUG!</b> <button> test </button>"
        },
        {
            "fieldType": "CHECK_BOX",
            "selectedByDefault": false,
            "editedDate": "2014-03-17T17:47:57Z",
            "class": "marketplace.CheckBoxCustomFieldDefinition",
            "label": "CF Checkbox",
            "section": "typeProperties",
            "isPermanent": false,
            "id": 3,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": true,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Checkbox",
            "uuid": "99544f4b-d876-447c-9db0-acb933b4f57d",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "tooltip": "CF Checkbox"
        },
        {
            "fieldType": "DROP_DOWN",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.DropDownCustomFieldDefinition",
            "label": "CF Dropdown",
            "isMultiSelect": false,
            "section": "typeProperties",
            "isPermanent": false,
            "id": 4,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Dropdown",
            "uuid": "5375c56d-7afd-493c-a85e-41b82e3d6a1b",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "fieldValues": [
                {
                    "displayText": "a",
                    "isEnabled": 1
                },
                {
                    "displayText": "b",
                    "isEnabled": 1
                },
                {
                    "displayText": "c",
                    "isEnabled": 1
                }
            ],
            "tooltip": null
        },
        {
            "fieldType": "IMAGE_URL",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.ImageURLCustomFieldDefinition",
            "label": "CF Image",
            "section": "typeProperties",
            "isPermanent": false,
            "id": 5,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Image",
            "uuid": "141b970e-6d54-406f-8595-ff56086e7bf7",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "tooltip": "CF Image"
        },
        {
            "fieldType": "DROP_DOWN",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.DropDownCustomFieldDefinition",
            "label": "CF Multi Dropdown",
            "isMultiSelect": true,
            "section": "typeProperties",
            "isPermanent": false,
            "id": 6,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Multi Dropdown",
            "uuid": "a9d40a1b-751b-4122-86e9-4698ebcdfe64",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "fieldValues": [
                {
                    "displayText": "a",
                    "isEnabled": 1
                },
                {
                    "displayText": "b",
                    "isEnabled": 1
                },
                {
                    "displayText": "c",
                    "isEnabled": 1
                }
            ],
            "tooltip": "CF Multi Dropdown"
        },
        {
            "fieldType": "TEXT",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.TextCustomFieldDefinition",
            "label": "CF Text",
            "section": "typeProperties",
            "isPermanent": false,
            "id": 7,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Text",
            "uuid": "b9cd30ab-fb20-4aa7-bca1-8ca6320a0f47",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "tooltip": "CF Text"
        },
        {
            "fieldType": "TEXT_AREA",
            "editedDate": "2014-03-14T16:24:50Z",
            "class": "marketplace.TextAreaCustomFieldDefinition",
            "label": "CF Text area",
            "section": "typeProperties",
            "isPermanent": false,
            "id": 8,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "isRequired": false,
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": null,
            "name": "CF Text area",
            "uuid": "ccd70ae8-baad-4b77-8db2-f744593bbaff",
            "types": [],
            "allTypes": true,
            "createdDate": "2014-03-14T16:24:50Z",
            "tooltip": "CF Text area"
        }        
    ];

    return customFieldDefinitions;
});