package cqrs.command;

import cqrs.common.Message;

/**
 * Tells someone to do something. Commands can be rejected, if they are not
 * valid or the System is in a state, where this command must not be accepted
 */
public interface Command extends Message {
	//Just a marker interface
	//we may add a timeout and an optional expected aggregateId and version
}
