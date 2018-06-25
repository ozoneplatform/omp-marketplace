package marketplace

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional


@Transactional
class TextService extends MarketplaceService {

    public static String ABOUT_DESCRIPTION =
            /The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content./

    public static List<Map> REQUIRED_TEXTS =
            [[name: Constants.TEXT_NAME_ABOUT, readOnly: false, value: ABOUT_DESCRIPTION],
             [name: Constants.TEXT_NAME_VERSION, readOnly: true, value: "2.0"]]

    public static List<String> DEPRECATED_TEXTS =
            [Constants.TEXT_NAME_MP_DESCRIPTION,
             Constants.TEXT_NAME_MP_ADVANCED_SEARCH_DESCRIPTION,
             Constants.TEXT_NAME_ANALYST,
             Constants.TEXT_NAME_DEVELOPER,
             Constants.TEXT_NAME_ADMINISTRATOR]

    @ReadOnly
    List<Text> list(Map params) {
        Text.findAllByReadOnly(false, params)
    }

    @ReadOnly
    Text get(Map params) {
        Text.findByIdAndReadOnly(params.id as Long, false)
    }

    @ReadOnly
    int countTypes() {
        Text.countByReadOnly(false)
    }

    void manageRequiredTexts() {
        // Add all required Texts if they do not exist
        REQUIRED_TEXTS.each { text ->
            if (Text.findByName(text.name as String) == null) {
                new Text(text).save()
            }
        }

        // Remove all deprecated Texts if they exist
        DEPRECATED_TEXTS.each { name ->
            Text text = Text.findByName(name)
            if (text != null) {
                text.delete()
            }
        }
    }

}
