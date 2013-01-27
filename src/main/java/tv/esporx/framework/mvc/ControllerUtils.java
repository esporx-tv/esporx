package tv.esporx.framework.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ControllerUtils {

	public static ModelAndView notFound(final HttpServletResponse response, final String viewName) {
		response.setStatus(SC_NOT_FOUND);
		return new ModelAndView(viewName);
	}
}
