package tv.esporx.framework;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ReflectionUtils;

public class EntityConverter<E> implements GenericConverter {

	private final Object repository;

	private final Class<E> entityClass;

	public EntityConverter(final Object repository, final Class<E> entityClass) {
		this.repository = repository;
		this.entityClass = entityClass;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, entityClass));
	}

	@Override
	public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
		String idString = (String) source;
		long id = Long.valueOf(idString);
		Method method = ReflectionUtils.findMethod(repository.getClass(), "findById", long.class);
		return ReflectionUtils.invokeMethod(method, repository, id);
	}

}
