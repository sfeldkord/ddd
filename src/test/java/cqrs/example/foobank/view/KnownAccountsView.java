package cqrs.example.foobank.view;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cqrs.example.foobank.event.AccountCreatedEvent;
import cqrs.query.EventConsumer;
import cqrs.query.PullView;
import cqrs.store.EventStore;

@Component
public class KnownAccountsView extends PullView {

    @Autowired
    public KnownAccountsView(EventStore es) {
        super(es);
    }

    final Set<UUID> knownAccounts = new HashSet<>();

    @EventConsumer
    public void handle(AccountCreatedEvent evt) {
        knownAccounts.add(evt.getAggregateId());
    }

    public boolean exists(UUID id) {
        pullEvents();
        return knownAccounts.contains(id);
    }
}
