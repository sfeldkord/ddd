package cqrs.command;

/**
 * A command bus dispatches commands to registered handlers..
 */
public interface CommandBus {//TODO Rename to CommandDispatcher? Or is a command bus rather a special dispatcher allowing registration?

	/** Dispatches the given command to an appropriate handler and passes along any resulting effect, i.e. events or commands. */
	<C extends Command> void post(C command);
	
//	<C extends Command> void post(C command, Consumer<?> callback);//TODO Anwendungsszenarien?, alternative wäre Rückgabe eines Futures
	
	/** Registeres a handler for the type of command. */
	<C extends Command> void register(Class<C> commandType, AbstractCommandHandler<C> commandHandler);

}