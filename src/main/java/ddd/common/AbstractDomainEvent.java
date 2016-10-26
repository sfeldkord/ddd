package ddd.common;

import java.io.Serializable;
import java.util.Date;

//add Auditable if you need to persist (potentially, defUser and defTime only) 
@SuppressWarnings("serial")
public abstract class AbstractDomainEvent extends AbstractValueObject implements DomainEvent, Serializable {

	private Date eventDate;
	
	public AbstractDomainEvent() {
		this.eventDate = new Date();
	}
	
	public AbstractDomainEvent(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	@Override
	public final Date getEventDate() {
		return eventDate;
	}

	protected final void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

}
