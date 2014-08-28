package marketplace

import org.springframework.context.*

class ThemeCssTagLib implements ApplicationContextAware {

    static namespace = 'marketplaceTheme'

    def themeService
    def applicationContext

    def defaultCssPath = {
        out << "${request.contextPath}/" + getCurrentTheme().css.toString()
    }

    // Used to output theme base url when referencing other css files besides the theme standard one
    def defaultThemeBasePath = {
        out << "${request.contextPath}/" + getCurrentTheme().base_url.toString()
    }

    def getCurrentThemeName = {
        out << "" + getCurrentTheme().name.toString()
    }

    def getThemeFontSize = {
        out << getCurrentTheme().font_size.toString()
    }

    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    def getCurrentTheme() {
        themeService.getCurrentTheme()
    }
}
