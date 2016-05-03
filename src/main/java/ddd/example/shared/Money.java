package ddd.example.shared;

import java.math.BigDecimal;
import java.util.List;

import ddd.example.atm.NoSuitableChangeException;
import ddd.example.atm.NotEnoughMoneyException;

public interface Money<CUR> {

	List<CUR> units();
	
	CUR none();
	
	CUR add(CUR money);

	CUR subtract(CUR money) throws NotEnoughMoneyException;

	CUR allocate(BigDecimal amountInCent) throws NotEnoughMoneyException, NoSuitableChangeException;

	BigDecimal amount();

}