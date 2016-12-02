package cqrs.example.foobank.command;

import java.util.UUID;

import cqrs.command.Command;
import lombok.Value;

@Value
public class CreditNotificationCommand implements Command {
	UUID reciever;
	int amount;
}