package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.esporx.repositories.VideoProviderRepository;

@Controller
@RequestMapping("/video")
public class VideoController {

	private final VideoProviderRepository videoProviderRepository;

	@Autowired
    public VideoController(VideoProviderRepository videoProviderRepository) {
        this.videoProviderRepository = videoProviderRepository;
    }

	@RequestMapping("/matchProvider")
	@ResponseBody
	public String matchProvider(@RequestParam final String url) {
		return "" + videoProviderRepository.isValid(url);
	}
}
