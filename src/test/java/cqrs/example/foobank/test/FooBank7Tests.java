package cqrs.example.foobank.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cqrs.CqrsConfig;
import cqrs.example.foobank.ApplicationFacade;
import cqrs.example.foobank.FooBankConfig;
import cqrs.example.foobank.view.GoldCustomersView;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		CqrsConfig.class,
        FooBankConfig.class })
@DirtiesContext
public class FooBank7Tests {

    @Autowired
    private ApplicationFacade facade;

    @Autowired
    private GoldCustomersView gold;

    @Test(timeout = 3000)
    public void goldCustomerReport() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");
        UUID joergId = facade.createAccount("joerg", "adler");
        UUID torstenId = facade.createAccount("torsten", "blum");
        UUID peterId = facade.createAccount("peter", "weber"); // :)

        facade.deposit(uweId, 100000);
        facade.transfer(uweId, andreasId, 30);
        facade.transfer(uweId, joergId, 10000);
        facade.transfer(uweId, torstenId, 20000);
        facade.transfer(uweId, peterId, 500);

        Collection<String> reportNotYetReady = gold.createGoldCustomerReport();
        assertTrue(reportNotYetReady.isEmpty());

        // don't do this at home
        while (gold.createGoldCustomerReport().size() < 2) {
            try {
                // eventually (:D) the report has to catch up
                Thread.sleep(50);
            } catch (InterruptedException e) {
            	//
            }
        }

        Collection<String> reportUpToDate = gold.createGoldCustomerReport();

        assertEquals(2, reportUpToDate.size());
        assertTrue(reportUpToDate.contains("adler, joerg"));
        assertTrue(reportUpToDate.contains("blum, torsten"));

    }

}
