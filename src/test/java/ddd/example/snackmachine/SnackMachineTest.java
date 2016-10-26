package ddd.example.snackmachine;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;
import ddd.example.shared.Euros;

public class SnackMachineTest {

	private static final SnackPile initialChoclatePile = new SnackPile(Snack.Chocolate, Euros.ONE_EURO, 1);

	private SnackMachine snackMachine = new SnackMachine();

	@Before
	public void setup() {
        snackMachine.loadSnacks(1, initialChoclatePile);
	}
	
	@Test
	public void testThatReturnEmptiesMoneyInTransaction() {
		snackMachine.insert(Euros.ONE_EURO);
		
		Euros returnedMoney = snackMachine.returnMoney();

		assertEquals(Euros.ONE_EURO, returnedMoney);
		assertUnmodified();
	}
	
	@Test
	public void testThatInsertedMoneyGoesIntoTransaction() {
		snackMachine.insert(Euros.ONE_CENT);
		snackMachine.insert(Euros.ONE_EURO);

		assertEquals(new BigDecimal("1.01"), snackMachine.getMoneyInTransaction());
		assertEquals(1, snackMachine.getMoneyInside().getAmountOneCentCoins());
		assertEquals(1, snackMachine.getMoneyInside().getAmountOneEuroCoins());
	}

	@Test
	public void testCannotInsertMoreThanOneCoinAtATime() {
		Euros twoCentCoins = Euros.ONE_CENT.add(Euros.ONE_CENT);

		try {
			snackMachine.insert(twoCentCoins);
		} catch (IllegalArgumentException e) {
			assertUnmodified();
			return;
		}
		fail();
	}

	@Test
	public void testBuySnackTradesInsertedMoneyForASnack() throws Exception {
		snackMachine.insert(Euros.ONE_EURO);

		snackMachine.buySnack(1);

		assertEquals(initialChoclatePile.removeOne(), snackMachine.getSnackPile(1));
		assertEquals(BigDecimal.ZERO, snackMachine.getMoneyInTransaction());
		assertEquals(1, snackMachine.getMoneyInside().getAmountOneEuroCoins());
	}

	@Test
	public void testCannotMakePurchaseWhenSlotIsEmpty() throws Exception {
		try {
			snackMachine.buySnack(2);
		} catch (SlotIsEmptyException e) {
			assertUnmodified();
			return;
		}
		fail();
	}

	@Test
	public void testCannotMakePurchaseIfNotEnoughMoneyInserted() throws Exception {
		try {
			snackMachine.buySnack(1);
		} catch (NotEnoughMoneyException e) {
			assertUnmodified();
			return;
		}
		fail();
	}
	
	@Test
	public void testChangeIsReturnedWithHighestDenominationFirst() {
		snackMachine.loadMoney(Euros.ONE_EURO);
		snackMachine.insert(Euros.FIFTY_CENT);
		snackMachine.insert(Euros.FIFTY_CENT);

		snackMachine.returnMoney();

		assertEquals(BigDecimal.ZERO, snackMachine.getMoneyInTransaction());
		assertEquals(0, snackMachine.getMoneyInside().getAmountOneEuroCoins());
		assertEquals(2, snackMachine.getMoneyInside().getAmountFiftyCentCoins());
	}

	@Test
	public void testChangeIsReturnedAfterPurchase() throws Exception {
		snackMachine.loadMoney(Euros.ONE_EURO);

		snackMachine.insert(Euros.TWO_EURO);
		snackMachine.buySnack(1);

		assertEquals(BigDecimal.ZERO, snackMachine.getMoneyInTransaction());
		assertEquals(Euros.TWO_EURO, snackMachine.getMoneyInside());
		assertEquals(initialChoclatePile.removeOne(), snackMachine.getSnackPile(1));
	}
	
	@Test
	public void testNotEnoughChangeForPurchase() throws Exception {
		snackMachine.insert(Euros.TWO_EURO);

		try {
			snackMachine.buySnack(1);
		} catch (NoSuitableChangeException e) {
			assertEquals(Euros.TWO_EURO.amount(), snackMachine.getMoneyInTransaction());
			assertEquals(Euros.TWO_EURO, snackMachine.getMoneyInside());
			assertEquals(initialChoclatePile, snackMachine.getSnackPile(1));
			return;
		}
		fail();
	}    

	private void assertUnmodified() {
		assertEquals(BigDecimal.ZERO, snackMachine.getMoneyInTransaction());
		assertEquals(Euros.NONE, snackMachine.getMoneyInside());
		assertEquals(initialChoclatePile, snackMachine.getSnackPile(1));
	}

}
