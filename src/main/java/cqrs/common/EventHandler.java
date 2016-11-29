package cqrs.common;

import java.util.function.Consumer;

public interface EventHandler extends Consumer<Event> {
	
	@Override void accept(Event event);
	
}
