package ddd.example.mgmt;

import java.math.BigDecimal;

import ddd.common.AbstractAggregateRoot;
import ddd.example.atm.Atm;
import ddd.example.shared.Euros;
import ddd.example.snackmachine.SnackMachine;

@SuppressWarnings("serial")
public class HeadOffice extends AbstractAggregateRoot<Long> {

	public static final HeadOffice INSTANCE = new HeadOffice(1L);
	
	private transient HeadOfficeRepository headOfficeRepo;
	
	private BigDecimal balance = BigDecimal.ZERO;
	private Euros cash = Euros.NONE;

	private HeadOffice() {
		super();
	}
	
	private HeadOffice(Long id) {
		this();
		setId(id);
		headOfficeRepo.findById(id);
	}
	
	public void changeBalance(BigDecimal delta) {
		balance = balance.add(delta);
	}

	public void unloadCashFromSnackMachine(SnackMachine snackMachine) {
		Euros money = snackMachine.unloadMoney();
		cash = cash.add(money);
	}

	public void loadCashToAtm(Atm atm) {
		atm.load(cash);
		cash = Euros.NONE;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Euros getCash() {
		return cash;
	}

}
