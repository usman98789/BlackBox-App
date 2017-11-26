package Database.DatabaseDriver;

import java.util.ArrayList;
import java.util.List;

import user.*;
import android.content.Context;
import android.database.Cursor;

public class DatabaseSelectHelper {

  /**
   * Gets the role with id
   *
   * @param id      the id of the role
   * @param context is the context received from the activity
   * @return the string containing the role
   */
  public static String getRole(int id, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    return mydb.getRole(id);
  }

  /**
   * get the hashed version of the password.
   *
   * @param userId  the user's id.
   * @param context is the context received from the activity
   * @return the hashed password to be checked against given password.
   */
  public static String getPassword(int userId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    String pass = mydb.getPassword(userId);
    mydb.close();
    return pass;
  }

  /**
   * Get a User object from the userId.
   *
   * @param userId  the id of the user
   * @param context is the context received from the activity
   * @return a User object with details about the user from the database
   */
  public static User getUserDetails(int userId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    User user = null;
    // get the result set based on the userId
    Cursor results = mydb.getUserDetails(userId);
    // for each result get the user details
    while (results.moveToNext()) {
      // get the role name
      int roleId = results.getInt(results.getColumnIndex("ROLEID"));
      String role = mydb.getRole(roleId);
      // check which user it is and create the users
      if (role.equals("PROF")) {
        user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
            results.getInt(results.getColumnIndex("AGE")), context);
      }  else if (role.equals("STUDENT")) {
        user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
            results.getInt(results.getColumnIndex("AGE")), context);
      }
    }
    results.close();
    mydb.close();
    return user;
  }

  /**
   * Get a list of roles.
   *
   * @param context is the context received from the activity
   * @return a list of roles.
   */
  public static List<Integer> getRoles(Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    Cursor results = null;
    List<Integer> ids = new ArrayList<>();
    // try to get the result set of all the roles
    results = mydb.getRoles();
    while (results.moveToNext()) {
      // add each id into the list of id's
      ids.add(results.getInt(results.getColumnIndex("ID")));
    }
    results.close();
    mydb.close();
    return ids;
  }

  public static double getAssignmentMark (int userId, int aNum, Context context) {
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    double mark = -1;
    try {
      mark = mydb.getAssignmentMark(userId, aNum);
    } catch (Exception e) {
    }
    mydb.close();
    return mark;
  }

  /**
   * Get the roleId of the user.
   *
   * @param userId  the id of the user.
   * @param context is the context received from the activity
   * @return the roleId of the user.
   */
  public static int getUserRole(int userId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    int role = -1;
    // try and get the roleId of the user, otherwise catch an exception
    role = mydb.getUserRole(userId);
    mydb.close();
    return role;
  }

  /**
   * Get all messageIds currently available to a user.
   *
   * @param userId  the user whose messages are being retrieved.
   * @param context is the context received from the activity
   * @return a list of IDs of the messages.
   */
  public static List<Integer> getAllMessageIds(int userId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    List<Integer> messageIdList = new ArrayList<>();
    Cursor result = mydb.getAllMessages(userId);
    while (result.moveToNext()) {
      messageIdList.add(result.getInt(result.getColumnIndex("ID")));
    }
    result.close();
    mydb.close();
    return messageIdList;
  }

  /**
   * Get all messages currently available to a user.
   *
   * @param userId  the user whose messages are being retrieved.
   * @param context is the context received from the activity
   * @return a list of messages currently in the database.
   */
  public static List<String> getAllMessages(int userId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    List<String> messageList = new ArrayList<>();
    Cursor result = mydb.getAllMessages(userId);
    while (result.moveToNext()) {
      messageList.add(result.getString(result.getColumnIndex("MESSAGE")));
    }
    result.close();
    mydb.close();
    return messageList;
  }

  /**
   * Get a specific message from the database.
   *
   * @param messageId the id of the message.
   * @param context   is the context received from the activity
   * @return the message from the database as a string.
   */
  public static String getSpecificMessage(int messageId, Context context) {
    // create a an instance of a database
    DatabaseDriverA mydb = new DatabaseDriverA(context);
    String message = "";
    message = mydb.getSpecificMessage(messageId);
    mydb.close();
    return message;
  }
}