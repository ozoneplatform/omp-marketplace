println("appconfig loading")

databaseChangeLog = {

    def detailValues = ["types", "categories", "state", "releaseDate",
            "lastActivity", "owners", "organization", "Alternate POC Info",
            "Technical POC Info", "Support POC Info", "launchUrl"]
            .toListString().replace("[", "").replace("]", "")

    def doConfigInsert = { appConfig, groupName, subGroupName, order ->
        insert(tableName: "application_configuration") {
            column(name: "code", value: appConfig.code)
            column(name: "type", value: appConfig.type)
            column(name: "group_name", value: groupName)
            column(name: "mutable", valueBoolean: appConfig.mutable)
            column(name: "sub_group_order", valueNumeric: order)
            column(name: "version", valueNumeric: 0)
            column(name: "title", value: " ")
            column(name: "sub_group_name", value: subGroupName)
            column(name: '${appconfig.valColumn}', value: appConfig.value)
        }
    }

    changeSet(author: "marketplace", id: "app_config-1", dbms: "oracle",  context: "create") {
        comment("Trigger for Oracle database to handle primary key generation based on a sequence during 'application_configuration' table insert statements")
        sql(endDelimiter: "", splitStatements: false, sql: """
            create or replace trigger app_config_insert before insert on application_configuration
            for each row
            when (new.id is null)
            begin
            select hibernate_sequence.nextval into :new.id from dual;
            end;
            /
        """)
    }

    // Insert inital values
    changeSet(author: "marketplace", id: "app_config-2", dbms: "mssql, oracle, postgresql, mysql, hsqldb, h2",  context: "create") {

          //Initial "Branding"
        [
            [items: [[code: "store.name", type: "String", mutable: true, value: ""],
                     [code: "store.logo", type: "Image", mutable: true, value: "/static/themes/gold.theme/images/Mp_logo.png"],
                     [code: "store.icon", type: "Image", mutable: true, value: "/static/themes/common/images/agency/agencyDefault.png"],
                     [code: "free.warning.content", type: "String", mutable: true, value: ""],
                     [code: 'url.public', type: 'string', mutable: true, value: '']]
            ],

            [items: [[code: "about.box.content", type: "String", mutable: true, value: "The Store allows visitors to discover and explore business and convenience applications and enables user-configurable visualizations of available content."],
                     [code: "about.box.image", type: "Image", mutable: true, value: "/static/themes/gold.theme/images/Mp_logo_128x128.png"]],
             subGroupName: "About Information"
            ],

            [items: [[code: "access.alert.enable", type: "Boolean", value: "true", mutable: true],
                     [code: "access.alert.content", type: "String", mutable: true, value: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a."]],
             subGroupName: "Access Alert Information"
            ],

            [items: [[code: "store.footer.featured.title", type: "String", mutable: true, value: "Store"],
                     [code: "store.footer.featured.content", type: "String", mutable: true, value: "The Store allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content."]],
             subGroupName: "Footer Information"
            ],

            [items: [[code: "store.open.search.title.message", type: "String", mutable: true, value: "Marketplace Search"],
                     [code: "store.open.search.description.message", type: "String", mutable: true, value: "Marketplace Search Description"],
                     [code: "store.open.search.fav.icon", type: "Image", mutable: true, value: "/static/themes/gold.theme/images/favicon.ico"],
                     [code: "store.open.search.site.icon", type: "Image", mutable: true, value: "/static/themes/common/images/themes/default/market_64x64.png"]],
             subGroupName: "Open Search"
            ],

            [items: [[code: "store.custom.header.url", type: "String", mutable: true],
                     [code: "store.custom.header.height", type: "Integer", mutable: true, value: "0"],
                     [code: "store.custom.footer.url", type: "String", mutable: true],
                     [code: "store.custom.footer.height", type: "Integer", mutable: true, value: "0"],
                     [code: "store.custom.css", type: "String", mutable: true],
                     [code: "store.custom.js", type: "String", mutable: true]],
             subGroupName: "Custom Header and Footer"
            ]
        ].each { subGroup ->
            subGroup.items.eachWithIndex { appConfig, index -> doConfigInsert(appConfig, "BRANDING", subGroup.subGroupName, index+1) }
        }

        //Initial "Additional Configuration"
        [
            [items:[[code: "store.insideOutside.behavior", type: "String", mutable: true, value: "ADMIN_SELECTED"],
                    [code: "store.enable.ext.serviceitem", type: "Boolean", mutable: true, value: "false"],
                    [code: "store.allow.owner.to.edit.approved.listing", type: "Boolean", mutable: true, value: "true"]]
            ],

            [items:[[code: "store.amp.search.result.size", type: "Integer",  mutable: true, value: "30"],
                    [code: "store.amp.search.default.timeout", type: "Integer",  mutable: true, value: "30000"]],
             subGroupName: 'Partner Store Search'],

            [items:[[code: "store.image.allow.upload", type: "Boolean", mutable: true, value: "true"],
                    [code: "store.type.image.max.size", type: "Integer", mutable: true, value: "${1024 * 1024}"],
                    [code: "store.amp.image.max.size", type: "Integer", mutable: true, value: "${1024 * 1024}"]],
             subGroupName: 'Partner Store Image Configurations'],

            [items: [[code: "store.owf.sync.urls", type: "List", mutable: true]],
                    subGroupName: "OWF Sync"],

            [items: [[code: "store.contact.email", type: "String", mutable: true]],
             subGroupName: "Store Contact Information"
            ],
            [items: [[code: 'store.quick.view.detail.fields', type: 'String', mutable: true, value: detailValues]],
             subGroupName: 'Quick View'
            ]
        ].each { subGroup ->
            subGroup.items.eachWithIndex { appConfig, index -> doConfigInsert(appConfig, "ADDITIONAL_CONFIGURATION", subGroup.subGroupName, index+1) }
        }

        //Initial "Scorecard"
        def scoreCardConfig = [code: "store.enable.scoreCard", type: "Boolean", mutable: true, value: "false"]
        doConfigInsert(scoreCardConfig, "SCORECARD", null, 1)

        //Initial "Hidden"
        [
            [code: "store.is.franchise", type: "Boolean",  mutable: false, value: "true"],
            [code: "store.default.theme", type: "String", mutable: true, value: "gold"],
            [code: "store.job.disable.accounts.interval", type: "Integer", mutable: true, value: "1440"],
            [code: "store.job.disable.accounts.start.time", type: "String", mutable: true, value: "23:59:59"],
        ].eachWithIndex{ appConfig, index -> doConfigInsert(appConfig, "HIDDEN", null, index+1) }

        //Initial "User Account Settings"
        [
            [items: [[code: "store.session.control.enabled", type: "Boolean", mutable: true, value: "false"],
                    [code: "store.session.control.max.concurrent", type: "Integer", mutable: true, value: "1"]],
                    subGroupName: "Session Control"
            ],

            [items: [[code: "store.disable.inactive.accounts", type: "Boolean", mutable: true, value: "true"],
                    [code: "store.inactivity.threshold", type: "Integer", mutable: true, value: "90"]],
                    subGroupName: "Inactive Accounts"
            ]
        ].each { subGroup ->
            subGroup.items.eachWithIndex{ appConfig, index -> doConfigInsert(appConfig, "USER_ACCOUNT_SETTINGS", subGroup.subGroupName, index+1) }
        }

        [
            [code: "store.enable.cef.logging", type: "Boolean", mutable: true, value: "true"],
            [code: "store.enable.cef.object.access.logging", type: "Boolean", mutable: true, value: "false"],
            [code: "store.enable.cef.log.sweep", type: "Boolean", mutable: true, value: "true"],
            [code: "store.cef.log.location", type: "String", mutable: true, value: "/usr/share/tomcat6"],
            [code: "store.cef.sweep.log.location", type: "String", mutable: true, value: "/var/log/cef"],
            [code: "store.security.level", type: "String", mutable: true]
        ].eachWithIndex{ appConfig, index -> doConfigInsert(appConfig, "AUDITING", null, index+1) }

    }

    changeSet(author: "marketplace", id: "app_config-3", dbms: "oracle", context: "create") {
        comment("Drop the trigger")
        sql(endDelimiter: "", splitStatements: false, sql: """
            drop trigger app_config_insert;
        """)
    }
}
