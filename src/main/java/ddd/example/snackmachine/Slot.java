package ddd.example.snackmachine;

import ddd.common.AbstractEntity;

@SuppressWarnings("serial")
public class Slot extends AbstractEntity<Long> {

	private SnackMachine machine;
	
	public int position;
	
	private SnackPile snackPile;

	public Slot() {
		super();
	}
	
	public Slot(SnackMachine machine, int position) {
		super();
		this.machine = machine;
		this.position = position;
		this.snackPile = SnackPile.EMPTY;
	}

	public SnackMachine getMachine() {
		return machine;
	}
	
	public int getPosition() {
		return position;
	}
	
	public SnackPile getSnackPile() {
		return snackPile;
	}
	
	public void setSnackPile(SnackPile snackPile) {
		this.snackPile = snackPile;
	}
	
}
