package dev.adil.orderservice.exception;

public class OutOfStockException extends RuntimeException {
	
	public OutOfStockException() {
		super();
	}
	
	public OutOfStockException(String message) {
		super(message);
	}
	
	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 3495289208600938824L;
}
