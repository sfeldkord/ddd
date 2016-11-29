package cqrs.example.foobank.event;

import java.util.UUID;

import cqrs.common.Event;
import lombok.Value;

@Value
public class TransferReceivedEvent implements Event {
    UUID aggregateId;
    int amount;
}
