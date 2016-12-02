package cqrs.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks event consumer interested in specific events. Event
 * consumers marked with this annotation will get picked up by the framework
 * automatically.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventConsumer {
	//
}
