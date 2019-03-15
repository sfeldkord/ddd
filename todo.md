- Persistenter Event-Store auf Basis von
	-- JPA (ggf. mit PostgreSQL JSON-Serialisierung?)
	-- Document-Store
	
- Queue-Backed CommandBus, persistent

- Beispiele/Tests mit Entity/Aggregate Root CommandHandler und/oder View

- Das eine Bild, das alles erklärt ...

- AbstractEventSourcedAggregateRoot/Entity auf Basis von (Stored)Events
- AbstractStateBasedAggregateRoot/Entity auf Basis von JPA
	- je mit equals und hashcode auf Basis einer fachlichen (oder techn.?) id,  UUID vs. long -> ggf. zu String abstrahieren
		-> pragmatisch für jarvis mit long
	- ggf. interne protected detaults für toString, equals und hashCode -> Java 9+
	- version/lockno!
	- ggf. Auditing (bei eventSource aus events ableiten, im (stored)event vermerken analog jarvis)

- Packages und DDD-Bloat aufräumen

- Konfigurierbar machen (mit Autoconfig?)

- @NonNullApi von Spring?

- Beispiel aus AOK/Barmer mit Kern-Domäne Bonusprogramm (Scheckheft, Maßnahme, Teilnehmer, Programm, ...)
	 und unterstützende bzw. generischen Domänen Prämien, Bestellungen und Konto, Adresse
	 Fragen dazu: Ist ein Bonuskonto Teil der Kerndomäne?
	 
- RestBucks Beispiel

- CommandHandler-Annotation analog EventConsumer, so dass diese ähnlich zur AbstractView gebündelt werden können