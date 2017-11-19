package com.c01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;

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

    @Test
    public void testInsertUser() throws Exception {
        User user = null;
        int userId = 0;
        userId = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
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
        assertEquals("gagan", user.getName());
        assertEquals(19, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertMessage() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        int msgId = (int)mydb.insertMessage(id, "hello");
        String msg = mydb.getSpecificMessage(msgId);
        assertEquals("hello", msg);
    }

    @Test
    public void testInsertMark() throws Exception {
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        mydb.insertAssignmentMark(id, 50, 1);
        double mark = mydb.getAssignmentMark(id, 1);
        assertEquals(50, mark , 0);
    }

}
