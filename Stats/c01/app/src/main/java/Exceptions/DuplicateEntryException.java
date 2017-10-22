package Exceptions;

/**
 * Exception to be thrown when an entry is well-formed but is
 * rejected due to duplicating existing data.
 */
public class DuplicateEntryException extends Exception {

  private static final long serialVersionUID = 1L;

  public DuplicateEntryException() {
    super();
  }
  
  /**
   * Allows this exception to be thrown with a message.
   * @param msg message to use
   */
  public DuplicateEntryException(String msg) {
    super(msg);
  }
}
