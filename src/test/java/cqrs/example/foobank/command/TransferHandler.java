package cqrs.example.foobank.command;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cqrs.command.AbstractCommandHandler;
import cqrs.command.Effect;
import cqrs.example.foobank.AccountRepository;
import cqrs.example.foobank.event.TransferReceivedEvent;
import cqrs.example.foobank.event.TransferSentEvent;
import cqrs.example.foobank.view.KnownAccountsView;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferHandler extends AbstractCommandHandler<TransferCommand> {

    final AccountRepository repo;

    final KnownAccountsView knownAccounts;

    @Override
    public Effect apply(TransferCommand t) throws RuntimeException {

        // Criteria 1
        if (!exists(t.getFrom()) || !exists(t.getTo()))
            throw new AccountUnknownException();

        // Criteria 2
        if (repo.find(t.getFrom()).getBalance() < t.getAmount())
            throw new UnfundedTransferException();

        return Effect.of(//
                new TransferSentEvent(t.getFrom(), t.getAmount()), //
                new TransferReceivedEvent(t.getTo(), t.getAmount()), //
                new CreditNotificationCommand(t.getTo(), t.getAmount()));
    }

    private boolean exists(UUID id) {
        return knownAccounts.exists(id);
    }

}
