package Database.DatabaseDriver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import Database.DatabaseDriver.PasswordHelpers;

import java.math.BigDecimal;

/**
 * Created by Joe on 2017-07-17.
 */

public class DatabaseDriverA extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "bank.db";

  public DatabaseDriverA(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("CREATE TABLE ROLES "
        + "(ID INTEGER PRIMARY KEY NOT NULL,"
        + "NAME TEXT NOT NULL)");
    sqLiteDatabase.execSQL("CREATE TABLE USERS "
            + "(ID INTEGER PRIMARY KEY NOT NULL,"
            + "NAME TEXT NOT NULL,"
            + "AGE INTEGER NOT NULL,"
            + "ADDRESS CHAR(100),"
            + "ROLEID INTEGER,"
            + "FOREIGN KEY(ROLEID) REFERENCES ROLE(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE USERPW "
        + "(USERID INTEGER NOT NULL,"
        + "PASSWORD CHAR(64),"
        + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE MARK "
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE USERMESSAGES "
            + "(ID INTEGER PRIMARY KEY NOT NULL,"
            + "USERID INTEGER NOT NULL,"
            + "MESSAGE CHAR(512) NOT NULL,"
            + "VIEWED CHAR(1) NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE A1MARK"
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE A2MARK"
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE A3MARK"
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE A4MARK"
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");
    sqLiteDatabase.execSQL("CREATE TABLE FeedBackMark"
            + "(USERID INTEGER NOT NULL,"
            + "MARKS REAL NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))");

  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERMESSAGES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERPW");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERS");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ROLES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MARK");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS A1MARK");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS A2MARK");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS A3MARK");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS A4MARK");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FeedBackMark");
    onCreate(sqLiteDatabase);
  }

  //INSERTS
  public long insertRole(String role) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", role);
    return sqLiteDatabase.insert("ROLES", null, contentValues);
  }

  public long insertNewUser(String name, int age, String address, int roleId, String password) {
    long id = insertUser(name, age, address, roleId);
    insertPassword(password, (int) id);
    return id;
  }


  public long insertMessage(int userId, String message) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("MESSAGE", message);
    contentValues.put("VIEWED", 0);
    return sqLiteDatabase.insert("USERMESSAGES", null, contentValues);
  }

  public long insertUser(String name, int age, String address, int roleId) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    contentValues.put("AGE", age);
    contentValues.put("ADDRESS", address);
    contentValues.put("ROLEID", roleId);

    return sqLiteDatabase.insert("USERS", null, contentValues);
  }

  public void insertPassword(String password, int userId) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();

    password = PasswordHelpers.passwordHash(password);

    contentValues.put("USERID", userId);
    contentValues.put("PASSWORD", password);
    sqLiteDatabase.insert("USERPW", null, contentValues);
  }

  public void insertMark(int userId, double mark){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("MARKS", mark);
    sqLiteDatabase.insert("MARK", null, contentValues);
  }

  public void insertFeedBackMark(int userId, double mark){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("MARKS", mark);
    sqLiteDatabase.insert("FeedBackMark", null, contentValues);
  }

  public void insertAssignmentMark(int userId, double mark, int aNum){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("MARKS", mark);
    if (aNum == 1){
      sqLiteDatabase.insert("A1MARK", null, contentValues);
    } else if (aNum == 2){
      sqLiteDatabase.insert("A2MARK", null, contentValues);
    } else if (aNum == 3){
      sqLiteDatabase.insert("A3MARK", null, contentValues);
    } else if (aNum == 4){
      sqLiteDatabase.insert("A4MARK", null, contentValues);
    }
  }

  //SELECT METHODS
  public double getMark(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM MARK WHERE USERID = ?",
            new String[]{String.valueOf(userId)});
    cursor.moveToFirst();
    double value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
    cursor.close();
    return value;
  }

  public double getFeedBackMark(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();

    Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM FeedBackMark WHERE USERID = ?",
            new String[]{String.valueOf(userId)});
    cursor.moveToFirst();
    double value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
    cursor.close();
    return value;
  }

  public double getAssignmentMark(int userId, int aNum) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    double value = -1;

    if (aNum == 1){
      Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM A1MARK WHERE USERID = ?",
              new String[]{String.valueOf(userId)});
      cursor.moveToFirst();
      value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
      cursor.close();

    } else if (aNum == 2){
      Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM A2MARK WHERE USERID = ?",
              new String[]{String.valueOf(userId)});
      cursor.moveToFirst();
      value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
      cursor.close();

    } else if (aNum == 3){
      Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM A3MARK WHERE USERID = ?",
              new String[]{String.valueOf(userId)});
      cursor.moveToFirst();
      value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
      cursor.close();

    } else if (aNum == 4){
      Cursor cursor = sqLiteDatabase.rawQuery("SELECT MARKS FROM A4MARK WHERE USERID = ?",
              new String[]{String.valueOf(userId)});
      cursor.moveToFirst();
      value = cursor.getDouble(cursor.getColumnIndex("MARKS"));
      cursor.close();
    }

    return value;
  }

  public Cursor getRoles() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM ROLES;", null);
  }

  public String getRole(int id) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT NAME FROM ROLES WHERE ID = ?",
        new String[]{String.valueOf(id)});
    cursor.moveToFirst();
    String value = cursor.getString(cursor.getColumnIndex("NAME"));
    cursor.close();
    return value;

  }

  public int getUserRole(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT ROLEID FROM USERS WHERE ID = ?",
        new String[]{String.valueOf(userId)});
    cursor.moveToFirst();
    int result = cursor.getInt(cursor.getColumnIndex("ROLEID"));
    cursor.close();
    return result;
  }

  public Cursor getUsersDetails() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM USERS", null);
  }

  public Cursor getUserDetails(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE ID = ?",
        new String[]{String.valueOf(userId)});
  }

  public String getPassword(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT PASSWORD FROM USERPW WHERE USERID = ?",
        new String[]{String.valueOf(userId)});
    cursor.moveToFirst();
    String result = cursor.getString(cursor.getColumnIndex("PASSWORD"));
    cursor.close();
    return result;
  }


  public Cursor getAllMessages(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM USERMESSAGES WHERE USERID = ?",
        new String[]{String.valueOf(userId)});
  }

  public String getSpecificMessage(int messageId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT MESSAGE FROM USERMESSAGES WHERE ID = ?",
        new String[]{String.valueOf(messageId)});
    cursor.moveToFirst();
    String result = cursor.getString(cursor.getColumnIndex("MESSAGE"));
    cursor.close();
    return result;
  }

  //UPDATE Methods
  public boolean updateRoleName(String name, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    return sqLiteDatabase.update("ROLES", contentValues, "ID = ?", new String[]{String.valueOf(id)})
        > 0;
  }

  public boolean updateUserName(String name, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    return sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[]{String.valueOf(id)})
        > 0;
  }

  public boolean updateUserAge(int age, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("AGE", age);
    return sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[]{String.valueOf(id)})
        > 0;
  }

  public boolean updateUserRole(int roleId, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ROLEID", roleId);
    return sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[]{String.valueOf(id)})
        > 0;
  }

  public boolean updateUserAddress(String address, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ADDRESS", address);
    return sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[]{String.valueOf(id)})
        > 0;
  }


  public boolean updateUserPassword(String password, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("PASSWORD", password);
    return sqLiteDatabase.update("USERPW", contentValues, "USERID = ?",
        new String[]{String.valueOf(id)}) > 0;
  }

  public boolean updateUserMessageState(int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("VIEWED", 1);
    return sqLiteDatabase.update("USERMESSAGES", contentValues, "ID = ?",
        new String[]{String.valueOf(id)}) > 0;
  }
}
