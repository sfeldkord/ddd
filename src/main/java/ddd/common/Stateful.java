package ddd.common;

//Interface used to expose a unified state captured as an enum
//TODO may be extended to support safe state transitions
public interface Stateful<S extends Enum<S>> {
	
	S getState();

}
