package cqrs.example.foobank.event;

import java.util.UUID;

import cqrs.common.Event;
import lombok.Value;

@Value
public class DepositedEvent implements Event {
	UUID aggregateId;
	int amount;
}
