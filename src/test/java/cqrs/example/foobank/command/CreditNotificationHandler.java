package cqrs.example.foobank.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cqrs.command.AbstractCommandHandler;
import cqrs.command.Effect;
import cqrs.example.foobank.service.CreditNotificationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditNotificationHandler extends AbstractCommandHandler<CreditNotificationCommand> {

    final CreditNotificationService s;

    @Override
    public Effect apply(CreditNotificationCommand t) {
        s.sendEmailTo(t.getReciever(), t.getAmount());
        // probably, we also want to emit an event saying the mail was sent, but
        // that is not the point.
        return Effect.none();
    }

}
