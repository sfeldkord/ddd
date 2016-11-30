package cqrs.example.foobank;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cqrs.command.CommandBus;
import cqrs.example.foobank.command.AccountUnknownException;
import cqrs.example.foobank.command.CreateAccountCommand;
import cqrs.example.foobank.command.DepositCommand;
import cqrs.example.foobank.command.TransferCommand;
import cqrs.example.foobank.command.UnfundedTransferException;
import cqrs.example.foobank.command.WithdrawCommand;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationFacade {

    final CommandBus bus;

    public UUID createAccount(String first, String last) {
        UUID id = UUID.randomUUID();
        bus.post(new CreateAccountCommand(id, first, last));
        return id;
    }

    public void deposit(UUID id, int amount) {
        bus.post(new DepositCommand(id, amount));
    }

    public void withdraw(UUID id, int amount) {
        bus.post(new WithdrawCommand(id, amount));
    }

    public void transfer(UUID sender, UUID reciever, int amount) throws AccountUnknownException,
            UnfundedTransferException {
        bus.post(new TransferCommand(sender, reciever, amount));
    }

}
