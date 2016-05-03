package ddd.example.shared;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;

public class EurosTest {

    private Euros twentySevenEuroSixty = new Euros(1, 2, 3, 4, 5, 6, 7, 8);
    
    @Test
	public void testEqualityAndHashCode() {
    	Euros anotherTwentySevenEuroSixty = new Euros(1, 2, 3, 4, 5, 6, 7, 8);
		assertEquals(twentySevenEuroSixty, anotherTwentySevenEuroSixty);
		assertEquals(twentySevenEuroSixty.hashCode(), anotherTwentySevenEuroSixty.hashCode());

	    Euros twoCentsInOneCoin = new Euros(0, 1, 0, 0, 0, 0, 0, 0);
	    Euros twoCentsInTwoCoins = new Euros(1, 0, 0, 0, 0, 0, 0, 0);
		assertNotEquals(twoCentsInOneCoin, twoCentsInTwoCoins);
		assertNotEquals(twoCentsInOneCoin.hashCode(), twoCentsInTwoCoins.hashCode());
	}
	
    @Test
    public void testAdd() {
        Euros sum = twentySevenEuroSixty.add(twentySevenEuroSixty);
        assertEquals(2, sum.getAmountOneCentCoins());
        assertEquals(4, sum.getAmountTwoCentCoins());
        assertEquals(6, sum.getAmountFiveCentCoins());
        assertEquals(8, sum.getAmountTenCentCoins());
        assertEquals(10, sum.getAmountTwentyCentCoins());
        assertEquals(12, sum.getAmountFiftyCentCoins());
        assertEquals(14, sum.getAmountOneEuroCoins());
        assertEquals(16, sum.getAmountTwoEuroCoins());
    }        

    @Test
    public void testSubtract() {
        Euros delta = twentySevenEuroSixty.subtract(twentySevenEuroSixty);
        assertEquals(0, delta.getAmountOneCentCoins());
        assertEquals(0, delta.getAmountTwoCentCoins());
        assertEquals(0, delta.getAmountFiveCentCoins());
        assertEquals(0, delta.getAmountTenCentCoins());
        assertEquals(0, delta.getAmountTwentyCentCoins());
        assertEquals(0, delta.getAmountFiftyCentCoins());
        assertEquals(0, delta.getAmountOneEuroCoins());
        assertEquals(0, delta.getAmountTwoEuroCoins());
    }

    @Test
    public void testCannotCreateMoneyWithNegativeValue() {
    	testCannotCreateMoneyWithNegativeValue(-1, 0, 0, 0, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, -1, 0, 0, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, -1, 0, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, -1, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, -1, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, 0, -1, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, 0, 0, -1, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, 0, 0, 0, -1);
    }

    private void testCannotCreateMoneyWithNegativeValue(
    		int amountOneCent, 
			int amountTwoCent,
			int amountFiveCent,
			int amountTenCent, 
			int amountTwentyCent,
			int amountFiftyCent,
			int amountOneEuro,
			int amountTwoEuro) {
    	try {
			new Euros(amountOneCent, amountTwoCent, amountFiveCent, amountTenCent, amountTwentyCent, amountFiftyCent,
					amountOneEuro, amountTwoEuro);
    	} catch (IllegalArgumentException e) {
    		return;
    	}
		fail();
    }

    @Test
    public void testCannotSubtractMoreCoinsThanExist() {
    	testCannotSubtractMoreCoinsThanExist(Euros.ONE_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.TWO_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.FIVE_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.TEN_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.TWENTY_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.FIFTY_CENT);
    	testCannotSubtractMoreCoinsThanExist(Euros.ONE_EURO);
    	testCannotSubtractMoreCoinsThanExist(Euros.TWO_EURO);
    }

    private void testCannotSubtractMoreCoinsThanExist(Euros moneyToSubtract) {
    	try {
    		Euros.NONE.subtract(moneyToSubtract);
    	} catch (IllegalArgumentException e) {
    		return;
    	}
		fail();
    }

    @Test
    public void testAmount() {
    	assertEquals(new BigDecimal("27.6"), twentySevenEuroSixty.amount());
    }

    @Test
    public void testAllocate() throws Exception {
    	assertEquals(new Euros(1, 1, 1, 1, 1, 1, 1, 1), new Euros(2, 2, 2, 2, 2, 2, 2, 2).allocate(new BigDecimal("3.88")));
    	assertEquals(new Euros(0, 3, 0, 0, 0, 0, 0, 0), new Euros(0, 3, 1, 0, 0, 0, 0, 0).allocate(new BigDecimal("0.06")));
    	assertEquals(new Euros(0, 0, 0, 0, 3, 0, 0, 0), new Euros(0, 0, 0, 0, 3, 1, 0, 0).allocate(new BigDecimal("0.60")));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testAllocateWithNotEnoughMoney() throws Exception {
    	twentySevenEuroSixty.allocate(new BigDecimal("27.61"));
    }

    @Test(expected = NoSuitableChangeException.class)
    public void testAllocateWithNoSuitableChange() throws Exception {
    	new Euros(0, 3, 0, 0, 0, 0, 0, 0).allocate(new BigDecimal("0.05"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAllocateWithNegativeAmount() throws Exception {
    	Euros.NONE.allocate(new BigDecimal("-0.01"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateWithInvalidAmount() throws Exception {
    	Euros.NONE.allocate(new BigDecimal("0.001"));
    }
    
    @Test
    public void testToString() {
    	assertEquals("1.234,56 \u20ac", new Euros(123456, 0, 0, 0, 0, 0, 0, 0).toString());
    }

}
