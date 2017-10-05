package com.example.codetribe.thusang;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    // private static final String URL_FOR_REGISTRATION = "com.example.codetribe.thusang;";
    ProgressDialog progressDialog;

    private EditText Name, Email, Password, Address;
    private Button SignUp;
    private Button Login;
    private RadioGroup gender;

    //firebase
    private DatabaseReference mref;
    PersonalD personalD = new PersonalD();
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //resource id
        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        Address = (EditText) findViewById(R.id.ad);
        SignUp = (Button) findViewById(R.id.signup);
        Login = (Button) findViewById(R.id.login);
        gender = (RadioGroup) findViewById(R.id.gender);



        //getting firebase reference
       mref = FirebaseDatabase.getInstance().getReference();

           mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

             //   PersonalD personalD = dataSnapshot.getValue(PersonalD.class);
                personalD.setName(Name.getText().toString().trim().toUpperCase());
                personalD.setEmail(Email.getText().toString());
                personalD.setPassword(Password.getText().toString().toLowerCase().trim());
                personalD.setAddress(Address.getText().toString().toString());
                progressDialog.show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 submitForm();
          Toast.makeText(getApplication(), "Hi " + Name + ", " +
                  "You are registered successfully .Welcome!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
            }

        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitForm() {

       int selectedId = gender.getCheckedRadioButtonId();
        String gender;
        if (selectedId == R.id.female)
            gender = "Female";
        else
           gender = "Male";

//        registerUser(Name.getText().toString(),
//                Email.getText().toString(),
//                Password.getText().toString(),
//                gender,
//                Address.getText().toString());
//    }
//
//    private void registerUser(final String name, final String email, final String password,
//                              final String gender, final String dob) {
//        // Tag used to cancel the request
//        String cancel_req_tag = "register";
//
//        progressDialog.setMessage("Adding you ...");
//       // showDialog();
//    }
////          StringRequest strReq = new StringRequest(DownloadManager.Request.Method.POST,
////                    URL_FOR_REGISTRATION, new Response.Listener<String>() {
//
//
//    public void onResponse(String response) {
//        Log.d(TAG, "Register Response: " + response.toString());
//        // hideDialog();
//
//        try {
//            JSONObject jObj = new JSONObject(response);
//            boolean error = jObj.getBoolean("error");
//
//            if (!error) {
//                String user = jObj.getJSONObject("user").getString("name");
//                Toast.makeText(getApplicationContext(), "Hi " + user + ", You are successfully Added!", Toast.LENGTH_SHORT).show();
//
//                // Launch login activity
//                Intent intent = new Intent(
//                        RegisterActivity.this,
//                        LogActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//
//                String errorMsg = jObj.getString("error_msg");
//                Toast.makeText(getApplicationContext(),
//                        errorMsg, Toast.LENGTH_LONG).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
