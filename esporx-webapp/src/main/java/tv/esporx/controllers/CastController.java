package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.dao.impl.GameRepository;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Game;
import tv.esporx.framework.EntityConverter;
import tv.esporx.framework.mvc.RequestUtils;

@Controller
@RequestMapping("/cast")
public class CastController {

	private static final String COMMAND = "castCommand";

	@Autowired
	private PersistenceCapableCast castDao;
	@Autowired
	private PersistenceCapableGame gameDao;

	@Autowired
	private RequestUtils requestHelper;

	@Autowired
	private PersistenceCapableVideoProvider videoProvider;

	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@InitBinder(COMMAND)
	public void customizeConversions(final WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
		
		EntityConverter<Cast> entityConverter = new EntityConverter<Cast>(castDao, Cast.class);
		((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}

	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("cast/notFound");
	}
	

	@RequestMapping(value = "/remove/{id}", method = POST)
	public ModelAndView delete(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0);
		Cast cast = castDao.findById(id);
		if (cast == null) {
			return notFound(response, "cast/notFound");
		}
		castDao.delete(cast);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/watch/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0);
		Cast cast = castDao.findById(id);
		if (cast == null) {
			return notFound(response, "cast/notFound");
		}
		ModelMap model = new ModelMap("cast", cast);
		model.addAttribute("embeddedVideo", videoProvider.getEmbeddedContents(cast.getVideoUrl()));
		return new ModelAndView("cast/index", model);
	}

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView list() {
		List<Cast> cast = castDao.findAll();
		ModelMap model = new ModelMap("casts", cast);
		return new ModelAndView("cast/list", model);
	}

	@RequestMapping(value = { "/new", "edit/{castCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Cast castCommand, final BindingResult result, final HttpServletRequest request, final ModelAndView modelAndView) {
		Game game = gameDao.findByTitle(requestHelper.currentGame(request));
		castCommand.setRelatedGame(game);
		if (result.hasErrors()) {
			return populatedCastForm(modelAndView);
		}
		castDao.saveOrUpdate(castCommand);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation(final ModelAndView modelAndView) {
		return populatedCastForm(modelAndView).addObject(COMMAND, new Cast());
	}

	@RequestMapping(value = "/edit/{castCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final Cast castCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (castCommand == null) {
			return notFound(response, "cast/notFound");
		}
		return populatedCastForm(modelAndView);
	}

	public void setCastRepository(final PersistenceCapableCast castDao) {
		this.castDao = castDao;
	}

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedCastForm(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("cast/form");
		return modelAndView;
	}

	public void setVideoProvider(final PersistenceCapableVideoProvider videoProvider) {
		this.videoProvider = videoProvider;
	}

	public void setGameRepository(final GameRepository gameDao) {
		this.gameDao = gameDao;
	}

}
