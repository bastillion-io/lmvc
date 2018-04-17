package com.lmvc.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.lmvc.base.TemplateServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebListener
public class TemplateConfig implements ServletContextListener {

	private static final String TEMPLATE_ENGINE = "com.thymeleafexamples.thymeleaf3.TemplateEngineInstance";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TemplateEngine engine = templateEngine(sce.getServletContext());
		sce.getServletContext().setAttribute(TEMPLATE_ENGINE, engine);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private TemplateEngine templateEngine(ServletContext servletContext) {
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(templateResolver(servletContext));
		return engine;
	}

	private ITemplateResolver templateResolver(ServletContext servletContext) {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
		resolver.setPrefix("/");
		resolver.setSuffix(TemplateServlet.VIEW_EXT);
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
	
	public static TemplateEngine getTemplateEngine(ServletContext context) {
		return (TemplateEngine) context.getAttribute(TEMPLATE_ENGINE);
	}
}