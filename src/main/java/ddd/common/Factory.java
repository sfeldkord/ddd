package ddd.common;

public interface Factory<T> {

	T build();
	
	void verify() throws IllegalStateException;

}
