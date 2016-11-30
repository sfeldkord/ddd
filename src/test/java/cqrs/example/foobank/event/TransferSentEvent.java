package cqrs.example.foobank.event;

import java.util.UUID;

import cqrs.common.Event;
import lombok.Value;

@Value
public class TransferSentEvent implements Event {
    UUID aggregateId;
    int amount;
}
