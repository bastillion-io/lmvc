package com.lmvc.base;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lmvc.config.TemplateConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

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
        WebContext context = new WebContext(request, response, request.getServletContext());
        String uri = request.getRequestURI().replaceAll("\\" + TemplateServlet.VIEW_EXT + ".*", "");
        engine.process(uri, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        doGet(request, response);
    }
}