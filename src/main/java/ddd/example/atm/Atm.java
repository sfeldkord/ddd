package ddd.example.atm;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ddd.common.AbstractAggregateRoot;
import ddd.example.shared.Euros;

@SuppressWarnings("serial")
public class Atm extends AbstractAggregateRoot<Long> {

	public static final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");
	public static final BigDecimal minimumCommission = Euros.ONE_CENT.amount();
	public static final RoundingMode ROUNDING_MODE = RoundingMode.UP;

	public Euros moneyInside = Euros.NONE;// { get; protected set; } = None;
	public BigDecimal moneyCharged = BigDecimal.ZERO;// { get; protected set; }

	//TODO NoSuitableChangeException?!
	public void withdraw(BigDecimal amount) throws NotEnoughMoneyException, NoSuitableChangeException {
		Euros output = moneyInside.allocate(amount);
		moneyInside = moneyInside.subtract(output);

		BigDecimal commission = caluculateCommission(amount);
		BigDecimal amountWithCommission = amount.add(commission);
		moneyCharged = moneyCharged.add(amountWithCommission);

		addEvent(new BalanceChangedEvent(amountWithCommission));
	}

	private BigDecimal caluculateCommission(BigDecimal amount) {
		return amount.multiply(COMMISSION_RATE).setScale(2, ROUNDING_MODE);
	}

	public void load(Euros money) {
		moneyInside = moneyInside.add(money);
	}
	
}
