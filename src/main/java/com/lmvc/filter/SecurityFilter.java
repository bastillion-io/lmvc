package com.lmvc.filter;

import com.lmvc.base.DispatcherServlet;
import com.lmvc.base.TemplateServlet;
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
    private static final String X_FRAME_HEADER = "X-Frame-Options";
    private static final String X_FRAME_VALUE = "SAMEORIGIN";

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
        httpServletResponse.addHeader(X_FRAME_HEADER, X_FRAME_VALUE);

        // transport security header
        httpServletResponse.addHeader(TRANSPORT_SECURITY_HEADER, TRANSPORT_SECURITY_VALUE);

        // csrf check
        String _csrf = (String) httpServletRequest.getSession().getAttribute(_CSRF);
        if (_csrf == null || _csrf.equals(request.getParameter(_CSRF))) {
            log.debug("CSRF token is valid");
            _csrf = (new BigInteger(165, random)).toString(36).toUpperCase();
            httpServletRequest.getSession().setAttribute(_CSRF, _csrf);
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("CSRF token is invalid");
        httpServletRequest.getSession().invalidate();
        log.debug("Session invalidated");
        httpServletResponse.sendRedirect("/");
    }

    public void destroy() {
    }
}
