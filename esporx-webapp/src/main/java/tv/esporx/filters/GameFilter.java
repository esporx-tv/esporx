package tv.esporx.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GameFilter implements Filter {

	private static final String DEFAULT_GAME = "starcraft2";
	public static final String GAME_PARAMETER_NAME = "currentGame";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String requestedGame = request.getParameter(GAME_PARAMETER_NAME);
		if (session.getAttribute(GAME_PARAMETER_NAME) == null || requestedGame != null) {
			String currentGame = (requestedGame != null) ? requestedGame : DEFAULT_GAME;
			session.setAttribute(GAME_PARAMETER_NAME, currentGame);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
