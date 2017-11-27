package com.c01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
* The activity for which problem set.
*/
public class WhichProblemSet extends AppCompatActivity {

    private static Context context;
    private static String num;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_problem_set);

        Button button = (Button) findViewById(R.id.enter);
        context = this.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            public void onClick(View view) {
                if (check(context)) {
                    Intent i = new Intent(WhichProblemSet.this, ViewProblems.class);
                    i.putExtra("num", num);
                    startActivity(i);
                }
            }
        });
    }

    /**
    * Check if number is between 1 and 4.
    * @param context The context
    * @return boolean True if number is between 1 and 4
    */
    private boolean check(Context context) {
        EditText etNum = (EditText) findViewById(R.id.enterNumber);
        num = etNum.getText().toString();
        boolean check = false;
        if (TextUtils.isEmpty(String.valueOf(num))) {
            etNum.setError("No number entered");
            return false;
        }
        if (!((num.equals("1")) || (num.equals("2")) || (num.equals("3")) || (num.equals("4")))) {
            etNum.setError("Number is not between 1-4");
            return false;
        } else {
            return true;
        }

    }
    
    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(WhichProblemSet.this, StudentFileInbox.class);
        startActivity(i);
    }
}
