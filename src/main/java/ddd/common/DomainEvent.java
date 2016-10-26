package ddd.common;

import java.util.Date;

//Marker
//Events belong to the most inner shell in the DDD-Stack
//Events are created by aggregates and published during a commit
//Events are dispatched by the infrastructure to all interested handlers
//Event handlers belong to a subdomain (avoid using between aggregate of the same subdomain to reduce complexity)
//Example atm.ATM.takeMoney() creates BalanceChangedEvent(amount) --dispatch-to--> manager.BalanceChangedEventHandler.handle(event) -> management.HeadOffice.Instance.changeBalance(event.amount)
//Questions:	What if we cannot dispatch to a singleton, i.e. broadcast or determine a single Object
//				What if an Aggregate implements the HandlerInferace for a Domain Event
//				Do we want/need to persist Domain Events?
public interface DomainEvent {

	Date getEventDate();
	//defUser and defTime
}
