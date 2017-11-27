package com.c01;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AssignmentChoice extends AppCompatActivity {

    private static int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_choice);

        Button attemptRand = (Button) findViewById(R.id.attemptRandom);
        attemptRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCreds();


            }
        });
    }

    public void getCreds() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText numBox = new EditText(this);
        numBox.setHint("Enter assignment number");
        layout.addView(numBox);


        dialog.setView(layout);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                num = Integer.parseInt(numBox.getText().toString());

                Intent nextpage = new Intent(AssignmentChoice.this, AnswerProblems.class);
                int id = getIntent().getIntExtra("userId", 0);
                nextpage.putExtra("userId", id);
                nextpage.putExtra("num", num);
                startActivity(nextpage);
            }
        });
        dialog.show();
    }

}
