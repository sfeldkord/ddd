package cqrs.query;

import java.util.function.Consumer;

import cqrs.common.Event;

public interface EventHandler extends Consumer<Event> {
	
	@Override void accept(Event event);
	
}
