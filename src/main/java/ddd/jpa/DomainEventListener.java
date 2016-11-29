package ddd.jpa;

import java.util.*;

import javax.persistence.*;

import ddd.common.AbstractAggregateRoot;
import ddd.common.DomainEventHandler;

//Singleton, ggf. add/remove handler?!
//Muss via @EntityListeners an die entität gebunden werden
public class DomainEventListener {

	//Map<DomainEventType, List<DomainEventHandler<DomainEventType>>
	private final List<DomainEventHandler> handlers = new LinkedList<>();

	private DomainEventListener() {
		ServiceLoader.load(DomainEventHandler.class).forEach(event -> handlers.add(event));
	}

	@PrePersist
	@PreUpdate
	@PreRemove
	void dispatchPendingEvents(AbstractAggregateRoot<?> root) {
		root.getPendingEvents().forEach(event -> handlers.forEach(handler -> handler.handle(event)));
		root.clearPendingEvents();
	}

}
