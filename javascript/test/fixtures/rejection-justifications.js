define([
    'underscore'
], function () {
    var rejectionJustifications = {};

    rejectionJustifications.RESPONSE = {
        "success": true,
        "totalCount": 5,
        "data": [
            {
                "id": 3,
                "title": "Data Content",
                "description": "Data Content"
            },
            {
                "id": 5,
                "title": "Lack of Documentation",
                "description": "Lack of Documentation"
            },
            {
                "id": 4,
                "title": "Not Operational",
                "description": "Not Operational"
            },
            {
                "id": 2,
                "title": "Security Guideline",
                "description": "Security Guideline"
            },
            {
                "id": 1,
                "title": "Sustainment",
                "description": "Sustainment"
            }
        ]
    };

    return rejectionJustifications;
});