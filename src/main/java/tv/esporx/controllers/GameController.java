package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Game;
import tv.esporx.repositories.GameRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

@Controller
public class GameController {

    private static final String GAME_COMMAND = "gameCommand";
    private final GameRepository gameRepository;
    private DomainClassConverter<?> entityConverter;

    @Autowired
    public GameController(GameRepository gameRepository, DomainClassConverter entityConverter){
        this.gameRepository = gameRepository;
        this.entityConverter = entityConverter;
    }

    @RequestMapping(value = "/admin/game/new", method = GET)
    public ModelAndView creation() {
        return populatedGameForm(new ModelAndView()).addObject(GAME_COMMAND, new Game());
    }

    @RequestMapping(value = "/admin/games", method = GET)
    @ResponseBody
    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }

    @RequestMapping(value = "/admin/game/edit/{gameCommand}", method = GET)
    public ModelAndView edition(
            @ModelAttribute(GAME_COMMAND) @PathVariable @Valid final Game gameCommand,
            final HttpServletResponse response,
            final ModelAndView modelAndView) {
        if (gameCommand == null) {
            return notFound(response, "channel/notFound");
        }
        return populatedGameForm(modelAndView);
    }

    @RequestMapping(value = {"/admin/game/new", "/admin/game/edit/{gameCommand}"}, method = POST)
    public ModelAndView save(
            @ModelAttribute(GAME_COMMAND) @Valid final Game gameCommand,
            final BindingResult result,
            final ModelAndView modelAndView) {

        if (result.hasErrors()) {
            return populatedGameForm(modelAndView);
        }
        gameRepository.save(gameCommand);
        return successfulRedirectionView();
    }

    @RequestMapping(value = "/admin/game/remove", method = POST)
    public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
        Game game = gameRepository.findOne(id);
        if (game == null) {
            return notFound(response, "channel/notFound");
        }
        gameRepository.delete(game);
        return successfulRedirectionView();
    }

    @InitBinder(GAME_COMMAND)
    protected void customizeConversions(final WebDataBinder binder) {
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
    }

    private ModelAndView populatedGameForm(final ModelAndView modelAndView) {
        modelAndView.addObject("disableAnalytics", true);
        modelAndView.setViewName("game/form");
        return modelAndView;
    }

    private ModelAndView successfulRedirectionView() {
        return new ModelAndView("redirect:/admin/home?active=game");
    }
}
