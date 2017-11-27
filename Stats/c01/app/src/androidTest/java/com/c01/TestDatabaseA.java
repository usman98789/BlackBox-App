package com.c01;

import android.content.Context;
import android.content.SyncStats;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SymbolTable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.Toast;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import Database.DatabaseDriver.DatabaseDriverA;
import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import Database.DatabaseDriver.PasswordHelpers;
import generics.EnumMapRoles;
import generics.Roles;
import user.Prof;
import user.Student;
import user.User;

import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDatabaseA {

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

    //Test cases for insert

    @Test
    public void testInsertUser() throws Exception {
        User user = null;
        int userId = 0;
        userId = (int)mydb.insertNewUser("gagan", 19, roleMap.get(Roles.STUDENT), "123");
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
        assertEquals("gagan", user.getName());
        assertEquals(19, user.getAge());
    }

    @Test
    public void testInsertMessage() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, roleMap.get(Roles.STUDENT), "123");
        int msgId = (int)mydb.insertMessage(id, "hello");
        String msg = mydb.getSpecificMessage(msgId);
        assertEquals("hello", msg);
    }

    @Test
    public void testInsertMark() throws Exception {
        int id = 0;
                id = (int)mydb.insertNewUser("gagan", 19, roleMap.get(Roles.STUDENT), "123");
        mydb.insertAssignmentMark(id, 50, 1);
        double mark = mydb.getAssignmentMark(id, 1);
        assertEquals(50, mark , 0);
    }

    //Test cases for select
//    @Test
//    public void testgetAssignmentMark() throws Exception {
//        EnumMapRoles roleMap = new EnumMapRoles(context);
//        int id = 0;
//        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
//        DatabaseInsertHelper.insertAssignmentMark(id, 59.4, 1, context);
//        assertEquals(59.4, DatabaseSelectHelper.getAssignmentMark(id, 1, context), 0);
//    }

    @Test
    public void testGetRoleOfStudent() throws Exception {
        EnumMapRoles roleMap = new EnumMapRoles(context);

        assertEquals("STUDENT",         mydb.getRole(roleMap.get(Roles.STUDENT).intValue()));
    }

    @Test
    public void testGetRoleOfProf() throws Exception {
        assertEquals("PROF",         mydb.getRole(roleMap.get(Roles.PROF).intValue()));
    }

    @Test
    public void testGetUserRoleStudent() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, roleMap.get(Roles.STUDENT), "123");
        int expected = roleMap.get(Roles.STUDENT).intValue();
        assertEquals(expected,         mydb.getUserRole(id));
    }

    @Test
    public void testGetUserRoleProf() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("KC", 24, roleMap.get(Roles.PROF), "123");;
        int expected = roleMap.get(Roles.PROF).intValue();
        assertEquals(expected,         mydb.getUserRole(id));
    }

    @Test
    public void testPassword() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("KC", 24, roleMap.get(Roles.PROF), "WOOOO");
        String pass = PasswordHelpers.passwordHash("WOOOO");
        assertEquals(pass,         mydb.getPassword(id));
    }

    @Test
    public void testGetAllMessages() throws Exception {
        User user = null;
        int userId = 0;
        int id = 0;
        id = (int)mydb.insertNewUser("KC", 24, roleMap.get(Roles.PROF), "WOOOO");
        userId = (int)mydb.insertNewUser("bob", 19, roleMap.get(Roles.STUDENT), "123");

        int msgId = (int)mydb.insertMessage(userId, "hello");
        int msgId2 = (int)mydb.insertMessage(userId, "I need help with problem set 1 question 2");
        int msgId3 = (int)mydb.insertMessage(id, "Read the textbook.");

        Cursor results = mydb.getAllMessages(userId);
        String text = "";
        Cursor results2 = mydb.getAllMessages(id);
        String text2 = "";

        while (results.moveToNext()) {
            text += results.getString(results.getColumnIndex("MESSAGE")) + " ";
        }

        while (results2.moveToNext()) {
            text2 += results2.getString(results.getColumnIndex("MESSAGE")) + " ";
        }

        results.close();
        assertEquals("hello I need help with problem set 1 question 2 ", text);
        assertEquals("Read the textbook. ", text2);


    }
}
