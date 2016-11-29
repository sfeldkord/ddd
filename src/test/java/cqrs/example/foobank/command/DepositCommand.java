package cqrs.example.foobank.command;

import java.util.UUID;

import cqrs.common.Command;
import lombok.Value;

@Value
public class DepositCommand implements Command {
	UUID id;
	int amount;
}