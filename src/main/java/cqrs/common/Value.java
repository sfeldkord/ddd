package cqrs.common;

//TODO replace by @lombok.Value?
public interface Value {
	
	@Override boolean equals(Object obj);
	@Override int hashCode();
	
}
