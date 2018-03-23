package marketplace

import org.springframework.context.*

import ozone.marketplace.domain.ThemeDefinition


class ThemeCssTagLib implements ApplicationContextAware {

    static namespace = 'marketplaceTheme'

    ThemeService themeService

    ApplicationContext applicationContext

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

    ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    ThemeDefinition getCurrentTheme() {
        themeService.getCurrentTheme()
    }


    Closure configScript = { attrs ->
        String context = attrs.context ?: request.contextPath ?: ""

        out << script("${context}/static/config.js")
    }

    def themeStylesheet = { attrs ->
        String themeName = attrs.theme

        if (themeName) {
            String encoded = themeName.encodeAsURL().encodeAsHTML()
            out << linkStylesheet("${request.contextPath}" + "/static/themes/${encoded}.theme/css/${encoded}.css")
            return
        }

        String currentTheme = themeService.getCurrentTheme().css.toString()

        if (currentTheme == null || currentTheme.isEmpty()) {
            out << linkStylesheet('about:blank')
            return
        }

        out << linkStylesheet("${request.contextPath}" + "/static/" + currentTheme, 'theme')
    }

    def bootstrapStylesheet = { attrs ->
        String currentTheme = themeService.getCurrentTheme().css.toString()

        if (currentTheme == null || currentTheme.isEmpty()) {
            out << linkStylesheet('about:blank')
            return
        }

        def bootstrapTheme = (currentTheme =~ /[^\/]+$/).replaceFirst('bootstrap.css')

        out << linkStylesheet("${request.contextPath}" + "/static/" + bootstrapTheme, 'bootstrap')
    }

    def dataTablesStylesheet = { attrs ->
        String currentTheme = themeService.getCurrentTheme().css.toString()

        if (currentTheme == null || currentTheme.isEmpty()) {
            out << linkStylesheet('about:blank')
            return
        }

        def bootstrapTheme = (currentTheme =~ /[^\/]+$/).replaceFirst('dataTables.css')

        out << linkStylesheet("${request.contextPath}" + "/static/" + bootstrapTheme, 'bootstrap')
    }

    def imageLink = { attrs ->
        String currentTheme = themeService.getCurrentTheme().css.toString()

        if (currentTheme == null || currentTheme.isEmpty()) {
            out << linkStylesheet('about:blank')
            return
        }
        def imageLink = (currentTheme =~ /\/css.*/).replaceFirst('/images/' + attrs.src)
        out << "${request.contextPath}" + "/static/" + imageLink

    }

    def vendor = { attrs ->
        String src = attrs.src
        String context = attrs.context ?: request.contextPath ?: ""

        out << script("$context/static/vendor/$src")
    }
    def javascript = { attrs ->
        String src = attrs.src
        out << script("${request.contextPath}" + "/static/js/$src")
    }
    private static String linkStylesheet(String href, String id = null) {
        return (id != null) ? """<link id="$id" rel="stylesheet" type="text/css" href="$href"  media="screen, projection"/>"""
                : """<link rel="stylesheet" type="text/css" href="$href"  media="screen, projection"/>"""
    }
    private static String script(String src) {
        """<script type="text/javascript" src="$src"></script>"""
    }

}
