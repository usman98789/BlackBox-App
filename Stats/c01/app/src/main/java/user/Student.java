package user;

import Database.DatabaseDriver.DatabaseSelectHelper;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

  private String address;
  private int roleId;

  /**
   * create a Student object under User
   * 
   * @param id of customer
   * @param name of customer
   * @param age of customer
   * @param address of customer
   */
  public Student(int id, String name, int age, String address, Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.address = address;
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  /**
   * customer with authentication
   * 
   * @param id of customer
   * @param name of customer
   * @param age of customer
   * @param address of customer
   * @param authenticated if the customer is authenticated or not
   */
  public Student(int id, String name, int age, String address, boolean authenticated,
                 Context context) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.address = address;
    this.roleId = DatabaseSelectHelper.getUserRole(id, context);
  }

  @Override
  public String getAddress() {
    // the address of customer
    return this.address;
  }

  @Override
  public int getRoleId() {
    // the role id of the customer
    return this.roleId;
  }



}
