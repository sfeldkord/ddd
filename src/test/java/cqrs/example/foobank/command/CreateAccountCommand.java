package cqrs.example.foobank.command;

import java.util.UUID;

import cqrs.command.Command;
import lombok.Value;

@Value
public class CreateAccountCommand implements Command {
	UUID id;
	String first;
	String last;
}