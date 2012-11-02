package tv.esporx.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GameFilter implements Filter {

	public static final String GAME_PARAMETER_NAME = "currentGame";
	private static final String DEFAULT_GAME = "starcraft2";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {}

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
	public void destroy() {}

}
