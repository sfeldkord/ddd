package cqrs.common;

public interface Value {
	
	@Override boolean equals(Object obj);
	@Override int hashCode();
	
}
