package cqrs.example.foobank.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cqrs.configuration.CqrsConfig;
import cqrs.example.foobank.Account;
import cqrs.example.foobank.AccountRepository;
import cqrs.example.foobank.ApplicationFacade;
import cqrs.example.foobank.FooBankConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { CqrsConfig.class, FooBankConfig.class })
@DirtiesContext
public class FooBank1Tests {

    @Autowired
    private ApplicationFacade facade;

    @Autowired
    private AccountRepository repo;
    
    @Test
    public void createAccountRoundTrip() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");

        Account uwe = repo.find(uweId);
        Account andreas = repo.find(andreasId);

        assertNotNull(uwe);
        assertNotNull(andreas);
        assertEquals(0, uwe.getBalance());
        assertEquals(0, andreas.getBalance());
    }

    @Test
    public void depositRoundTrip() {
        UUID uweId = facade.createAccount("uwe", "schaefer");

        facade.deposit(uweId, 100);

        Account uwe = repo.find(uweId);
        assertEquals(100, uwe.getBalance());
    }

    @Test
    public void withdrawRoundTrip() {
        UUID uweId = facade.createAccount("uwe", "schaefer");

        facade.deposit(uweId, 100);
        facade.withdraw(uweId, 80);

        Account uwe = repo.find(uweId);
        assertEquals(20, uwe.getBalance());
    }

}
