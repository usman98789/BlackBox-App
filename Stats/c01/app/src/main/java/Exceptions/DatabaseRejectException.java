package Exceptions;

/**
 * Fancy exception class to throw when a user tries to insert something to the database
 * which does not agree with the style and format of the database.
 */
public class DatabaseRejectException extends Exception {

  private static final long serialVersionUID = 0L;

  public DatabaseRejectException() {
    super();
  }
  
  /**
   * Allows this exception to be thrown with a message.
   * @param msg message to use
   */
  public DatabaseRejectException(String msg) {
    super(msg);
  }
}
