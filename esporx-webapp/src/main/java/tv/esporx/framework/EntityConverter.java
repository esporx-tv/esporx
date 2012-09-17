package tv.esporx.framework;

import static java.util.Collections.singleton;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

public class EntityConverter<E> implements GenericConverter {

    private final Object repository;

    private final Class<E> entityClass;

    private final String methodName;

    private final Class<?> singleParameterClass;

    public EntityConverter(final Object repository, final Class<E> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
        methodName = "findById";
        singleParameterClass = long.class;
    }

    public EntityConverter(final Object repository, final Class<E> entityClass, final String methodName, final Class<?> singleParameterClass) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.methodName = methodName;
        this.singleParameterClass = singleParameterClass;
    }

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return singleton(new ConvertiblePair(String.class, entityClass));
	}

	@Override
	public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
		Object result = null;
		try {
			Object param = singleParameterClass.cast((String) source);
			Method method = findMethod(repository.getClass(), methodName, singleParameterClass);
			result = invokeMethod(method, repository, param);
		}
		catch (RuntimeException nfe) {
		}
		return result;
	}

}
