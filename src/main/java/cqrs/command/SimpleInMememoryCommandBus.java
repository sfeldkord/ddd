package cqrs.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cqrs.store.EventBus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Used to post (sync or async) a command. Posting a command means to find the
 * appropriate Command Handler and passing it along, recoding its effects, if it
 * was not rejected.
 */
@Slf4j
@Service
public class SimpleInMememoryCommandBus implements CommandBus {//TODO extract interface
	
	private final EventBus eventBus;
	
	private final Map<Class<?>, AbstractCommandHandler<?>> handlers = new LinkedHashMap<>();
	
	//TODO http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
	//Hier wichtig: 1 Thread der Commands behandelt - bewusstes Bottleneck, da wir ja von mehr Lese als Schreib-Operationen ausgehen
	//Für Tests: Executors.newSingleThreadExecutor()
	//Ggf. in die CqrsConfig auslagern und injezieren
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	@Autowired
	public SimpleInMememoryCommandBus(@NonNull EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public <C extends Command> void register(@NonNull Class<C> commandType, @NonNull AbstractCommandHandler<C> commandHandler) {
		log.info("Registration of {} for type {}", commandHandler.getClass().getSimpleName(), commandType.getSimpleName());
		handlers.put(commandType, commandHandler);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized <C extends Command> void post(@NonNull C command) throws RuntimeException {
		AbstractCommandHandler<C> handler = (AbstractCommandHandler<C>) handlers.get(command.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("No handler for " + command.getClass().getSimpleName());
		}

		Effect effects = handler.apply(command); // can throw RTEs	//TODO hinterfragen, ggf. generische CommandRejectedException
		publish(effects);
	}

	// should be transactional from here	//TODO ist das so sicher
	private void publish(@NonNull Effect effect) {
		effect.getCommands().forEach(this::postAsync);
		eventBus.publish(effect.getEvents().collect(Collectors.toList()));
	}

//	@Async //async durch explizten executor aufruf, alternativ wäre diese Annotation mit selbst-Ref-Aufruf
	private void postAsync(Command command) {
		try {
			//TODO potentieller Knackpunkt bei Last?
			executor.execute(() -> post(command));
		} catch (RuntimeException exception) {
			log.error("Could not apply {}, due to {}", command, exception.getMessage(), exception);//TODO passt das letzte Argument?
		}
	}

}
