package marketplace

import java.util.regex.*;
import java.util.UUID;

import java.text.SimpleDateFormat

class Helper {

    static String shorten(String orig, int len, String suffix) {
        return Helper.shorten(orig, len, suffix, 0);
    }

    static String shorten(String orig, int len, String suffix, int lines) {
        String small = "";
        boolean addSuffix = false;
        if (!suffix) {
            suffix = "";
        }

        if (orig) {

            small = new String(orig);

            if (lines > 0) {

                // Limit number of lines displayed
                String[] a = small.split("\n")
                if (a.size() > lines) {
                    addSuffix = true;
                    small = "";
                    for (int i = 0; i < lines; i++) {
                        if (i > 0) {
                            small += "\n";
                        }
                        small = small + a[i];
                    }
                }
            }

            if (len > 0) {

                // Perform truncation
                if (small.size() > len) {
                    addSuffix = true;
                    small = small.substring(0, len)
                }

            }

            if (addSuffix) {
                small += suffix;
            }
        }

        return small;
    }

    static String deString(String orig) {
        String ret;
        if (null != orig && orig.size() > 2) {
            ret = new String(orig);
            ret = ret.substring(1, ret.size() - 1);
        }
        return ret;
    }

    static String highlight(String exp, String pattern) {
        String ret;

        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
        Matcher m = p.matcher(exp);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "<b>" + m.group() + "</b>");
        }
        m.appendTail(sb);
        ret = sb.toString();
        return ret;
    }

    static String generateLogReference() {
        def random = new Random()
        def randInt1 = random.nextInt(1000)
        def randInt2 = random.nextInt(1000)

        def uuid = UUID.randomUUID()
        def date = new Date()
        def formattedDate = date.format('MMM dd, yyyy HH:mm:ss zzz')

        return "${formattedDate} REF${randInt1}-${randInt2}"
    }

    // From the JSONObject class
    static Date parseExternalDate(String dateStr) {
        assert dateStr != null
        // Workaround for Java6:  ISO8601, which recognizes 'Z' as a timezone,
        //    is not supported until Java7; so we have to pretend to understand the date
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT, Locale.US)
        if (dateStr[-1..-1] == 'Z') {
            dateStr = "${dateStr[0..-2]}UTC"
        }
        return df.parse(dateStr)
    }

    /**
     * <pre>
     * Given a String representing a URL, parse it into its components
     * and return them as a Map containing: scheme, host, port, path, args, and related
     *
     * e.g. parseUrl('https://full.org:3333/url?arg1=%AA%_val@#tzt')
     * returns a Map with
     *   scheme : https
     *   host : full.org
     *   port : 3333
     *   path : /url
     *   args : arg1=%AA%_val@
     *   related : tzt
     * </pre>
     * @param url
     * @return
     */
    static Map parseUrl(String url) {
        def retVal = [:]
        if (url && url.size() > 0) {
            def m = (url =~ Constants.URL_PARSER)
            if (m?.groupCount() > 0) {
                if (m[0][2]) retVal['scheme'] = m[0][2]
                if (m[0][5]) retVal['path'] = m[0][5]
                if (m[0][6]) retVal['args'] = m[0][6]
                if (m[0][9]) retVal['related'] = m[0][9]

                if (m[0][4] != null && m[0][4].size() > 0) {
                    def l = m[0][4].split(':')
                    if (l?.size() > 0) {
                        retVal['host'] = l[0]
                    }
                    if (l?.size() > 1) {
                        retVal['port'] = l[1]
                    }
                }
            }
        }
        return retVal
    }

    static String convertDate(String date, String sourceFormat, String destinationFormat) throws java.text.ParseException {
        if (date) {
            SimpleDateFormat formatter = new SimpleDateFormat(sourceFormat, Locale.US)
            Date date1 = formatter.parse(date)

            formatter = new SimpleDateFormat(destinationFormat, Locale.US)
            return formatter.format(date1)
        } else {
            return null
        }
    }

    static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}

