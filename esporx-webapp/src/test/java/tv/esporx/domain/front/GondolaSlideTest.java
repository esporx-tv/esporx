package tv.esporx.domain.front;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class GondolaSlideTest {

	private GondolaSlide gondolaSlide;

	@Before
	public void setup() {
		gondolaSlide = new GondolaSlide();
	}

	@Test
	public void when_assigning_date_then_is_retrieved() {
		Date date = new Date();
		gondolaSlide.setDate(date);
		assertThat(gondolaSlide.getDate()).isEqualTo(date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_invalid_link_then_throws_exception() {
		gondolaSlide.setLink("tata");
	}

	@Test
	public void when_assigning_lang_then_is_retrieved() {
		String lang = "FR";
		gondolaSlide.setLanguage(lang);
		assertThat(gondolaSlide.getLanguage()).isEqualTo(lang);
	}

	@Test
	public void when_assigning_link_then_is_retrieved() {
		String link = "http://youtube.com";
		gondolaSlide.setLink(link);
		assertThat(gondolaSlide.getLink()).isEqualTo(link);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_null_description_then_throws_exception() {
		gondolaSlide.setDescription(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_null_tag_line_then_throws_exception() {
		gondolaSlide.setTagLine(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_null_title_then_throws_exception() {
		gondolaSlide.setTitle(null);
	}

	@Test
	public void when_assigning_prize_then_is_retrieved() {
		String prize = "1M€";
		gondolaSlide.setPrize(prize);
		assertThat(gondolaSlide.getPrize()).isEqualTo(prize);
	}

	@Test
	public void when_assigning_valid_description_then_is_retrieved() {
		String description = "This is a description";
		gondolaSlide.setDescription(description);
		assertThat(gondolaSlide.getDescription()).isEqualTo(description);
	}

	@Test
	public void when_assigning_valid_picture_then_is_retrieved() {
		gondolaSlide.setPicture("toto.png");
		assertThat(gondolaSlide.getPicture()).isEqualTo("toto.png");
	}

	@Test
	public void when_assigning_valid_tag_line_then_is_retrieved() {
		String tagLine = "This is a tag line";
		gondolaSlide.setTagLine(tagLine);
		assertThat(gondolaSlide.getTagLine()).isEqualTo(tagLine);
	}

	@Test
	public void when_assigning_valid_title_then_is_retrieved() {
		String title = "This is a gondola slide";
		gondolaSlide.setTitle(title);
		assertThat(gondolaSlide.getTitle()).isEqualTo(title);
	}

	@Test
	public void when_instanciating_then_description_is_empty() {
		assertThat(gondolaSlide.getDescription()).isEmpty();
	}

	@Test
	public void when_instanciating_then_lang_is_empty() {
		assertThat(gondolaSlide.getLanguage()).isEmpty();
	}

	@Test
	public void when_instanciating_then_link_is_empty() {
		assertThat(gondolaSlide.getLink()).isEmpty();
	}

	@Test
	public void when_instanciating_then_picture_is_empty() {
		assertThat(gondolaSlide.getPicture()).isEmpty();
	}

	@Test
	public void when_instanciating_then_prize_is_empty() {
		assertThat(gondolaSlide.getPrize()).isEmpty();
	}

	@Test
	public void when_instanciating_then_tag_line_is_empty() {
		assertThat(gondolaSlide.getTagLine()).isEmpty();
	}

	@Test
	public void when_instanciating_then_title_is_empty() {
		assertThat(gondolaSlide.getTitle()).isEmpty();
	}

}
