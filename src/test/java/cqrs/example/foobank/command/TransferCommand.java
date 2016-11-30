package cqrs.example.foobank.command;

import java.util.UUID;

import cqrs.common.Command;
import lombok.Value;

@Value
public class TransferCommand implements Command {
    UUID from;

    UUID to;

    int amount;
}
