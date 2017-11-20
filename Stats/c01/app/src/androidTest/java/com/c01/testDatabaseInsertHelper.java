package com.c01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import Database.DatabaseDriver.DatabaseDriverA;
import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import generics.EnumMapRoles;
import generics.Roles;
import user.Prof;
import user.Student;
import user.User;

import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testDatabaseInsertHelper {

    Context context;
    DatabaseDriverA mydb;
    EnumMapRoles roleMap;
    SQLiteDatabase sql;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        mydb = new DatabaseDriverA(context);
        mydb.insertRole("PROF");
        mydb.insertRole("STUDENT");
        roleMap = new EnumMapRoles(context);
    }

    @After
    public void finish() {
        mydb.close();
        sql = mydb.getWritableDatabase();
        mydb.onUpgrade(sql, 1, 2);
    }

    @Test
    public void testInsertNewUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(19, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertMaxAgeUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", 2147483647, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(2147483647, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertZeroAgeUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", 0, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(0, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertMinNegativeAgeUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", -2147483648, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(-2147483648, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertNegativeAgeUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", -1, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(-1, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertBlankNameUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("", 1, "123street", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("", user.getName());
        assertEquals(1, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertBlankAddressUser() throws Exception {
        User user = null;
        int userId = (int)mydb.insertNewUser("menu", 1, "", roleMap.get(Roles.STUDENT), "123");
        Cursor results = mydb.getUserDetails(userId);
        // for each result get the user details
        while (results.moveToNext()) {
            // get the role name
            int roleId = results.getInt(results.getColumnIndex("ROLEID"));
            String role = mydb.getRole(roleId);
            // check which user it is and create the users
            if (role.equals("PROF")) {
                user = new Prof(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }  else if (role.equals("STUDENT")) {
                user = new Student(userId, results.getString(results.getColumnIndex("NAME")),
                        results.getInt(results.getColumnIndex("AGE")),
                        results.getString(results.getColumnIndex("ADDRESS")), context);
            }
        }
        results.close();
        assertEquals("menu", user.getName());
        assertEquals(1, user.getAge());
        assertEquals("", user.getAddress());
    }

    @Test
    public void testInsertBlankPassUser() throws Exception {
        try {
            int userId = (int)mydb.insertNewUser("menu", 1, "123", roleMap.get(Roles.STUDENT), "");
            Log.e("Blank Password", "There is an user account without a password!");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertBlankUser() throws Exception {
        try {
            int userId = (int)mydb.insertNewUser("", 1, "", roleMap.get(Roles.STUDENT), "");
            Log.e("Blank white account", "There is a blank user account");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertRole() throws Exception {
        assertEquals(3, mydb.insertRole("PROF"));
        assertEquals(4, mydb.insertRole("STUDENT"));
    }

    @Test
    public void testNULLInsertRole() throws Exception {
        try {
            assertEquals(3, mydb.insertRole("PROF"));
            Log.e("Blank role", "There is a blank");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertAssignmentMark() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        mydb.insertAssignmentMark(id, 50, 1);
        double mark = mydb.getAssignmentMark(id, 1);
        assertEquals(50, mark , 0);
    }

    @Test
    public void testInsertAssignmentNegativeMark() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            mydb.insertAssignmentMark(id, -50, 1);
            Log.e("Negative Mark", "There is a negative mark for an assignment");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertAssignmentOverMark() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            mydb.insertAssignmentMark(id, 101, 1);
            Log.e("Mark", "There is a mark over 100% for an assignment");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertAssignmentOverMarkWithWrongId() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            mydb.insertAssignmentMark(-1, 101, 1);
            Log.e("ID", "No such ID and assignment mark over 100%");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertAssignmentMarkWithWrongId() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            mydb.insertAssignmentMark(-1, 100, 1);
            Log.e("ID", "No such ID and assignment mark over 100%");
        } catch (Exception e) {
        }
    }

    @Test
    public void testInsertAssignmentPerfectMark() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        mydb.insertAssignmentMark(id, 100, 1);
        double mark = mydb.getAssignmentMark(id, 1);
        assertEquals(100, mark , 0);
    }

    @Test
    public void testInitInsertMessage() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            int msgId = (int)mydb.insertMessage(id, "hello");
            String msg = mydb.getSpecificMessage(msgId);
            assertEquals("hello", msg);
        } catch (Exception e) {
        }
    }

    @Test
    public void testInitInsertMessageWithWrongID() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            int msgId = (int)mydb.insertMessage(id, "hello");
            String msg = mydb.getSpecificMessage(-1);
        } catch (Exception e) {
        }
    }

    @Test
    public void testInitInsertMessageWithBlankMessage() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            int msgId = (int)mydb.insertMessage(id, "");
            String msg = mydb.getSpecificMessage(id);
            assertEquals("", msg);
        } catch (Exception e) {
        }
    }

    @Test
    public void testInitInsertMessageWithBlankMessageAndWrongId() throws Exception {
        try {
            int id = 0;
            id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
            int msgId = (int)mydb.insertMessage(id, "");
            String msg = mydb.getSpecificMessage(-1);
            assertEquals("", msg);
        } catch (Exception e) {
        }
    }
}
