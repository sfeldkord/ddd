package cqrs.query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cqrs.common.Event;
import cqrs.store.EventStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** Abstract base class for all Views. */
@Slf4j
public abstract class AbstractView implements EventHandler {

	//TODO inject?
    protected final EventStore eventStore;

    protected UUID aggregateId = null;

    
	protected final Map<Class<?>, Consumer<Event>> eventConsumers = new HashMap<>();

    protected Event lastEvent;
	
    public AbstractView(EventStore eventStore) {
        this(eventStore, null);
    }

    public AbstractView(@NonNull EventStore eventStore, UUID aggregateId) {
        this.eventStore = eventStore;
        this.aggregateId = aggregateId;
        Collection<Method> methods = Arrays.asList(getClass().getMethods());
        methods.stream()
        	.filter(isEventConsumerMethod())
        	.forEach(addEventConsumer());
    }
    
    @Override
    public void accept(Event event) {
        lastEvent = event;
        log.debug("Applying " + event + " to " + getClass().getSimpleName());
        Consumer<Event> consumer = eventConsumers.get(event.getClass());
        consumer.accept(event);
    }
	
    public UUID getAggregateId() {
    	return aggregateId;
    }
    
    protected Predicate<Event> getEventFilter() {
    	Predicate<Event> eventFilter = event -> eventConsumers.containsKey(event.getClass());
    	if (aggregateId != null) {
    		eventFilter = eventFilter.and(event -> aggregateId.equals(event.getAggregateId()));
    	}
		return eventFilter;
    }
    
    private Predicate<? super Method> isEventConsumerMethod() {
        return method -> (method.getAnnotation(EventConsumer.class) != null)
        		&& (method.getParameterTypes().length == 1)
        		&& (Event.class.isAssignableFrom(method.getParameterTypes()[0]))
        		&& (method.getReturnType().equals(void.class));
    }

    private Consumer<Method> addEventConsumer() {
        return method -> {
        	Class<?> eventType = method.getParameterTypes()[0];
        	eventConsumers.put(eventType, consumeEvent(method));
        };
    }

    private Consumer<Event> consumeEvent(Method eventConsumer) {
    	return event -> {
            try {
                eventConsumer.invoke(this, event);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                log.error("Reflective invocation failed", event);
            }
        };
    }

}
