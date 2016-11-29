package cqrs.example.foobank.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cqrs.CqrsConfig;
import cqrs.example.foobank.Account;
import cqrs.example.foobank.AccountRepository;
import cqrs.example.foobank.ApplicationFacade;
import cqrs.example.foobank.FooBankConfig;
import cqrs.example.foobank.service.CreditNotificationService;
import cqrs.example.foobank.service.CreditNotificationServiceImpl;
import cqrs.example.foobank.test.FooBank6Tests.MockNotificationService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		MockNotificationService.class,
		CqrsConfig.class,
		FooBankConfig.class })
@DirtiesContext
public class FooBank6Tests {

    @Configuration
    public static class MockNotificationService {
        @Bean
        @Primary
        public CreditNotificationService creditNotificationService() {
            return Mockito.spy(new CreditNotificationServiceImpl());
        }
    }

    @Autowired
    private ApplicationFacade facade;

    @Autowired
    private AccountRepository repo;

    @Autowired
    private CreditNotificationService notification;

    @Test
    public void transfer() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");

        facade.deposit(uweId, 100);
        facade.transfer(uweId, andreasId, 30);
        
		try { Thread.sleep(50); } catch (InterruptedException e) {} //Synchronizer!!! Executor ausfindig machen
        
        Account uwe = repo.find(uweId);
        Account andreas = repo.find(andreasId);

        assertEquals(70, uwe.getBalance());
        assertEquals(30, andreas.getBalance());

        verify(notification).sendEmailTo(andreasId, 30);//FIXME offenbar wird das Command erst konsumiert, wenn dieser Test durch ist Insgesamt instabil!
        verifyNoMoreInteractions(notification);
    }

}
