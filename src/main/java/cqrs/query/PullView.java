package cqrs.query;

import java.util.UUID;

import cqrs.store.EventStore;
import lombok.NonNull;

public abstract class PullView extends AbstractView {

	public PullView(@NonNull EventStore eventStore) {
		super(eventStore);
	}

	public PullView(@NonNull EventStore eventStore, UUID aggregateId) {
		super(eventStore, aggregateId);
	}

	public void pullEvents() {
		eventStore.fetch(getEventFilter(), lastEvent).forEachOrdered(this::accept);
	}

}
