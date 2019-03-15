package cqrs.command;

import java.util.Arrays;
import java.util.stream.Stream;

import cqrs.common.Event;
import cqrs.common.Message;
import lombok.NonNull;

/**
 * An effect is the result of a command execution that gets published. This can
 * be one or more events or commands. A special case is a command that has no
 * effect.
 */
public class Effect {

	private final Message[] messages;
	
	private Effect(@NonNull Message... messages) {
		this.messages = messages;
	}
	
	public Stream<Message> getMessages() {
		return Arrays.stream(messages);
	}

	public Stream<Command> getCommands() {
		return Arrays.stream(messages)
				.filter(message -> message instanceof Command)
				.map(Command.class::cast);
	}

	public Stream<Event> getEvents() {
		return Arrays.stream(messages)
				.filter(message -> message instanceof Event)
				.map(Event.class::cast);
	}

	public static Effect none() {
		return new Effect(new Message[0]);
	}

	public static Effect of(@NonNull Message... messages) {
		return new Effect(messages);
	}

}
