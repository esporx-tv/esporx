package tv.esporx.framework.mvc;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class ControllerUtils {

	public static ModelAndView notFound(final HttpServletResponse response, final String viewName) {
		response.setStatus(SC_NOT_FOUND);
		return new ModelAndView(viewName);
	}
}
