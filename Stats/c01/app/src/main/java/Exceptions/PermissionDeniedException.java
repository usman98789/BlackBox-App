package Exceptions;

/**
 * Exception to throw when users attempt to perform actions they are not
 * permitted to take based on authentication.
 */
public class PermissionDeniedException extends Exception {

  private static final long serialVersionUID = 5904047562400456177L;

  public PermissionDeniedException() {
    super();
  }
  
  /**
   * Allows this exception to be thrown with a message.
   * @param msg message to use
   */
  public PermissionDeniedException(String msg) {
    super(msg);
  }
}
