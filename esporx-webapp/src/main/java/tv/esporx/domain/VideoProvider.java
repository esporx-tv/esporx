package tv.esporx.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Objects.toStringHelper;
import static java.util.regex.Pattern.compile;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Supplies a video representation (usually HTML code) against a URI, so long as
 * the URI matches the registered pattern.
 */
@Entity
@Table(name = "video_providers")
public class VideoProvider {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;
	@NotBlank
	@Column(name = "pattern", unique = true)
	private String pattern;
	@NotBlank
	@Column(name = "template")
	private String template;
    @Column(name = "endpoint")
    private String endpoint;

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
        String channel = extractChannelName(url);
        if(channel == null) {
            return null;
        }
        return template.replace("{ID}", channel);
	}

	public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public String extractChannelName(String url) {
        Pattern compiledPattern = compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return toStringHelper(this) //
            .add("pattern", pattern) //
            .toString();
    }
}
