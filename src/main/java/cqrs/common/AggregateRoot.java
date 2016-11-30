package cqrs.common;

import java.util.UUID;
import java.util.stream.Stream;

public interface AggregateRoot {

	UUID getAggregateId();
	int getVersion();
	
	Stream<Event> getPendingEvents();
	//post, clear Events
	
}
