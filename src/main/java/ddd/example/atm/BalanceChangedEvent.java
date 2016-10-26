package ddd.example.atm;

import java.math.BigDecimal;

import ddd.common.AbstractDomainEvent;

@SuppressWarnings("serial")
public class BalanceChangedEvent extends AbstractDomainEvent {
	
	public final BigDecimal delta;
	
	public BalanceChangedEvent(BigDecimal delta) {
		this.delta = delta;
	}

}
