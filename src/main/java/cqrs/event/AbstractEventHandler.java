package cqrs.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cqrs.common.Event;
import cqrs.common.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractEventHandler implements EventHandler {

	//FIXME
	//EventType -> Consumer (handle-Methods)
	//Passt das wirklich?
	protected final Map<Class<?>, Consumer<Event>> eventConsumers = new HashMap<>();

    protected Event lastEvent;
	
    @Override
    public void accept(Event event) {
        lastEvent = event;
        log.debug("Applying " + event + " to " + getClass().getSimpleName());
        Consumer<Event> consumer = eventConsumers.get(event.getClass());
        consumer.accept(event);
    }
	
    //FIXME
    //Alter Return type: Predicate<Class<? extends Event>>
    //Schöner wäre EventFilter
    protected Predicate<Class<? extends Event>> getEventTypeFilter() {
        return consumer -> eventConsumers.containsKey(consumer);
    }

}
