package cqrs.example.foobank.event;

import java.util.UUID;

import cqrs.common.Event;
import lombok.Value;

@Value
public class AccountCreatedEvent implements Event {
	UUID aggregateId;
	String firstName;
	String lastName;
}
