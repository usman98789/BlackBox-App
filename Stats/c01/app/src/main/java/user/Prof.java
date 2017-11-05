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
  private float mark;

  /**
   * create a Prof.
   * 
   * @param id which is the integer given
   * @param name of the admin
   * @param age of admin
   * @param address of admin
   */
  public Prof(int id, String name, int age, String address, Context context) {
    // set id,name,address and get role id.
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.address = address;
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  /**
   * already authenticated new admin.
   * 
   * @param id which is the id of admin
   * @param name of admin
   * @param age of admin
   * @param address of admin
   * @param authenticated of admin
   */
  public Prof(int id, String name, int age, String address, boolean authenticated,
              Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.address = address;
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  @Override
  public int getRoleId() {
    // return the role id number
    return this.roleId;
  }

  @Override
  public String getAddress() {
    // return the string which is the address
    return this.address;
  }

}
