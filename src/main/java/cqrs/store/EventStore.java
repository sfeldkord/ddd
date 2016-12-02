package cqrs.store;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import cqrs.common.Event;
import cqrs.query.PushView;

public interface EventStore {

	void publish(List<Event> events);
	
	Stream<Event> fetchByAggregateId(UUID aggregateId);//add optional lastVersion
	
	Stream<Event> fetchByEventType(Class<? extends Event> eventType);
	Stream<Event> fetchByAggregateIdAndEventType(UUID aggregateId, Class<? extends Event> eventType);

	Stream<Event> fetch(Predicate<Event> eventFilter);
	
	//TODO Brauchen wir das? Oder kann der Event-Store das nicht kapseln?
	Stream<Event> fetch(Predicate<Event> eventFilter, Event lastEvent);
	
	void subscribe(PushView pushView, Predicate<Event> eventFilter);
	
	void unsubscribe(PushView pushView);
	
}
