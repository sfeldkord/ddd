package ddd.example.atm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import ddd.example.shared.Euros;

public class AtmTest {

	private Atm atm = new Atm();
	
	@Before
	public void setup() {
		atm.load(Euros.TWO_EURO);
	}

	@Test
	public void testWithdrawChargesCommission() throws Exception {
		atm.load(Euros.ONE_EURO);

		atm.withdraw(new BigDecimal("1.00"));

		assertEquals(new BigDecimal("1.01"), atm.moneyCharged);
		assertEquals(Euros.TWO_EURO, atm.moneyInside);
		assertEvents();
	}

	@Test
	public void testCommissionIsAtLeastOneCent() throws Exception {
		atm.load(Euros.ONE_CENT);

		atm.withdraw(new BigDecimal("0.01"));

		assertEquals(new BigDecimal("0.02"), atm.moneyCharged);
		assertEquals(Euros.TWO_EURO, atm.moneyInside);
		assertEvents();
	}

	@Test
	public void testCommissionIsRoundedUpToTheNextCent() throws Exception {
		atm.load(Euros.ONE_EURO.add(Euros.TEN_CENT));

		atm.withdraw(new BigDecimal("1.10"));

		assertEquals(new BigDecimal("1.12"), atm.moneyCharged);
		assertEquals(Euros.TWO_EURO, atm.moneyInside);
		assertEvents();
	}

	@Test
	public void testWithdrawInvalidAmount() throws Exception {
		try {
			atm.withdraw(new BigDecimal("-1.00"));
		} catch (IllegalArgumentException e) {
			assertUnmodified();
			return;
		}
		fail();
	}

	@Test
	public void testWithdrawNotEnoughMoney() throws Exception {
		try {
			atm.withdraw(new BigDecimal("2.01"));
		} catch (NotEnoughMoneyException e) {
			assertUnmodified();
			return;
		}
		fail();
	}

	@Test
	public void testWithdrawNoSuitableChange() throws Exception {
		try {
			atm.withdraw(new BigDecimal("1.00"));
		} catch (NoSuitableChangeException e) {
			assertUnmodified();
			return;
		}
		fail();
	}

	private void assertEvents() {
		assertEquals(1, atm.getEvents().size());
		assertEquals(BalanceChangedEvent.class, atm.getEvents().get(0).getClass());
		BigDecimal chargedAmount = ((BalanceChangedEvent) atm.getEvents().get(0)).delta;
		assertEquals(0, atm.moneyCharged.compareTo(chargedAmount));
	}

	private void assertUnmodified() {
		assertEquals(Euros.TWO_EURO, atm.moneyInside);
		assertEquals(0, atm.moneyCharged.compareTo(BigDecimal.ZERO));
		assertEquals(0, atm.getEvents().size());
	}
}
