package ozone.marketplace.controller;

/**
 * Thrown when an error occurs during the synchronization of OMP listings and
 * OWF widget definitions.
 */
public class OzoneSynchronizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OzoneSynchronizationException() {
	}

	public OzoneSynchronizationException(String arg0) {
		super(arg0);
	}

	public OzoneSynchronizationException(Throwable arg0) {
		super(arg0);
	}

	public OzoneSynchronizationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
