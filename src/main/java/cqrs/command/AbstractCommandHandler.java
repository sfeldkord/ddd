package cqrs.command;

import java.util.function.Function;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;

import lombok.NonNull;

public abstract class AbstractCommandHandler<C extends Command> implements Function<C, Effect>, ApplicationContextAware {

	@Override
	public abstract Effect apply(C t) throws RuntimeException;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		applicationContext.getBean(SimpleCommandBus.class).register(getCommandType(), this);
	}

	@SuppressWarnings("unchecked")
	private Class<C> getCommandType() {
		return (Class<C>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractCommandHandler.class);
	}
	
}
