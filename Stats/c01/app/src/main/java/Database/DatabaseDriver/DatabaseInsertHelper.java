package Database.DatabaseDriver;


import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Exceptions.*;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;

@WebServlet("/FileUpload")
@MultipartConfig
public class DatabaseInsertHelper extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * inserts a new user into the users table
     *
     * @param name     the name of the user
     * @param age      the age of the user
     * @param address  the address of the user
     * @param roleId   the roleId of the user
     * @param password the password of the user
     * @param context  is the context received from the activity
     * @return Id if successful, -1 otherwise
     * @throws InvalidNameException if the name is null
     */
    public static int insertNewUser(String name, int age, String address, int roleId, String password,
                                    Context context) throws InvalidNameException {

        if (password.length() < 1) {
            return -1;
        }

        // create a an instance of a database
        DatabaseDriverA mydb = new DatabaseDriverA(context);
        // check if role id is in the database
        List<Integer> listA = new ArrayList<>();
        listA = DatabaseSelectHelper.getRoles(context);
        boolean exists = false;
        for (int i = 0; i < listA.size(); i++) {
            if (!(roleId == listA.get(i))) {
                exists = true;
            }
        }
        if (exists == false) {
            return -1;
        }
        // insert new user
        int id = 0;
        id = (int) mydb.insertNewUser(name, age, address, roleId, password);
        mydb.close();
        return id;
    }

    /**
     * Use this to insert new roles into the database.
     *
     * @param role    the new role to be added.
     * @param context is the context received from the activity
     * @return true if successful, false otherwise.
     * @throws InvalidNameException if the string is null
     */
    public static int insertRole(String role, Context context) throws InvalidNameException {
        // create a an instance of a database
        DatabaseDriverA mydb = new DatabaseDriverA(context);
        // check if name is null
        if (role.equals(null)) {
            throw new InvalidNameException();
        }
        // insert the role
        int success = 0;
        success = (int) mydb.insertRole(role);
        mydb.close();
        return success;
    }

    public static void insertMark(int userId, double mark, Context context) {
        DatabaseDriverA mydb = new DatabaseDriverA(context);
        mydb.insertMark(userId, mark);
        mydb.close();
    }

    public static void insertFeedBackMark(int userId, double mark, Context context) {
        DatabaseDriverA mydb = new DatabaseDriverA(context);
        mydb.insertFeedBackMark(userId, mark);
        mydb.close();
    }

    public static void insertAssignmentMark (int userId, double mark, int aNum, Context context) throws InvalidMarkException, InvalidIdException, InvalidAssignmentException {
        if ((mark < 0) || (mark > 100)) {
            throw new InvalidMarkException();
        }
        if ((aNum < 0)) {
            throw new InvalidAssignmentException();
        }
        if (userId < 0) {
            throw new InvalidIdException();
        }
        DatabaseDriverA mydb = new DatabaseDriverA(context);
        mydb.insertAssignmentMark(userId, mark, aNum);
        mydb.close();
    }

    /**
     * Insert a new message into the database.
     *
     * @param userId  the id of the user whom the message is for.
     * @param message the message to be left (max 512 characters).
     * @param context is the context received from the activity
     * @return the id of the inserted message.
     */
    public static int insertMessage(int userId, String message, Context context) throws InvalidIdException {
        if (userId < 0) {
            throw new InvalidIdException();
        }

        int messageId = -1;
        if ((userId > 0) && (message.length() <= 512)) {
            // create a an instance of a database
            DatabaseDriverA mydb = new DatabaseDriverA(context);
            messageId = (int) mydb.insertMessage(userId, message);
            mydb.close();
        }
        return messageId;
    }

    /**
     * File upload into database
     *
     * @param filePath the path of the pdf
     * @return void
     */
    public static void uploadPDF(String filePath) throws Exception, IOException, SQLException {
        String url = "jdbc:mysql://localhost:3306/contactdb";
        String user = "root";
        String password = "secret";

        //String filePath = "D:/Photos/Tom.png";

        try {
            Connection conn = DriverManager.getConnection("http://10.0.2.2", "", "");
            //(URL, "USERNAME", "PASSWORD") -> to access db
            String querySetLimit = "SET GLOBAL max_allowed_packet=104857600;";  // 10 MB
            Statement stSetLimit = conn.createStatement();
            stSetLimit.execute(querySetLimit);
            //
            String sql = "INSERT INTO person (first_name, last_name, photo) values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            //
            InputStream inputStream = new FileInputStream(new File(filePath));
            FileInputStream io = new FileInputStream(String.valueOf(inputStream));
            byte[] b = new byte[(int)String.valueOf(inputStream).length()];

            PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("INSERT  INTO  2012DOC (SRNO,DOCUMENT) VALUES  (?,?)");   //con is java.sql.Connection object
            psmnt.setString(1, "1200021");
            psmnt.setBytes(2, b);
            psmnt.executeUpdate();

            psmnt.setBinaryStream(3,  (InputStream)io,(int)String.valueOf(inputStream).length());


            statement.setBlob(3, inputStream);

            int row = statement.executeUpdate();
            if (row > 0) {
                System.out.println("A contact was inserted with photo image.");
            }
            psmnt.execute();
            conn.commit();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * File upload into database
     *
     * @param filePath the path of the pdf
     * @return void
     */
    public static void uploadPictureFile(String filePath) throws Exception, IOException, SQLException {
        Class.forName("org.gjt.mm.mysql.Driver");
        Connection conn = DriverManager.getConnection("http://10.0.2.2", "", "");
        //("jdbc:mysql://localhost/databaseName", "root", "root") ^
        String INSERT_PICTURE = "insert into MyPictures(id, name, photo) values (?, ?, ?)";

        filePath = "myPhoto.png"; // temp

        FileInputStream fis = null;
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            File file = new File(filePath);
            fis = new FileInputStream(file);
            ps = conn.prepareStatement(INSERT_PICTURE);
            ps.setString(1, "001");
            ps.setString(2, "name");
            ps.setBinaryStream(3, fis, (int) file.length());
            ps.executeUpdate();
            conn.commit();
        } finally {
            ps.close();
            fis.close();
        }
    }
}
