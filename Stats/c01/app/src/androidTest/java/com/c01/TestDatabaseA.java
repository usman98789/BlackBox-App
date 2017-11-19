package com.c01;

import android.content.Context;
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
import user.User;

import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDatabaseA {

    Context context;
    DatabaseDriverA mydb;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        mydb = new DatabaseDriverA(context);
    }

    @After
    public void finish() {
        mydb.close();
    }

    @Test
    public void testInsertUser() throws Exception {
        EnumMapRoles roleMap = new EnumMapRoles(context);
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        User user = DatabaseSelectHelper.getUserDetails(id, context);
        assertEquals("gagan", user.getName());
        assertEquals(19, user.getAge());
        assertEquals("123street", user.getAddress());
    }

    @Test
    public void testInsertMessage() throws Exception {
        EnumMapRoles roleMap = new EnumMapRoles(context);
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        int msgId = (int)mydb.insertMessage(id, "hello");
        String msg = DatabaseSelectHelper.getSpecificMessage(msgId, context);
        assertEquals("hello", msg);
    }

    @Test
    public void testInsertRole() throws Exception {
        int id = (int)mydb.insertRole("ROLE1");
        String role = DatabaseSelectHelper.getRole(id, context);
        assertEquals("ROLE1", role);
    }

    @Test
    public void testgetAssignmentMark() throws Exception {
        EnumMapRoles roleMap = new EnumMapRoles(context);
        int id = 0;
        id = (int)mydb.insertNewUser("gagan", 19, "123street", roleMap.get(Roles.STUDENT), "123");
        DatabaseInsertHelper.insertAssignmentMark(id, 59.4, 1, context);
        assertEquals(59.4, DatabaseSelectHelper.getAssignmentMark(id, 1, context), 0);
    }

    
}
