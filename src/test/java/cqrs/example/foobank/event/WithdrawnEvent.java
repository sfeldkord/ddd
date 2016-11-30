package cqrs.example.foobank.event;

import java.util.UUID;

import cqrs.common.Event;
import lombok.Value;

@Value
public class WithdrawnEvent implements Event {
	UUID aggregateId;
	int amount;

}
