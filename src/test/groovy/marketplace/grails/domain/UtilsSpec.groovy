package marketplace.grails.domain

import marketplace.Constants
import marketplace.Helper
import ozone.utils.Utils
import spock.lang.Specification

class UtilsSpec extends Specification{

    void testUrlPatternMatches() {
        setup:
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

        expect:
        assert fullHttps ==~ Constants.URL_PATTERN
        assert fullHttp ==~ Constants.URL_PATTERN
        assert fullFtp ==~ Constants.URL_PATTERN
        assert fullFile ==~ Constants.URL_PATTERN
        assert rel ==~ Constants.URL_PATTERN
        assert fullHttps ==~ Constants.URL_PATTERN
        assert fullHttp ==~ Constants.URL_PATTERN
        assert fullFtp ==~ Constants.URL_PATTERN
        assert rel ==~ Constants.URL_PATTERN
        assert fullHttpsDef ==~ Constants.URL_PATTERN
        assert fullHttpDef ==~ Constants.URL_PATTERN
        assert fullFtpDef ==~ Constants.URL_PATTERN
        assert relDef ==~ Constants.URL_PATTERN
        assert fullHttpsAnch ==~ Constants.URL_PATTERN
        assert fullHttpAnch ==~ Constants.URL_PATTERN
        assert fullFtpAnch ==~ Constants.URL_PATTERN
        assert relAnch ==~ Constants.URL_PATTERN
        assert fullHttpsParens ==~ Constants.URL_PATTERN
        assert fullHttpsChars ==~ Constants.URL_PATTERN
        assert fullHttpsPort ==~ Constants.URL_PATTERN
        assert !(fullJavascript ==~ Constants.URL_PATTERN)
    }

    void testParseUrl() {
        when:
        def url = 'https://full.org:3333/url?arg1=%AA%_val@#tzt'
        def m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == 'https'
        assert m['host'] == 'full.org'
        assert m['port'] == '3333'
        assert m['path'] == '/url'
        assert m['args'] == '?arg1=%AA%_val@'
        assert m['related'] == 'tzt'

        when:
        url = 'https://full.org/'
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == 'https'
        assert m['host'] == 'full.org'
        assert m['port'] == null
        assert m['path'] == '/'
        assert m['args'] == null
        assert m['related'] == null

        when:
        url = 'https://full.org'
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == 'https'
        assert m['host'] == 'full.org'
        assert m['port'] == null
        assert m['path'] == null
        assert m['args'] == null
        assert m['related'] == null

        when:
        url = 'full.org'
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == null
        assert m['host'] == null
        assert m['port'] == null
        assert m['path'] == url
        assert m['args'] == null
        assert m['related'] == null

        when:
        url = '/full.org'
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == null
        assert m['host'] == null
        assert m['port'] == null
        assert m['path'] == url
        assert m['args'] == null
        assert m['related'] == null

        when:
        url = ''
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == null
        assert m['host'] == null
        assert m['port'] == null
        assert m['path'] == null
        assert m['args'] == null
        assert m['related'] == null

        when:
        url = null
        m = Helper.parseUrl(url)
        then:
        assert m != null
        assert m['scheme'] == null
        assert m['host'] == null
        assert m['port'] == null
        assert m['path'] == null
        assert m['args'] == null
        assert m['related'] == null
    }

    void testEllipsizeString() {
        when:
        def str = '''
            Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, `and what is the use of a book,' thought Alice `without pictures or conversation?'
            So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies, when suddenly a White Rabbit with pink eyes ran close by her.
            There was nothing so very remarkable in that; nor did Alice think it so very much out of the way to hear the Rabbit say to itself, `Oh dear! Oh dear! I shall be late!' (when she thought it over afterwards, it occurred to her that she ought to have wondered at this, but at the time it all seemed quite natural); but when the Rabbit actually took a watch out of its waistcoat-pocket, and looked at it, and then hurried on, Alice started to her feet, for it flashed across her mind that she had never before seen a rabbit with either a waistcoat-pocket, or a watch to take out of it, and burning with curiosity, she ran across the field after it, and fortunately was just in time to see it pop down a large rabbit-hole under the hedge.
            '''
        def truncatedString = Utils.ellipsizeString(str, 255)
        then:
        assert "Does not end with ellipses",  truncatedString.endsWith(Utils.ELLIPSES)
        assert "Too Long", truncatedString.size() <= 255
        assert "Wrong beginning", truncatedString.startsWith(str.substring(0, 100))

        when:
        str = 'So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies'
        truncatedString = Utils.ellipsizeString(str, 255)
        then:
        assert str, truncatedString
    }
}
