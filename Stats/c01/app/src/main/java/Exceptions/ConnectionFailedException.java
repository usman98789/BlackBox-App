package Exceptions;

/**
 * Exception thrown when database connections fail.
 */
public class ConnectionFailedException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public ConnectionFailedException() {
    super();
  }
  
  /**
   * Allows this exception to be thrown with a message.
   * @param msg message to use
   */
  public ConnectionFailedException(String msg) {
    super(msg);
  }

}
