package ddd.example.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;

import ddd.example.atm.NotEnoughMoneyException;

public class DollarsTest {

    private Dollars nineDollarNinetyOne = new Dollars(1, 2, 3, 4, 5, 6);
    
    @Test
	public void testEqualityAndHashCode() {
    	Dollars anontherNineDollarninetyOne = new Dollars(1, 2, 3, 4, 5, 6);
		assertEquals(nineDollarNinetyOne, anontherNineDollarninetyOne);
		assertEquals(nineDollarNinetyOne.hashCode(), anontherNineDollarninetyOne.hashCode());

	    Dollars twoCentsInOneCoin = new Dollars(0, 1, 0, 0, 0, 0);
	    Dollars twoCentsInTwoCoins = new Dollars(1, 0, 0, 0, 0, 0);
		assertNotEquals(twoCentsInOneCoin, twoCentsInTwoCoins);
		assertNotEquals(twoCentsInOneCoin.hashCode(), twoCentsInTwoCoins.hashCode());
	}
	
    @Test
    public void testAdd() {
        Dollars sum = nineDollarNinetyOne.add(nineDollarNinetyOne);
        assertEquals(2, sum.amountOneCentCoins);
        assertEquals(4, sum.amountFiveCentCoins);
        assertEquals(6, sum.amountTenCentCoins);
        assertEquals(8, sum.amountTwentyfiveCentCoins);
        assertEquals(10, sum.amountFiftyCentCoins);
        assertEquals(12, sum.amountOneDollarCoins);
    }        

    @Test
    public void testSubtract() {
        Dollars delta = nineDollarNinetyOne.subtract(nineDollarNinetyOne);
        assertEquals(0, delta.amountOneCentCoins);
        assertEquals(0, delta.amountFiveCentCoins);
        assertEquals(0, delta.amountTenCentCoins);
        assertEquals(0, delta.amountTwentyfiveCentCoins);
        assertEquals(0, delta.amountFiftyCentCoins);
        assertEquals(0, delta.amountOneDollarCoins);
    }

    @Test
    public void testCannotCreateMoneyWithNegativeValue() {
    	testCannotCreateMoneyWithNegativeValue(-1, 0, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, -1, 0, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, -1, 0, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, -1, 0, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, -1, 0);
    	testCannotCreateMoneyWithNegativeValue(0, 0, 0, 0, 0, -1);
    }

    private void testCannotCreateMoneyWithNegativeValue(
    		int amountOneCent, 
			int amountFiveCent,
			int amountTenCent, 
			int amountTwentyfiveCent,
			int amountFiftyCent,
			int amountOneDollar) {
    	try {
			new Dollars(amountOneCent, amountFiveCent, amountTenCent, amountTwentyfiveCent, amountFiftyCent,
					amountOneDollar);
    	} catch (InvalidOperationException e) {
    		return;
    	}
		fail();
    }

    @Test
    public void testCannotSubtractMoreCoinsThanExist() {
    	testCannotSubtractMoreCoinsThanExist(Dollars.PENNTY);
    	testCannotSubtractMoreCoinsThanExist(Dollars.NICKEL);
    	testCannotSubtractMoreCoinsThanExist(Dollars.DIME);
    	testCannotSubtractMoreCoinsThanExist(Dollars.QUARTER);
    	testCannotSubtractMoreCoinsThanExist(Dollars.HALF_DOLLAR);
    	testCannotSubtractMoreCoinsThanExist(Dollars.BUCK);
    }

    private void testCannotSubtractMoreCoinsThanExist(Dollars moneyToSubtract) {
    	try {
    		Dollars.NONE.subtract(moneyToSubtract);
    	} catch (InvalidOperationException e) {
    		return;
    	}
		fail();
    }

    @Test
    public void testAmount() {
    	assertEquals(new BigDecimal("9.91"), nineDollarNinetyOne.amount());
    }
    
    @Ignore//TODO
    @Test
    public void testAllocate() throws Exception {
    	assertEquals(new Dollars(1, 1, 1, 1, 1, 1), new Dollars(2, 2, 2, 2, 2, 2).allocate(new BigDecimal("1.91")));
    	assertEquals(new Dollars(0, 0, 0, 0, 3, 0), new Dollars(0, 0, 0, 0, 3, 1).allocate(new BigDecimal("0.60")));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testCannotAllocate() throws Exception {
    	new Dollars(0, 1, 0, 0, 0, 0).allocate(new BigDecimal("0.06"));
    }

    
    @Test
    public void testAmountToString() {
    	assertEquals("$1,234.56", new Dollars(123456, 0, 0, 0, 0, 0).toString());
    }

}
