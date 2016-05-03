package ddd.example.shared;

@SuppressWarnings("serial")
public final class InvalidOperationException extends RuntimeException {

	public InvalidOperationException(String message) {
		super(message);
	}
	
}
