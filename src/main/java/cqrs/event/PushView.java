package cqrs.event;

import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cqrs.EventStore;
import lombok.NonNull;

public abstract class PushView extends AbstractView implements ApplicationContextAware {

	public PushView(@NonNull EventStore eventStore) {
		super(eventStore);
	}

	public PushView(@NonNull EventStore eventStore, UUID aggregateId) {
		super(eventStore, aggregateId);
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		applicationContext.getBean(EventStore.class).subscribe(this, getEventFilter());
	}
	
}
