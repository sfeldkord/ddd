package cqrs.common;

import java.util.UUID;

/**
 * Expresses a Fact of the past, a thing that has happened and cannot be
 * changed. An event refers to a specific version of an aggregate. //TODO Do we really want to restrict ourselves this way 
 */
public interface Event extends Message {

	/**
	 * @return the ID of the aggregate root this Event belongs to, used for
	 *         filtering.
	 */
	UUID getAggregateId();
	
	/** Version of the aggregate that this event refers to. */
//	int getAggregateVersion();
	
	//timestamp
	
}
