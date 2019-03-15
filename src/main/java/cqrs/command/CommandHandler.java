package cqrs.command;

import java.util.function.Function;

public interface CommandHandler<C extends Command> extends Function<C, Effect> {

	@Override
	public abstract Effect apply(C t) throws RuntimeException;

	
}
