package cqrs.command;

//TODO Convert to a message bus handling events, too???
public interface CommandBus {//Extend "CommandSender" (-> functional Interface Consumer?, "EventPublisher"?

	<C extends Command> void register(Class<C> commandType, AbstractCommandHandler<C> commandHandler);
	<C extends Command> void deregister(Class<C> commandType);//???

	<C extends Command> void post(C command);

}