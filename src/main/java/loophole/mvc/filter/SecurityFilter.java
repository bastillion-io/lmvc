/**
 *    Copyright (C) 2018 Loophole, LLC
 *
 *    This program is free software: you can redistribute it and/or  modify
 *    it under the terms of the GNU Affero General Public License, version 3,
 *    as published by the Free Software Foundation.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the GNU Affero General Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package loophole.mvc.filter;

import loophole.mvc.base.DispatcherServlet;
import loophole.mvc.base.TemplateServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter that prevents click jacking, enforces transport security, and prevents
 * cross-site request forgery.
 */
@WebFilter(urlPatterns = {"/", "*" + DispatcherServlet.CTR_EXT, "*" + TemplateServlet.VIEW_EXT})
public class SecurityFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    // csrf parameter and session name
    public static final String _CSRF = "_csrf";
    private static final SecureRandom random = new SecureRandom();

    // x-frame-options header
    private static final String CSP_HEADER = "Content-Security-Policy";
    private static final String CSP_VALUE = "frame-ancestors 'self';";

    // strict-transport-security header
    private static final String TRANSPORT_SECURITY_HEADER = "Strict-Transport-Security";
    private static final String TRANSPORT_SECURITY_VALUE = "max-age=31536000";

    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        // click jacking header
        httpServletResponse.addHeader(CSP_HEADER, CSP_VALUE);

        // transport security header
        httpServletResponse.addHeader(TRANSPORT_SECURITY_HEADER, TRANSPORT_SECURITY_VALUE);

        // csrf check
        log.debug("CSRF parameter token is " + request.getParameter(_CSRF));
        log.debug("CSRF sesson token is " + httpServletRequest.getSession().getAttribute(_CSRF));
        String _csrf = (String) httpServletRequest.getSession().getAttribute(_CSRF);
        if (_csrf == null || _csrf.equals(request.getParameter(_CSRF))) {
            log.debug("CSRF token is valid for " + httpServletRequest.getRequestURL());
            if(_csrf == null || httpServletRequest.getMethod().equalsIgnoreCase("POST")) {
                _csrf = (new BigInteger(165, random)).toString(36).toUpperCase();
                httpServletRequest.getSession().setAttribute(_CSRF, _csrf);
            }
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("CSRF token is invalid for " + httpServletRequest.getRequestURL());
        httpServletRequest.getSession().invalidate();
        log.debug("Session invalidated");
        httpServletResponse.sendRedirect("/");
    }

    public void destroy() {
    }
}
