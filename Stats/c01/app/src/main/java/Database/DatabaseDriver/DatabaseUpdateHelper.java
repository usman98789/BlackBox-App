package Database.DatabaseDriver;

import android.content.Context;

import Exceptions.InvalidNameException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DatabaseUpdateHelper {

  /**
   * Update the role name of a given role in the role table.
   *
   * @param name    the new name of the role.
   * @param id      the current ID of the role.
   * @param context is the context received from the activity
   * @return true if successful, false otherwise.
   * @throws InvalidNameException if the given name is null
   */
  public static boolean updateRoleName(String name, int id, Context context) throws InvalidNameException {
    // check if name is null
    if (name.equals(null)) {
      throw new InvalidNameException();
    }
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    // update the name
    boolean complete = mydb.updateRoleName(name, id);
    mydb.close();
    return complete;
  }

  /**
   * Use this to update the user's name.
   *
   * @param name    the new name
   * @param id      the current id
   * @param context is the context received from the activity
   * @return true if it works, false otherwise.
   * @throws InvalidNameException is the inputed name is null.
   */
  public static boolean updateUserName(String name, int id, Context context) throws InvalidNameException {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    // check if inputed name is null
    if (name.equals(null)) {
      throw new InvalidNameException();
    }
    // run helper to update the user name
    boolean complete = mydb.updateUserName(name, id);
    mydb.close();
    return complete;
  }

  /**
   * Use this to update the user's age.
   *
   * @param age     the new age.
   * @param id      the current id
   * @param context is the context received from the activity
   * @return true if it succeeds, false otherwise.
   */
  public static boolean updateUserAge(int age, int id, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    // check if inputed age is less than 0
    if (age < 0) {
      return false;
    }
    // run the helper to update the age of the user
    boolean complete = mydb.updateUserAge(age, id);
    mydb.close();
    return complete;
  }

  /**
   * update the role of the user.
   *
   * @param roleId  the new role.
   * @param id      the current id.
   * @param context is the context received from the activity
   * @return true if successful, false otherwise.
   */
  public static boolean updateUserRole(int roleId, int id, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    // get the list of roles from the database
    List<Integer> listA = new ArrayList<>();
    listA = DatabaseSelectHelper.getRoles(context);
    boolean exists1 = false;
    // if the inputed roleId is not in the list
    for (int i = 0; i < listA.size(); i++) {
      if ((roleId == listA.get(i))) {
        exists1 = true;
      }
    }
    if (exists1 == false) {
      return false;
    }
    // update the user role
    boolean complete = mydb.updateUserRole(roleId, id);
    mydb.close();
    return complete;
  }

  /**
   * Use this to update user's address.
   *
   * @param address the new address.
   * @param id      the current id.
   * @param context is the context received from the activity
   * @return true if successful, false otherwise.
   */
  public static boolean updateUserAddress(String address, int id, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    // check if address is within 100 character limit
    if (!(address.length() <= 100)) {
      return false;
    }
    boolean complete = mydb.updateUserAddress(address, id);
    mydb.close();
    return complete;
  }

  /**
   * Updates a users password in the database.
   *
   * @param id       the id of the user.
   * @param password the HASHED password of the user (not plain text!).
   * @param context  is the context received from the activity
   * @return true if update succeeded, false otherwise.
   * @throws InvalidNameException if password is null
   */
  public static boolean updatePassword(String password, int id, Context context)
      throws InvalidNameException {
    // check if name is null
    if (password.equals(null)) {
      throw new InvalidNameException();
    }
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    boolean complete = mydb.updateUserPassword(password, id);
    mydb.close();
    return complete;
  }

  /**
   * Update the state of the user message to viewed.
   *
   * @param id      the id of the message that has been viewed.
   * @param context is the context received from the activity
   * @return true if successful, false o/w.
   */
  public static boolean updateUserMessageState(int id, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    boolean changed = mydb.updateUserMessageState(id);
    mydb.close();
    return changed;
  }
}
