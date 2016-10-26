package ddd.example.shared;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import ddd.common.AbstractValueObject;
import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;

@SuppressWarnings("serial")
public class Dollars extends AbstractValueObject implements Money<Dollars> {

	public static final BigDecimal CENTS_IN_DOLLAR = new BigDecimal("100");
	public static final NumberFormat DECIMAL_FORMAT = DecimalFormat.getCurrencyInstance(Locale.US);

	public static final Dollars NONE		= new Dollars(0, 0, 0, 0, 0, 0);
	public static final Dollars PENNTY		= new Dollars(1, 0, 0, 0, 0, 0);
	public static final Dollars NICKEL		= new Dollars(0, 1, 0, 0, 0, 0);
	public static final Dollars DIME		= new Dollars(0, 0, 1, 0, 0, 0);
	public static final Dollars QUARTER		= new Dollars(0, 0, 0, 1, 0, 0);
	public static final Dollars HALF_DOLLAR	= new Dollars(0, 0, 0, 0, 1, 0);
	public static final Dollars BUCK		= new Dollars(0, 0, 0, 0, 0, 1);

	public static final List<Dollars> UNITS = Arrays.asList(PENNTY, NICKEL, DIME, QUARTER, HALF_DOLLAR, BUCK);
	
	public int amountOneCentCoins;
	public int amountFiveCentCoins;
	public int amountTenCentCoins;
	public int amountTwentyfiveCentCoins;
	public int amountFiftyCentCoins;
	public int amountOneDollarCoins;

	public Dollars(int amountOneCent, 
			int amountFiveCent,
			int amountTenCent, 
			int amountTwentyfiveCent,
			int amountFiftyCent,
			int amountOneDollar) {
		super();
		if (amountOneCent < 0
				|| amountFiveCent < 0
				|| amountTenCent < 0
				|| amountTwentyfiveCent < 0
				|| amountFiftyCent < 0
				|| amountOneDollar < 0) {
			throw new InvalidOperationException("Negative amount!");
		}
		this.amountOneCentCoins = amountOneCent;
		this.amountFiveCentCoins = amountFiveCent;
		this.amountTenCentCoins = amountTenCent;
		this.amountTwentyfiveCentCoins = amountTwentyfiveCent;
		this.amountFiftyCentCoins = amountFiftyCent;
		this.amountOneDollarCoins = amountOneDollar;
	}

	@Override
	public List<Dollars> units() {
		return UNITS;
	}

	@Override
	public Dollars none() {
		return NONE;
	}

	@Override
	public Dollars add(Dollars money) {
		return new Dollars(amountOneCentCoins + money.amountOneCentCoins,
				amountFiveCentCoins + money.amountFiveCentCoins,
				amountTenCentCoins + money.amountTenCentCoins,
				amountTwentyfiveCentCoins + money.amountTwentyfiveCentCoins,
				amountFiftyCentCoins + money.amountFiftyCentCoins,
				amountOneDollarCoins + money.amountOneDollarCoins);
	}

	@Override
	public Dollars subtract(Dollars money) {
		return new Dollars(amountOneCentCoins - money.amountOneCentCoins,
				amountFiveCentCoins - money.amountFiveCentCoins,
				amountTenCentCoins - money.amountTenCentCoins,
				amountTwentyfiveCentCoins - money.amountTwentyfiveCentCoins,
				amountFiftyCentCoins - money.amountFiftyCentCoins,
				amountOneDollarCoins - money.amountOneDollarCoins);
	}

	@Override
	public Dollars allocate(BigDecimal amount) throws NotEnoughMoneyException, NoSuitableChangeException {
		if (amount == null ||
				amount.compareTo(BigDecimal.ZERO) < 0 ||
				amount.multiply(CENTS_IN_DOLLAR).remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
			throw new IllegalArgumentException();
		}
		
		if (amount().compareTo(amount) < 0) {
			throw new NotEnoughMoneyException();
		}
		
		int remainingAmountInCent = amount.multiply(CENTS_IN_DOLLAR).intValue();

		int oneDollarCount = Math.min((int) (remainingAmountInCent / 100), amountOneDollarCoins);
		remainingAmountInCent = remainingAmountInCent - oneDollarCount * 100;
		
		int fiftyCentCount = Math.min((int) (remainingAmountInCent / 50), amountFiftyCentCoins);
		remainingAmountInCent = remainingAmountInCent - fiftyCentCount * 50;

		int twentyfiveCentCount = Math.min((int) (remainingAmountInCent / 25), amountTwentyfiveCentCoins);
		remainingAmountInCent = remainingAmountInCent - twentyfiveCentCount * 25;

		int tenCentCount = Math.min((int) (remainingAmountInCent / 10), amountTenCentCoins);
		remainingAmountInCent = remainingAmountInCent - tenCentCount * 10;

		int fiveCentCount = Math.min((int) (remainingAmountInCent / 5), amountFiveCentCoins);
		remainingAmountInCent = remainingAmountInCent - fiveCentCount * 5;
		
		int oneCentCount = Math.min(remainingAmountInCent, amountOneCentCoins);
		remainingAmountInCent = remainingAmountInCent - oneCentCount;

		if (remainingAmountInCent == 0) {
			return new Dollars(oneCentCount, fiveCentCount, tenCentCount, twentyfiveCentCount, fiftyCentCount,
				oneDollarCount);
		} else {
			throw new NoSuitableChangeException();
		}
	}

	private int amountInCent() {
		return amountOneCentCoins
				+ amountFiveCentCoins * 5
				+ amountTenCentCoins * 10
				+ amountTwentyfiveCentCoins * 25
				+ amountFiftyCentCoins * 50
				+ amountOneDollarCoins * 100;
	}

	@Override
	public BigDecimal amount() {
		return new BigDecimal(amountInCent()).divide(CENTS_IN_DOLLAR);
	}

	@Override
	public String toString() {
		return DECIMAL_FORMAT.format(amount());
	}

}
