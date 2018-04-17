
package com.lmvc.base;

import com.lmvc.filter.SecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*" + DispatcherServlet.CTR_EXT)
public class DispatcherServlet extends HttpServlet {


    private static Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String CTR_EXT = ".ktrl";
    private static final long serialVersionUID = 412L;

    public DispatcherServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    /**
     * Execute through base controller
     *
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     */
    private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseKontroller bc = new BaseKontroller(request, response);

        String forward = bc.execute();

        if (forward != null) {
            if (forward.contains("redirect:")) {
                log.debug(forward);
                //add csrf to redirect
                forward = forward.contains("?") ? forward + "&" : forward + "?";
                forward = forward + SecurityFilter._CSRF + "=" + request.getSession().getAttribute(SecurityFilter._CSRF);
                response.sendRedirect(forward.replaceAll("redirect:", ""));

            } else {
                log.debug(forward);
                request.getRequestDispatcher(forward.replaceAll("forward:", ""))
                        .forward(request, response);
            }
        }

    }
}