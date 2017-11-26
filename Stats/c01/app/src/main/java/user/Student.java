package user;

import Database.DatabaseDriver.DatabaseSelectHelper;
import android.content.Context;

public class Student extends User {

  private String address;
  private int roleId;

  /**
   * create a Student object under User
   * 
   * @param id of student
   * @param name of student
   * @param age of student
   */
  public Student(int id, String name, int age, Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  /**
   * studentr with authentication
   * 
   * @param id of student
   * @param name of student
   * @param age of student
   * @param authenticated if the user is authenticated or not
   */
  public Student(int id, String name, int age, boolean authenticated,
                 Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }


  @Override
  public int getRoleId() {
    // the role id of the customer
    return this.roleId;
  }
}
