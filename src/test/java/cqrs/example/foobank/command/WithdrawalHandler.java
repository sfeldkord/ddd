package cqrs.example.foobank.command;

import org.springframework.stereotype.Component;

import cqrs.command.AbstractCommandHandler;
import cqrs.command.Effect;
import cqrs.example.foobank.event.WithdrawnEvent;

@Component
public class WithdrawalHandler extends AbstractCommandHandler<WithdrawCommand> {

	@Override
	public Effect apply(WithdrawCommand c) {
		return Effect.of(new WithdrawnEvent(c.getId(), c.getAmount()));
	}
}
