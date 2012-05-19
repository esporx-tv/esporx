package tv.esporx.dao.exceptions;

import javax.persistence.PersistenceException;

public class PersistenceViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final PersistenceException cause;

	public PersistenceViolationException(final PersistenceException cause) {
		this.cause = cause;
	}

	public String getCauseMessage() {
		return this.cause.getMessage();
	}
}
