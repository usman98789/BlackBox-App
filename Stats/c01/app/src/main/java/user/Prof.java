package user;

import Database.DatabaseDriver.*;
import android.content.Context;

/**
 * Prof object that extends User
 * 
 */
public class Prof extends User {

  private String address;
  private int roleId;

  /**
   * create a Prof.
   * 
   * @param id which is the integer given
   * @param name of the prof
   * @param age of prof
   */
  public Prof(int id, String name, int age, Context context) {
    // set id,name,address and get role id.
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  /**
   * already authenticated new prof.
   * 
   * @param id which is the id of prof
   * @param name of prof
   * @param age of prof
   * @param authenticated of prof
   */
  public Prof(int id, String name, int age, boolean authenticated,
              Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  @Override
  public int getRoleId() {
    // return the role id number
    return this.roleId;
  }

}
