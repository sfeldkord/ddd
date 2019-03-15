package cqrs.store;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import cqrs.common.Event;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimpleInMemoryEventStore implements EventStore {

	private final List<Event> eventLog = new CopyOnWriteArrayList<>();
	
	private final Map<Consumer<Event>, Predicate<Event>> subscriptions = new IdentityHashMap<>();

	//TODO http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
	//Hier wichtig: Wir sind hier auf der Read-Seite - d.h wir können mit mehreren Threads arbeiten (im Gegensatz zum CommandBus)
	//Für Tests: Executors.newSingleThreadExecutor()
	//Ggf. in die CqrsConfig auslagern und injezieren
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	
	@Override
	public void publish(@NonNull List<Event> events) {
		eventLog.addAll(events);
		notifySubcribers(events);
	}

	// Ermittle zu jedem Event die Interessenten
	// Das sollte besser gehen, warum muss ich hier den Test durchführen, ob er
	// interessiert ist, warum passiert dass nicht dirct in accept des Consumers?
//	@Async
	private void notifySubcribers(List<Event> events) {
		executor.execute(() -> {
			events.forEach(event ->
				subscriptions.entrySet().forEach(eventConsumer -> {
					if (eventConsumer.getValue().test(event))
						eventConsumer.getKey().accept(event);
				}));
			});
	}

	@Override
	public Stream<Event> fetchByAggregateId(@NonNull UUID aggregateId) {
		Predicate<Event> eventFilter = event -> aggregateId.equals(event.getAggregateId());
		return fetch(eventFilter);
	}
	
	@Override
	public Stream<Event> fetchByEventType(@NonNull Class<? extends Event> eventType) {
		Predicate<Event> eventFilter = event -> eventType.equals(event.getClass());
		return fetch(eventFilter);
	}
	
	@Override
	public Stream<Event> fetchByAggregateIdAndEventType(@NonNull UUID aggregateId, @NonNull Class<? extends Event> eventType) {
		Predicate<Event> eventFilter = event -> eventType.equals(event.getClass());
		eventFilter = eventFilter.and(event -> aggregateId.equals(event.getAggregateId()));
		return fetch(eventFilter);
	}
	
	@Override
	public Stream<Event> fetch(@NonNull Predicate<Event> eventFilter) {
		return fetch(eventFilter, null);
	}

	@Override
	public Stream<Event> fetch(@NonNull Predicate<Event> eventFilter, Event lastEvent) {
		Stream<Event> events = eventLog.stream();
		if (lastEvent != null) {
			events = dropBefore(events, lastEvent);
		}
		events = events.filter(eventFilter);
		return events;
	}
	
	//Ignoriert alle Events bis einschließlich des lastEvents
	//TODO Richtig blöd ist, dass der Filter auf einen außerhalb liegenden Variable found zugreift - das kann man sicher optimieren
	private Stream<Event> dropBefore(@NonNull Stream<Event> events, @NonNull Event lastEvent) {
		AtomicBoolean found = new AtomicBoolean(false);
		events = events.filter(event -> !found.compareAndSet(false, event == lastEvent));//Skip until found
		return events;
	}

	@PreDestroy
	public void cleanup() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
			log.info("Shutdown complete");
		} catch (InterruptedException e) {
			log.error("Shutdown failed ", e);
		}
	}
	
	@Override
	public void subscribe(@NonNull Consumer<Event> subscriber, @NonNull Predicate<Event> eventFilter) {
		subscriptions.put(subscriber, eventFilter);
	}
	
	@Override
	public void unsubscribe(@NonNull Consumer<Event> subscriber) {
		subscriptions.remove(subscriber);
	}
	
}
