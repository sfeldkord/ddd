package cqrs.example.foobank.command;

import org.springframework.stereotype.Component;

import cqrs.command.AbstractCommandHandler;
import cqrs.command.Effect;
import cqrs.example.foobank.event.DepositedEvent;

@Component
public class DepositionHandler extends AbstractCommandHandler<DepositCommand> {

	@Override
	public Effect apply(DepositCommand c) {
		return Effect.of(new DepositedEvent(c.getId(), c.getAmount()));
	}
}
