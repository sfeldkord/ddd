package ddd.common;

public interface DomainEventHandler {

	void handle(DomainEvent event);
	
}
