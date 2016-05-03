package ddd.example.snackmachine;

import ddd.common.AbstractValueObject;
import ddd.example.shared.Euros;

@SuppressWarnings("serial")
public class SnackPile extends AbstractValueObject {

	public static final SnackPile EMPTY = new SnackPile(Snack.None, Euros.NONE, 0);
	
	public final Snack snack;
	public final Euros price;
	public final int quantity;
	
	public SnackPile(Snack snack, Euros price, int quantity) {
		super();
		if (snack == null || price == null || quantity < 0) {
			throw new IllegalArgumentException();
		}
		this.snack = snack;
		this.price = price;
		this.quantity = quantity;
	}
	
	public SnackPile removeOne() {
		return new SnackPile(snack, price, quantity - 1);
	}

}
