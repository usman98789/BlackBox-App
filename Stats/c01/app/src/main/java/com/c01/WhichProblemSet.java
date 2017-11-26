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

public class WhichProblemSet extends AppCompatActivity {

    private static Context context;
    private static String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_problem_set);

        Button button = (Button) findViewById(R.id.enter);
        context = this.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (check(context)) {
                    Intent i = new Intent(WhichProblemSet.this, AnswerProblems.class);
                    i.putExtra("num", num);
                    startActivity(i);
                }
            }
        });
    }

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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(WhichProblemSet.this, StudentFileInbox.class);
        startActivity(i);
    }
}
