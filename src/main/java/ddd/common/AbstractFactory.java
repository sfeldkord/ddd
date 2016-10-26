package ddd.common;

public abstract class AbstractFactory<T> implements Factory<T> {

	protected T object;
	
	@Override
	public final T build() {
		verify();
		return object;
	}

	public void verify() throws IllegalStateException {
		if (object == null) {
			throw new IllegalStateException();
		}
	}

}
