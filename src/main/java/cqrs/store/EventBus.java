package cqrs.store;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cqrs.common.Event;

public interface EventBus {

	void publish(List<Event> events);
	
	void subscribe(Consumer<Event> subscriber, Predicate<Event> eventFilter);
	
	void unsubscribe(Consumer<Event> subscriber);
	
}
