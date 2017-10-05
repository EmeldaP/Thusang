package com.example.codetribe.thusang;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {


    EditText Name, Email, Password;
    Button Register,Update;
     private  DatabaseReference ref;
    private  FirebaseDatabase da;
     PersonalD personalD = new PersonalD();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Name = (EditText) findViewById(R.id.editText5);
        Email = (EditText) findViewById(R.id.editText6);
        Password = (EditText) findViewById(R.id.editText8);
        Register = (Button) findViewById(R.id.reg);
        Update  = (Button)findViewById(R.id.update);

        Register.setOnClickListener(this);
        Update.setOnClickListener(this);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update( );
                Intent i = new Intent(LogActivity.this,LogActivity.class);
                startActivity(i);
            }
        });


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalD.setName(Name.getText().toString());
                personalD.setPassword(Password.getText().toString());
                personalD.setEmail(Email.getText().toString());

                if (Email.getText().toString().equals("") && Password.getText().
                        toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();



                    Intent register = new Intent(LogActivity.this, MainActivity.class);
                    startActivity(register);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong creditials entered",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void update(String Name, String Email,String Password) {

        ref = FirebaseDatabase.getInstance().getReference().child("personalD");
        PersonalD p = new PersonalD();
        ref.setValue("");

        Toast.makeText(this,"Thank you for updating your details",Toast.LENGTH_SHORT).show();
        return ;
    }


    //add new learner into database
    public void newMember(View view) {
        DatabaseHandlerLog log = new DatabaseHandlerLog(this);
        LogIn logIn = new LogIn(Name.getText().toString());
        log.addMember(logIn);
        Name.setText("");
        Email.setText("");
        Password.setText("");
        Toast.makeText(this,"Welcome,You are registered ",Toast.LENGTH_SHORT).show();
    }
    public void deleteMember(View view) {
        DatabaseHandlerLog log = new DatabaseHandlerLog(this);
        boolean result = log.delete(Email.getText().toString());

        if (result) {
            Name.setText("Record Deleted");
            Email.setText("");
            Password.setText("");

        } else {
            Email.setText("No Match Found");
        }
    }

    public void update(){
        DatabaseHandlerLog log = new DatabaseHandlerLog(this);
       // int id = Email.getText().toString();
        LogIn logIn = new LogIn(Email.getText().toString());

        log.updateMember(logIn);
        Name.setText("");
        Email.setText("");
        Password.setText("");
        Toast.makeText(this,"Update was sucessful",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onClick(View v) {

    }
}
