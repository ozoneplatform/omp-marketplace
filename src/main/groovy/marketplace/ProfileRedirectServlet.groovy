package marketplace;

import javax.servlet.http.HttpServletRequest

/**
 * A subclass of LegacyRedirectServlet that converts usernames in the incoming
 * request into database ids in the redirect
 */
class ProfileRedirectServlet extends LegacyRedirectServlet {
    protected String getId(HttpServletRequest req) {
        Profile.findByUsername(req.pathInfo.replaceFirst("^/", ""))?.id ?: 0
    }
}
