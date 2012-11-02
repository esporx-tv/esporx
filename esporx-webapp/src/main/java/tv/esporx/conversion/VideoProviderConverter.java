package tv.esporx.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import tv.esporx.domain.VideoProvider;
import tv.esporx.repositories.VideoProviderRepository;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

public class VideoProviderConverter implements GenericConverter {
    private static Logger LOGGER = LoggerFactory.getLogger(VideoProviderConverter.class);
    private final VideoProviderRepository repository;

    public VideoProviderConverter(final VideoProviderRepository repository) {
        this.repository = repository;
    }

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return singleton(new ConvertiblePair(String.class, VideoProvider.class));
	}

	@Override
	public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
		Object result = null;
		try {
            result = repository.findByUrl((String) source);
		}
		catch (RuntimeException nfe) {
            LOGGER.warn(nfe.getMessage(), nfe);
		}
		return result;
	}
}
