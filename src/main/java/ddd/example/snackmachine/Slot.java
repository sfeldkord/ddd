package ddd.example.snackmachine;

import ddd.common.AbstractEntity;
import lombok.*;


@SuppressWarnings("serial")
@NoArgsConstructor
@Getter
public class Slot extends AbstractEntity<Long> {

	private SnackMachine machine;
	
	public int position;
	
	@Setter
	private SnackPile snackPile;

	public Slot(@NonNull SnackMachine machine, int position) {
		super();
		this.machine = machine;
		this.position = position;
		this.snackPile = SnackPile.EMPTY;
	}

}
