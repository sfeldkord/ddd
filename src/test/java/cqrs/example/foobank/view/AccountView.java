package cqrs.example.foobank.view;

import java.util.UUID;

import cqrs.EventStore;
import cqrs.event.EventConsumer;
import cqrs.event.PullView;
import cqrs.example.foobank.Account;
import cqrs.example.foobank.event.AccountCreatedEvent;
import cqrs.example.foobank.event.DepositedEvent;
import cqrs.example.foobank.event.TransferReceivedEvent;
import cqrs.example.foobank.event.TransferSentEvent;
import cqrs.example.foobank.event.WithdrawnEvent;
import lombok.Getter;

public class AccountView extends PullView {

    @Getter
    private Account account;

    public AccountView(EventStore es, UUID aggregateId) {
        super(es, aggregateId);
        pullEvents(); // pull relevant events from the EventStore
    }

    @EventConsumer
    public void handle(AccountCreatedEvent evt) {
        this.account = new Account(evt.getAggregateId(), evt.getFirstName(), evt.getLastName(), 0);
    }

    @EventConsumer
    public void handle(DepositedEvent evt) {
        this.account.credit(evt.getAmount());
    }

    @EventConsumer
    public void handle(WithdrawnEvent evt) {
        this.account.debit(evt.getAmount());
    }

    @EventConsumer
    public void handle(TransferReceivedEvent evt) {
        this.account.credit(evt.getAmount());
    }

    @EventConsumer
    public void handle(TransferSentEvent evt) {
        this.account.debit(evt.getAmount());
    }

}