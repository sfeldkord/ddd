package cqrs.example.foobank.command;

import java.util.UUID;

import cqrs.command.Command;
import lombok.Value;

@Value
public class WithdrawCommand implements Command {
	UUID id;
	int amount;
}