package tv.esporx.framework.logging;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

//FIXME: I should be executed first!!
public class LoggableProcessor implements BeanFactoryPostProcessor, Ordered {

	private Logger logger = LoggerFactory.getLogger(LoggableProcessor.class);
	private int order;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
		String[] beanDefinitionNames = factory.getBeanDefinitionNames();
		for(String beanDefinition : beanDefinitionNames) {
			Object bean = factory.getBean(beanDefinition);
			Class<? extends Object> beanClass = bean.getClass();
			if(beanClass.isAnnotationPresent(Loggable.class)) {
				try {
					Field declaredField = beanClass.getDeclaredField("logger");
					declaredField.setAccessible(true);
					declaredField.set(bean, getLogger(beanClass));
				} catch (Exception e) {
					logger.trace(e.getMessage());
				}
			}
		}
		
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}

}
