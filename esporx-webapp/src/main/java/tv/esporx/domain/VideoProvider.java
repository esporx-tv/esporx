package tv.esporx.domain;

import static java.util.regex.Pattern.compile;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Supplies a video representation (usually HTML code) against a URI, so long as
 * the URI matches the registered pattern.
 */
@Entity
@Table(name = "video_providers")
@NamedQueries({ @NamedQuery(name = "VideoProvider.findAll", query = "FROM VideoProvider") })
public class VideoProvider {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@NotBlank
	@Column(name = "pattern", unique = true)
	private String pattern;
	@NotBlank
	@Column(name = "template")
	private String template;

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

	public String getPattern() {
		return pattern;
	}

	public String getTemplate() {
		return template;
	}

	public String getContents(final String url) {
		Pattern compiledPattern = compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);
		if (matcher.find()) {
			String videoId = matcher.group(1);
			return template.replace("{ID}", videoId);
		}
		throw new IllegalArgumentException("No contents found for " + url);
	}

	public long getId() {
		return id;
	}

}
