package ddd.common;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public abstract class AbstractAggregateRoot<ID extends Serializable> extends AbstractEntity<ID> implements AggregateRoot, Auditable {

	private String createdBy;
	
	private Date createdDate;
	
	private String lastModifiedBy;
	
	private Date lastModifiedDate;
	
	private int version;
	
	private transient final List<DomainEvent> pendingEvents = new LinkedList<DomainEvent>();

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	@Override
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public int getVersion() {
		return version;
	}

	public final List<DomainEvent> getPendingEvents() {
		return Collections.unmodifiableList(pendingEvents);
	}

	//Hier waere ein Friend gut oder der Dispatcher muss im gleichen Package liegen - wird er aber nicht wenn er nicht generisch ist
	public final void clearPendingEvents() {
		pendingEvents.clear();
	}

	protected final void addEvent(DomainEvent event) {
		pendingEvents.add(event);
	}

}
