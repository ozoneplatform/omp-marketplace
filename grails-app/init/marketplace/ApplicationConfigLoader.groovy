package marketplace.configuration

import org.hibernate.SessionFactory

import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import org.ozoneplatform.appconfig.server.domain.model.ApplicationSetting

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*


class ApplicationConfigLoader {

    private static final List<String> DETAIL_VALUES = ["types", "categories", "state", "releaseDate",
                                                       "lastActivity", "owners", "organization", "Alternate POC Info",
                                                       "Technical POC Info", "Support POC Info", "launchUrl"]

    private static final List<Map> BRANDING_CONFIG =
            [[items: [[code: STORE_NAME, type: "String"],
                      [code: STORE_LOGO, type: "Image", value: "/static/themes/gold.theme/images/Mp_logo.png"],
                      [code: STORE_ICON, type: "Image", value: "/static/themes/common/images/agency/agencyDefault.png"],
                      [code: FREE_WARNING_CONTENT, type: "String"],
                      [code: URL_PUBLIC, type: 'String']]],

             [subGroup: "About Information",
              items   : [[code: ABOUT_BOX_CONTENT, type: "String", value: "The Store allows visitors to discover and explore business and convenience applications and enables user-configurable visualizations of available content."],
                         [code: ABOUT_BOX_IMAGE, type: "Image", value: "/static/themes/gold.theme/images/Mp_logo_128x128.png"]]],

             [subGroup: "Access Alert Information",
              items   : [[code: ACCESS_ALERT_ENABLED, type: "Boolean", value: "true"],
                         [code: ACCESS_ALERT_CONTENT, type: "String", value: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a."]]],

             [subGroup: "Footer Information",
              items   : [[code: STORE_FOOTER_FEATURED_TITLE, type: "String", value: "Store"],
                         [code: STORE_FOOTER_FEATURED_CONTENT, type: "String", value: "The Store allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content."]]],

             [subGroup: "Open Search",
              items   : [[code: OPEN_SEARCH_TITLE_MESSAGE, type: "String", value: "Marketplace Search"],
                         [code: OPEN_SEARCH_DESCRIPTION_MESSAGE, type: "String", value: "Marketplace Search Description"],
                         [code: OPEN_SEARCH_FAV_ICON, type: "Image", value: "/static/themes/gold.theme/images/favicon.ico"],
                         [code: OPEN_SEARCH_SITE_ICON, type: "Image", value: "/static/themes/common/images/themes/default/market_64x64.png"]]],

             [subGroup: "Custom Header and Footer",
              items   : [[code: CUSTOM_HEADER_URL, type: "String"],
                         [code: CUSTOM_HEADER_HEIGHT, type: "Integer", value: "0"],
                         [code: CUSTOM_FOOTER_URL, type: "String"],
                         [code: CUSTOM_FOOTER_HEIGHT, type: "Integer", value: "0"],
                         [code: CUSTOM_CSS_IMPORTS, type: "String"],
                         [code: CUSTOM_JS_IMPORTS, type: "String"]]]]

    private static final List<Map> ADDITIONAL_CONFIG =
            [[items: [[code: INSIDE_OUTSIDE_BEHAVIOR, type: "String", value: "ADMIN_SELECTED"],
                      [code: EXTERNAL_SERVICE_ITEM_ENABLED, type: "Boolean", value: "false"],
                      [code: ALLOW_OWNER_TO_EDIT_APPROVED_LISTING, type: "Boolean", value: "true"]]],

             [subGroup: 'Partner Store Search',
              items   : [[code: AMP_SEARCH_RESULT_SIZE, type: "Integer", value: "30"],
                         [code: AMP_SEARCH_DEFAULT_TIMOUT, type: "Integer", value: "30000"]]],

             [subGroup: 'Partner Store Image Configurations',
              items   : [[code: ALLOW_IMAGE_UPLOAD, type: "Boolean", value: "true"],
                         [code: TYPE_IMAGE_MAX_SIZE, type: "Integer", value: "${1024 * 1024}"],
                         [code: AMP_IMAGE_MAX_SIZE, type: "Integer", value: "${1024 * 1024}"]]],

             [subGroup: "OWF Sync",
              items   : [[code: OWF_SYNC_URLS, type: "List"]]],

             [subGroup: "Store Contact Information",
              items   : [[code: STORE_CONTACT_EMAIL, type: "String"]]],

             [subGroup: 'Quick View',
              items   : [[code: QUICK_VIEW_DETAIL_FIELDS, type: 'String', value: String.join(", ", DETAIL_VALUES)]]]]

    private static final List<Map> USER_ACCOUNT_CONFIG =
            [[subGroup: "Session Control",
              items   : [[code: SESSION_CONTROL_ENABLED, type: "Boolean", value: "false"],
                         [code: SESSION_CONTROL_MAX_CONCURRENT, type: "Integer", value: "1"]]],

             [subGroup: "Inactive Accounts",
              items   : [[code: DISABLE_INACTIVE_ACCOUNTS, type: "Boolean", value: "true"],
                         [code: INACTIVITY_THRESHOLD, type: "Integer", value: "90"]],]]

    private static final List<Map> AUDITING_CONFIG =
            [[code: CEF_LOGGING_ENABLED, type: "Boolean", value: "true"],
             [code: CEF_OBJECT_ACCESS_LOGGING_ENABLED, type: "Boolean", value: "false"],
             [code: CEF_LOG_SWEEP_ENABLED, type: "Boolean", value: "true"],
             [code: CEF_LOG_LOCATION, type: "String", value: "/usr/share/tomcat6"],
             [code: CEF_LOG_SWEEP_LOCATION, type: "String", value: "/var/log/cef"],
             [code: SECURITY_LEVEL, type: "String"]]

    private static final List<Map> HIDDEN_CONFIG =
            [[code: FRANCHISE_STORE, type: "Boolean", mutable: false, value: "true"],
             [code: STORE_DEFAULT_THEME, type: "String", value: "gold"],
             [code: JOB_DISABLE_ACCOUNTS_INTERVAL, type: "Integer", value: "1440"],
             [code: JOB_DISABLE_ACCOUNTS_START, type: "String", value: "23:59:59"]]

    private final SessionFactory sessionFactory

    ApplicationConfigLoader(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory
    }

    void loadDefaultSettings() {
        ApplicationConfiguration.withTransaction {
            createConfigSubGroups("BRANDING", BRANDING_CONFIG)
            createConfigSubGroups("ADDITIONAL_CONFIGURATION", ADDITIONAL_CONFIG)
            createConfigSubGroups("USER_ACCOUNT_SETTINGS", USER_ACCOUNT_CONFIG)

            createConfigGroup("AUDITING", AUDITING_CONFIG)
            createConfigGroup("HIDDEN", HIDDEN_CONFIG)

            create([code: SCORE_CARD_ENABLED, type: "Boolean", value: "false"], "SCORECARD", null, 1)
        }

        sessionFactory.currentSession.with {
            flush()
            clear()
        }
    }

    private static void createConfigSubGroups(String groupName, List<Map> configGroup) {
        configGroup.each { Map it -> createConfigGroup(groupName, it.items as List<Map>, it.subGroup as String) }
    }

    private static void createConfigGroup(String groupName, List<Map> configGroup, String subGroup = null) {
        configGroup.eachWithIndex { Map cfg, int i -> create(cfg, groupName, subGroup, i + 1) }
    }

    private static void create(Map config, String group, String subGroup, int order) {
        String code = (config.code as ApplicationSetting).getCode()

        if (ApplicationConfiguration.findByCode(code)) return

        new ApplicationConfiguration(
                code: code,
                type: config.type,
                groupName: group,
                subGroupName: subGroup,
                subGroupOrder: order,
                mutable: config.mutable ?: true,
                value: config.value,
                version: 0,
                title: config.code).save(failOnError: true)
    }

}
