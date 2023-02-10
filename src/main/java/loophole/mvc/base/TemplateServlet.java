/**
 * Copyright (C) 2018 Loophole, LLC
 *
 * Licensed under The Prosperity Public License 3.0.0
 */
package loophole.mvc.base;

import loophole.mvc.config.TemplateConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*" + TemplateServlet.VIEW_EXT)
public class TemplateServlet extends HttpServlet {

    public static final String VIEW_EXT = ".html";
    private static final long serialVersionUID = 411L;

    public TemplateServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        TemplateEngine engine = TemplateConfig.getTemplateEngine(request.getServletContext());
        JavaxServletWebApplication application =
                JavaxServletWebApplication.buildApplication(request.getServletContext());
        response.setContentType("text/html; charset=UTF-8");

        final IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange);
        String uri = request.getRequestURI().replaceAll("\\" + TemplateServlet.VIEW_EXT + ".*", TemplateServlet.VIEW_EXT)
                .replaceAll("^" + request.getContextPath(), "");
        engine.process(uri, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        doGet(request, response);
    }
}