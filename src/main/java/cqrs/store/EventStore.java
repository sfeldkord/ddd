package cqrs.store;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import cqrs.common.Event;

public interface EventStore extends EventBus {

	Stream<Event> fetchByAggregateId(UUID aggregateId);//add optional lastVersion
	
	Stream<Event> fetchByEventType(Class<? extends Event> eventType);
	
	Stream<Event> fetchByAggregateIdAndEventType(UUID aggregateId, Class<? extends Event> eventType);

	Stream<Event> fetch(Predicate<Event> eventFilter);
	
	Stream<Event> fetch(Predicate<Event> eventFilter, Event lastEvent);
	
}
