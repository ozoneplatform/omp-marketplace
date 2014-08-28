package marketplace

import ozone.utils.Utils;
import grails.test.mixin.TestFor
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class UtilsTests {

    void testUrlPatternMatches() {
        def fullHttps = 'https://full.org/url?arg1=%AA%_val@#tzt'
        def fullHttp = 'http://full.org/url'
        def fullFtp = 'ftp://full.org/url'
        def fullFile = 'file:///c:/full/url/file.txt'
        def rel = '/full'
        def fullHttpsTerm = 'https://full.org/url/'
        def fullHttpTerm = 'http://full.org/url/'
        def fullFtpTerm = 'ftp://full.org/url/'
        def relTerm = '/full/'
        def fullHttpsDef = 'https://full.org/url/file.png'
        def fullHttpDef = 'http://full.org/url/file.png'
        def fullFtpDef = 'ftp://full.org/url/file.png'
        def relDef = '/full/file.png'
        def fullHttpsAnch = 'https://full.org/url#anch_'
        def fullHttpAnch = 'http://full.org/url#anch_'
        def fullFtpAnch = 'ftp://full.org/url#anch_'
        def relAnch = '/full#anch_'
        def fullHttpsParens = 'https://full.org/url?arg1=%AA%_va()l@#tzt'
        def fullHttpsChars = 'https://full.org/url?#arg1=%AA%_va&l@#tzt ?#'
        def fullHttpsPort = 'https://full.org: 8080/url?#arg1=%AA%_va&l@#tzt ?#'
        def fullJavascript = 'javascript://full.org/url?arg1=%AA%_va()l@#tzt'

        assertTrue fullHttps ==~ Constants.URL_PATTERN
        assertTrue fullHttp ==~ Constants.URL_PATTERN
        assertTrue fullFtp ==~ Constants.URL_PATTERN
        assertTrue fullFile ==~ Constants.URL_PATTERN
        assertTrue rel ==~ Constants.URL_PATTERN
        assertTrue fullHttps ==~ Constants.URL_PATTERN
        assertTrue fullHttp ==~ Constants.URL_PATTERN
        assertTrue fullFtp ==~ Constants.URL_PATTERN
        assertTrue rel ==~ Constants.URL_PATTERN
        assertTrue fullHttpsDef ==~ Constants.URL_PATTERN
        assertTrue fullHttpDef ==~ Constants.URL_PATTERN
        assertTrue fullFtpDef ==~ Constants.URL_PATTERN
        assertTrue relDef ==~ Constants.URL_PATTERN
        assertTrue fullHttpsAnch ==~ Constants.URL_PATTERN
        assertTrue fullHttpAnch ==~ Constants.URL_PATTERN
        assertTrue fullFtpAnch ==~ Constants.URL_PATTERN
        assertTrue relAnch ==~ Constants.URL_PATTERN
        assertTrue fullHttpsParens ==~ Constants.URL_PATTERN
        assertTrue fullHttpsChars ==~ Constants.URL_PATTERN
        assertTrue fullHttpsPort ==~ Constants.URL_PATTERN
        assertFalse fullJavascript ==~ Constants.URL_PATTERN
    }

    void testParseUrl() {
        def url = 'https://full.org:3333/url?arg1=%AA%_val@#tzt'
        def m = Helper.parseUrl(url)
        assertNotNull m
        assertTrue m['scheme'] == 'https'
        assertTrue m['host'] == 'full.org'
        assertTrue m['port'] == '3333'
        assertTrue m['path'] == '/url'
        assertTrue m['args'] == '?arg1=%AA%_val@'
        assertTrue m['related'] == 'tzt'

        url = 'https://full.org/'
        m = Helper.parseUrl(url)
        assertNotNull m
        assertTrue m['scheme'] == 'https'
        assertTrue m['host'] == 'full.org'
        assertNull m['port']
        assertTrue m['path'] == '/'
        assertNull m['args']
        assertNull m['related']

        url = 'https://full.org'
        m = Helper.parseUrl(url)
        assertNotNull m
        assertTrue m['scheme'] == 'https'
        assertTrue m['host'] == 'full.org'
        assertNull m['port']
        assertNull m['path']
        assertNull m['args']
        assertNull m['related']

        url = 'full.org'
        m = Helper.parseUrl(url)
        assertNotNull m
        assertNull m['scheme']
        assertNull m['host']
        assertNull m['port']
        assertTrue m['path'] == url
        assertNull m['args']
        assertNull m['related']

        url = '/full.org'
        m = Helper.parseUrl(url)
        assertNotNull m
        assertNull m['scheme']
        assertNull m['host']
        assertNull m['port']
        assertTrue m['path'] == url
        assertNull m['args']
        assertNull m['related']

        url = ''
        m = Helper.parseUrl(url)
        assertNotNull m
        assertNull m['scheme']
        assertNull m['host']
        assertNull m['port']
        assertNull m['path']
        assertNull m['args']
        assertNull m['related']

        url = null
        m = Helper.parseUrl(url)
        assertNotNull m
        assertNull m['scheme']
        assertNull m['host']
        assertNull m['port']
        assertNull m['path']
        assertNull m['args']
        assertNull m['related']
    }

    void testEllipsizeString() {
        def str = '''
            Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, `and what is the use of a book,' thought Alice `without pictures or conversation?'
            So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies, when suddenly a White Rabbit with pink eyes ran close by her.
            There was nothing so very remarkable in that; nor did Alice think it so very much out of the way to hear the Rabbit say to itself, `Oh dear! Oh dear! I shall be late!' (when she thought it over afterwards, it occurred to her that she ought to have wondered at this, but at the time it all seemed quite natural); but when the Rabbit actually took a watch out of its waistcoat-pocket, and looked at it, and then hurried on, Alice started to her feet, for it flashed across her mind that she had never before seen a rabbit with either a waistcoat-pocket, or a watch to take out of it, and burning with curiosity, she ran across the field after it, and fortunately was just in time to see it pop down a large rabbit-hole under the hedge.
            '''
        def truncatedString = Utils.ellipsizeString(str, 255)
        assertTrue "Does not end with ellipses",  truncatedString.endsWith(Utils.ELLIPSES)
        assertTrue "Too Long", truncatedString.size() <= 255
        assertTrue "Wrong beginning", truncatedString.startsWith(str.substring(0, 100))

        str = 'So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies'
        truncatedString = Utils.ellipsizeString(str, 255)
        assertEquals str, truncatedString
    }
}
