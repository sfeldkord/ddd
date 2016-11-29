package cqrs.example.foobank.command;

import org.springframework.stereotype.Component;

import cqrs.command.AbstractCommandHandler;
import cqrs.command.Effect;
import cqrs.example.foobank.event.AccountCreatedEvent;

@Component
public class CreateAccountHandler extends AbstractCommandHandler<CreateAccountCommand> {

	@Override
	public Effect apply(CreateAccountCommand c) {
		return Effect.of(new AccountCreatedEvent(c.getId(), c.getFirst(), c.getLast()));
	}
}
