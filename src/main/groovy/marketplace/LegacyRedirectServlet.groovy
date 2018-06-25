package marketplace;

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletConfig

/**
 * A servlet that redirects to the configured URL. This is used to support
 * links from old Affiliated Marketplaces that link to pages that no longer exist.
 *
 * It is expected that the Servlet will be configured such that the id will be retrievable
 * using HttpServletRequest#getPathInfo
 *
 * Expected Servlet configs:
 * "redirectTarget" A URL including the string ":id" which will
 * be replaced with the id parsed from the incoming URL.  The redirectTarget is the URL to
 * which the client is redirected
 */
class LegacyRedirectServlet extends HttpServlet {
    public static final String REDIRECT_TARGET_CONFIG = "redirectTarget"

    private String redirectTarget

    void init(ServletConfig conf) {
        redirectTarget = conf.getInitParameter(REDIRECT_TARGET_CONFIG)
    }

    void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String id = getId(req)
        String destination = req.contextPath + redirectTarget.replace(":id", id)

        //note: since we want a permanent redirect, we cannot use sendRedirect
        resp.setStatus(resp.SC_MOVED_PERMANENTLY)
        resp.setHeader("Location", destination)
    }

    protected String getId(HttpServletRequest req) {
        req.pathInfo.replaceFirst("^/", "")
    }
}
