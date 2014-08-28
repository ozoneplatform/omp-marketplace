(function () {

    var defaults = {
        context: '/marketplace',
        user: {
            "isAdmin": false
        },
        appconfigs: [
            { "code": 'store.amp.search.result.size', "value": 10 },
            { "code": 'free.warning.content', "value": 'free text!' },
            { "code": 'store.insideOutside.behavior', "value": 'ADMIN_SELECTED' },
            { "code": 'store.allow.owner.to.edit.approved.listing', "value": 'false' },
            { "code": 'store.image.allow.upload', "value": 'true' },
            { "code": 'store.amp.search.default.timeout', "value": '3000' }
        ],

        resetConfigDefaults: function () {
            window.Marketplace = defaults;
        },

        defaultState: {
            "id": 1,
            "title": "Active",
            "uuid": "a290db7b-e7b2-433a-ab95-03240c597610"
        },
        types :[
            {
                "id": 4,
                "title": "type 4 wayne<\/button>",
                "description" : "eat it",
                "hasLaunchUrl" :true,
                "hasIcons" :true,
                "uuid" :"c930f384-7dc1-4b1b-98d2-b0da07ac515d",
                "ozoneAware": true,
                "isPermanent": false
            },{
                "id": 2,
                "title": "App Component",
                "description": "app component",
                "hasLaunchUrl": true,
                "hasIcons": true,
                "uuid": "d9735dc0-dede-4176-81d1-2e5a5ee93344",
                "ozoneAware": true,
                "isPermanent": true
            },{
                "id": 3,
                "title": "OZONE App",
                "description": "OZONE app",
                "hasLaunchUrl": false,
                "hasIcons": false,
                "uuid": "dbc6c9ea-114e-4937-aa55-d8d5848929cc",
                "ozoneAware": true,
                "isPermanent": true
            },{
                "id": 1,
                "title": "Web App",
                "description": "web app",
                "hasLaunchUrl": true,
                "hasIcons": false,
                "uuid": "e03a28a2-3a47-46b3-8639-8a6407c64956",
                "ozoneAware": false,
                "isPermanent": false
            }
        ],
        "contactTypes":[
            {
                "id": 0,
                "title": "primary",
                "required": true
            },
            {
                "id": 1,
                "title": "secondary",
                "required": false
            }
        ]
    };

    window.Marketplace = defaults;
}());
