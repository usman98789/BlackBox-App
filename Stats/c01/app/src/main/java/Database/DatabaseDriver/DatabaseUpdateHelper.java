package Database.DatabaseDriver;

/**
 * Created by usman on 26/11/17.
 */

import android.content.Context;

import Exceptions.InvalidNameException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUpdateHelper {
    public static boolean updateAssignmentMark(int id, double mark, int aNum, Context context){
        if (mark < 0){
            mark = 0;
        } else if (mark > 100){
            mark = 100;
        }

        DatabaseDriverA mydb = new DatabaseDriverA(context);
        boolean complete = mydb.updateAssignmentMark(id, mark, aNum);
        mydb.close();
        return complete;
    }
}
