package cqrs;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import cqrs.common.Event;
import cqrs.event.PushView;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimpleEventStoreImpl implements EventStore {

	private final List<Event> eventLog = new CopyOnWriteArrayList<>();
	
	private final Map<PushView, Predicate<Event>> subscribers = new IdentityHashMap<>();

	//TODO http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
	//Hier wichtig: Wir sind hier auf der Read-Seite - d.h wir k�nnen mit mehreren Threads arbeiten (im Gegensatz zum CommandBus)
	//F�r Tests: Executors.newSingleThreadExecutor()
	//Ggf. in die CqrsConfig auslagern und injezieren
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	
	@Override
	public void publish(@NonNull List<Event> events) {
		eventLog.addAll(events);
		notifySubcribers(events);
	}

	//Ermittle zu jedem Event die Interessenten
	//Das sollte besser gehen, warum muss ich hier den Test durchf�hren, ob er interessiert ist, warum passiert dass nicht dirct in accept des Consumers?
//	@Async
	private void notifySubcribers(List<Event> events) {
		executor.execute(() -> {
			events.forEach(event ->
				subscribers.entrySet().forEach(eventConsumer -> {
					if (eventConsumer.getValue().test(event))
						eventConsumer.getKey().accept(event);
				}));
			});
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
	
	//Ignoriert alle Events bis einschlie�lich des lastEvents
	//TODO Richtig bl�d ist, dass der Filter auf einen au�erhalb liegenden Variable (seekMode) zugreift - das kann man sicher optimieren
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
	public void subscribe(@NonNull PushView subscriber, @NonNull Predicate<Event> eventFilter) {
		subscribers.put(subscriber, eventFilter);
	}
	
}
