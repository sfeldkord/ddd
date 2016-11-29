package cqrs.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

import cqrs.EventStore;
import cqrs.common.Event;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** Abstract base class for all Views. */
@Slf4j
public abstract class AbstractView extends AbstractEventHandler {

    protected final EventStore eventStore;

    protected UUID aggregateId = null;

    public AbstractView(EventStore eventStore) {
        this(eventStore, null);
    }

    public AbstractView(@NonNull EventStore eventStore, UUID aggregateId) {
        this.eventStore = eventStore;
        this.aggregateId = aggregateId;
        Collection<Method> methods = Arrays.asList(getClass().getMethods());
        methods.stream()
        	.filter(isMethodEventConsumer())
        	.forEach(consumer -> {
	            //FIXME -> Eigene Consmer-Klasse, damit klar ist, was passiert
	            Class<?> type = consumer.getParameterTypes()[0];
	            eventConsumers.put(type, event -> {
	                try {
	                    consumer.invoke(this, event);
	                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
	                    log.error("Reflective invocation failed", event);
	                }
	            });
        	});
    }
    
    public UUID getAggregateId() {
    	return aggregateId;
    }
    
    protected Predicate<Event> getEventFilter() {
    	Predicate<Event> eventFilter = e -> getEventTypeFilter().test(e.getClass());
		eventFilter = eventFilter.and(e -> aggregateId == null || aggregateId.equals(e.getAggregateId()));
		return eventFilter;
    }

    private Predicate<? super Method> isMethodEventConsumer() {
        return method -> (method.getAnnotation(EventConsumer.class) != null)
        		&& (method.getParameterTypes().length == 1)
        		&& (Event.class.isAssignableFrom(method.getParameterTypes()[0]))
        		&& (method.getReturnType().equals(void.class));
    }

}
