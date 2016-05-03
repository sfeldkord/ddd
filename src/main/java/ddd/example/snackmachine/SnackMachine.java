package ddd.example.snackmachine;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import ddd.common.AbstractEntity;
import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;
import ddd.example.shared.Euros;
import ddd.example.shared.InvalidOperationException;

//3 Slots of Products, each has a position/name, price, (capacity) and amount
//1 Bag/Counter per Coin/Note in Machine (Money)
//another such set for inserted Money in transaction
//-insert
//-buy
//-return
@SuppressWarnings("serial")
public class SnackMachine extends AbstractEntity<Long> {

	private final List<Slot> slots = Arrays.asList(new Slot(this, 1), new Slot(this, 2), new Slot(this, 3));
	
	private Euros moneyInside = Euros.NONE;
	
	private BigDecimal moneyInTransaction = BigDecimal.ZERO;
	
	public List<SnackPile> getAllSnackPiles() {
		return slots.stream().map(slot -> slot.getSnackPile()).collect(Collectors.toList());
	}

	public SnackPile getSnackPile(int position) {
		return getSlot(position).getSnackPile();
	}

	private Slot getSlot(int position) {
		if (position < 1 || position > slots.size()) {
			throw new IllegalArgumentException("Unknown slot!");
		}

		return slots.get(position - 1);
	}
	
	public void insert(Euros coin) {
		if (!Euros.UNITS.contains(coin)) {
			throw new IllegalArgumentException();
		}

		moneyInTransaction = moneyInTransaction.add(coin.amount());
        moneyInside = moneyInside.add(coin);
	}
	
	public Euros returnMoney() {
        Euros moneyToReturn;
		try {
			moneyToReturn = moneyInside.allocate(moneyInTransaction);
		} catch (Exception e) {
			throw new IllegalStateException();
		}
        moneyInside = moneyInside.subtract(moneyToReturn);
        moneyInTransaction = BigDecimal.ZERO;
        return moneyToReturn;
	}
	
	public void buySnack(int position) throws NotEnoughMoneyException, NoSuitableChangeException, SlotIsEmptyException {
		Slot slot = getSlot(position);
		SnackPile snackPile = getSnackPile(position);

		if (snackPile.quantity == 0) {
			throw new SlotIsEmptyException();
		}
		
		if (snackPile.price.amount().compareTo(moneyInTransaction) > 0) {
			throw new NotEnoughMoneyException();
		}
			
		Euros change = moneyInside.allocate(moneyInTransaction.subtract(snackPile.price.amount()));
		slot.setSnackPile(slot.getSnackPile().removeOne());
		moneyInside = moneyInside.subtract(change);
		moneyInTransaction = BigDecimal.ZERO;
	}

	public void loadSnacks(int position, SnackPile snackPile) {
		Slot slot = getSlot(position);
		slot.setSnackPile(snackPile);
	}

	public void loadMoney(Euros money) {
		moneyInside = moneyInside.add(money);
	}

	public Euros unloadMoney() {
		if (moneyInTransaction.compareTo(BigDecimal.ZERO) > 0) {
			throw new InvalidOperationException("Snack machine is busy!");
		}
		Euros money = moneyInside;
		moneyInside = Euros.NONE;
		return money;
    }
	
	public Euros getMoneyInside() {
		return moneyInside;
	}

	public BigDecimal getMoneyInTransaction() {
		return moneyInTransaction;
	}
	
}