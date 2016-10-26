package ddd.example.shared;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;

@SuppressWarnings("serial")
public class Euros extends AbstractMoney<Euros> implements Money<Euros> {

	public static final BigDecimal CENTS_IN_EURO = new BigDecimal(100);
	public static final NumberFormat DECIMAL_FORMAT = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
	
	public static final Euros NONE			= new Euros(0, 0, 0, 0, 0, 0, 0, 0);
	public static final Euros ONE_CENT		= new Euros(1, 0, 0, 0, 0, 0, 0, 0);
	public static final Euros TWO_CENT		= new Euros(0, 1, 0, 0, 0, 0, 0, 0);
	public static final Euros FIVE_CENT		= new Euros(0, 0, 1, 0, 0, 0, 0, 0);
	public static final Euros TEN_CENT		= new Euros(0, 0, 0, 1, 0, 0, 0, 0);
	public static final Euros TWENTY_CENT	= new Euros(0, 0, 0, 0, 1, 0, 0, 0);
	public static final Euros FIFTY_CENT	= new Euros(0, 0, 0, 0, 0, 1, 0, 0);
	public static final Euros ONE_EURO		= new Euros(0, 0, 0, 0, 0, 0, 1, 0);
	public static final Euros TWO_EURO		= new Euros(0, 0, 0, 0, 0, 0, 0, 1);

	public static final List<Euros> UNITS = Arrays.asList(ONE_CENT, TWO_CENT, FIVE_CENT, TEN_CENT, TWENTY_CENT, FIFTY_CENT, ONE_EURO, TWO_EURO);

	int amountOneCentCoins;
	int amountTwoCentCoins;
	int amountFiveCentCoins;
	int amountTenCentCoins;
	int amountTwentyCentCoins;
	int amountFiftyCentCoins;
	int amountOneEuroCoins;
	int amountTwoEuroCoins;

	protected Euros() {}
	
	protected Euros(int amountOneCent, 
			int amountTwoCent,
			int amountFiveCent,
			int amountTenCent, 
			int amountTwentyCent,
			int amountFiftyCent,
			int amountOneEuro,
			int amountTwoEuro) {
		super();
		if (amountOneCent < 0
				|| amountTwoCent < 0 
				|| amountFiveCent < 0
				|| amountTenCent < 0
				|| amountTwentyCent < 0
				|| amountFiftyCent < 0
				|| amountOneEuro < 0
				|| amountTwoEuro < 0) {
			throw new IllegalArgumentException("Negative amount!");
		}
		this.amountOneCentCoins = amountOneCent;
		this.amountTwoCentCoins = amountTwoCent;
		this.amountFiveCentCoins = amountFiveCent;
		this.amountTenCentCoins = amountTenCent;
		this.amountTwentyCentCoins = amountTwentyCent;
		this.amountFiftyCentCoins = amountFiftyCent;
		this.amountOneEuroCoins = amountOneEuro;
		this.amountTwoEuroCoins = amountTwoEuro;
	}

	@Override
	public List<Euros> units() {
		return UNITS;
	}
	
	@Override
	public Euros none() {
		return NONE;
	}
	
	@Override
	public Euros add(Euros money) {
		return new Euros(amountOneCentCoins + money.amountOneCentCoins,
				amountTwoCentCoins + money.amountTwoCentCoins,
				amountFiveCentCoins + money.amountFiveCentCoins,
				amountTenCentCoins + money.amountTenCentCoins,
				amountTwentyCentCoins + money.amountTwentyCentCoins,
				amountFiftyCentCoins + money.amountFiftyCentCoins,
				amountOneEuroCoins + money.amountOneEuroCoins,
				amountTwoEuroCoins + money.amountTwoEuroCoins);
	}

	@Override
	public Euros subtract(Euros money) {
		return new Euros(amountOneCentCoins - money.amountOneCentCoins,
				amountTwoCentCoins - money.amountTwoCentCoins,
				amountFiveCentCoins - money.amountFiveCentCoins,
				amountTenCentCoins - money.amountTenCentCoins,
				amountTwentyCentCoins - money.amountTwentyCentCoins,
				amountFiftyCentCoins - money.amountFiftyCentCoins,
				amountOneEuroCoins - money.amountOneEuroCoins,
				amountTwoEuroCoins - money.amountTwoEuroCoins);
	}

	@Override
	public Euros allocate(BigDecimal amount)
			throws NotEnoughMoneyException, NoSuitableChangeException {
		if (amount == null ||
				amount.compareTo(BigDecimal.ZERO) < 0 ||
				amount.multiply(CENTS_IN_EURO).remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
			throw new IllegalArgumentException();
		}
		
		if (amount().compareTo(amount) < 0) {
			throw new NotEnoughMoneyException();
		}

		int amountInCent = amount.multiply(CENTS_IN_EURO).intValue();
		List<Euros> coins = allocate(amountInCent, coinsInDecendingDenomination(), Collections.emptyList());
		Euros result = coins.stream().reduce(Euros.NONE, (a, b) -> a.add(b));

		if (result.amountInCent() == amountInCent) {
			return result;
		} else {
			throw new NoSuitableChangeException();
		}
	}
	
	protected List<Euros> coinsInDecendingDenomination() {
		List<Euros> coinsInDecendingDenomination = new ArrayList<>();
		for (int i = 0; i < amountTwoEuroCoins; i++) {
			coinsInDecendingDenomination.add(Euros.TWO_EURO);
		}
		for (int i = 0; i < amountOneEuroCoins; i++) {
			coinsInDecendingDenomination.add(Euros.ONE_EURO);
		}
		for (int i = 0; i < amountFiftyCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.FIFTY_CENT);
		}
		for (int i = 0; i < amountTwentyCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.TWENTY_CENT);
		}
		for (int i = 0; i < amountTenCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.TEN_CENT);
		}
		for (int i = 0; i < amountFiveCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.FIVE_CENT);
		}
		for (int i = 0; i < amountTwoCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.TWO_CENT);
		}
		for (int i = 0; i < amountOneCentCoins; i++) {
			coinsInDecendingDenomination.add(Euros.ONE_CENT);
		}
		return Collections.unmodifiableList(coinsInDecendingDenomination);
	}

	protected int amountInCent() {
		return amountOneCentCoins
				+ amountTwoCentCoins * 2
				+ amountFiveCentCoins * 5
				+ amountTenCentCoins * 10
				+ amountTwentyCentCoins * 20
				+ amountFiftyCentCoins * 50
				+ amountOneEuroCoins * 100
				+ amountTwoEuroCoins * 200;
	}

	@Override
	public BigDecimal amount() {
		return new BigDecimal(amountInCent()).divide(CENTS_IN_EURO);
	}

	@Override
	public String toString() {
		return DECIMAL_FORMAT.format(amount());
	}

	public int getAmountOneCentCoins() {
		return amountOneCentCoins;
	}

	public int getAmountTwoCentCoins() {
		return amountTwoCentCoins;
	}

	public int getAmountFiveCentCoins() {
		return amountFiveCentCoins;
	}

	public int getAmountTenCentCoins() {
		return amountTenCentCoins;
	}

	public int getAmountTwentyCentCoins() {
		return amountTwentyCentCoins;
	}

	public int getAmountFiftyCentCoins() {
		return amountFiftyCentCoins;
	}

	public int getAmountOneEuroCoins() {
		return amountOneEuroCoins;
	}

	public int getAmountTwoEuroCoins() {
		return amountTwoEuroCoins;
	}
	
}
