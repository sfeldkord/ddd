package cqrs;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import cqrs.common.Event;
import cqrs.event.PushView;

public interface EventStore {

	void publish(List<Event> events);
	
	//FIXME: EventFilter statt Predicate<Class<? extends Event>>
	Stream<Event> fetch(Predicate<Event> eventFilter, Event lastEvent);

	//FIXME: EventFilter statt Predicate<Class<? extends Event>>
	void subscribe(PushView pushView, Predicate<Event> eventFilter);

}
