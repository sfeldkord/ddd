package cqrs.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cqrs.store.EventStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Used to post (sync or async) a command. Posting a command means to find the
 * appropriate Command Handler and passing it along, recoding its effects, if it
 * was not rejected.
 */
@Slf4j
@Service
public class SimpleCommandBus implements CommandBus {//TODO extract interface
	
	private final EventStore eventStore;
	private final Map<Class<?>, AbstractCommandHandler<?>> commandHandlers = new LinkedHashMap<>();
	
	//TODO http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
	//Hier wichtig: 1 Thread der Commands behandelt - bewusstes Bottleneck, da wir ja von mehr Lese als Schreib-Operationen ausgehen
	//Für Tests: Executors.newSingleThreadExecutor()
	//Ggf. in die CqrsConfig auslagern und injezieren
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	@Autowired
	public SimpleCommandBus(@NonNull EventStore eventStore) {
		this.eventStore = eventStore;
	}

	@Override
	public <C extends Command> void register(@NonNull Class<C> commandType, @NonNull AbstractCommandHandler<C> commandHandler) {
		log.info("Registration of {} for type {}", commandHandler.getClass().getSimpleName(), commandType.getSimpleName());
		commandHandlers.put(commandType, commandHandler);
	}
	
	@Override
	public <C extends Command> void deregister(@NonNull Class<C> commandType) {
		commandHandlers.remove(commandType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized <C extends Command> void post(@NonNull C command) throws RuntimeException {
		AbstractCommandHandler<C> handler = (AbstractCommandHandler<C>) commandHandlers.get(command.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("No handler for " + command.getClass().getSimpleName());
		}

		Effect effects = handler.apply(command); // can throw RTEs	//TODO hinterfragen
		publish(effects);
	}

	// should be transactional from here	//TODO ist das so sicher
	private void publish(@NonNull Effect effect) {
		effect.getCommands().forEach(this::postAsync);
		eventStore.publish(effect.getEvents().collect(Collectors.toList()));
	}

//	@Async
	private void postAsync(Command command) {
		//TODO obviously improvable :)
		try {
			executor.execute(() -> post(command));
		} catch (RuntimeException exception) {
			log.error("Could not apply {}, due to {}", command, exception.getMessage(), exception);
		}
	}

}
