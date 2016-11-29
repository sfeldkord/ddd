package cqrs.command;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.googlecode.gentyref.GenericTypeReflector;

import cqrs.common.Command;
import lombok.NonNull;

//TODO ggf. nicht über Generics lösen sondern eine abstract Methode getParameterType
public abstract class AbstractCommandHandler<C extends Command> implements Function<C, Effect>, ApplicationContextAware {

	@Override
	public abstract Effect apply(C t) throws RuntimeException;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		applicationContext.getBean(CommandBus.class).register(getParameterType(), this);
	}

	@SuppressWarnings("unchecked")
	private Class<C> getParameterType() {
		Type myType = getClass();

		//TODO Koennen wir den Generic-Type- auch schoener ermitteln (ohne unchecked), ggf. sogar ohne Drittbibliothek?
		// get the parameterized type, recursively resolving type parameters
		Type baseType = GenericTypeReflector.getExactSuperType(myType, AbstractCommandHandler.class);

		if (baseType instanceof Class<?>) {
			// raw class, type parameters not known ...
			throw new RuntimeException("broken command handler");
		} else {
			ParameterizedType pBaseType = (ParameterizedType) baseType;
			assert pBaseType.getRawType() == AbstractCommandHandler.class; // always true
			return (Class<C>) pBaseType.getActualTypeArguments()[0];
		}
	}
	
}
