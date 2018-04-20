package com.example.poline.ntw_hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Subletor extends AppCompatActivity {
    Button button;
    EditText name;
    EditText email;
    EditText number;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subletor);

   Button button = (Button) findViewById(R.id.create);
   name = (EditText) findViewById(R.id.name);
   email = (EditText) findViewById(R.id.email);
   number = (EditText) findViewById(R.id.number);
   password = (EditText) findViewById(R.id.password);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(Subletor.this,Sublet.class));
        }
    });
    }
    private void createAccount(){
        String username = name.getText().toString().trim();
        String useremail = email.getText().toString().trim();
        String usernumber = number.getText().toString().trim();
        String userpassword = password.getText().toString().trim();

        if(!TextUtils.isEmpty(username) || !TextUtils.isEmpty(useremail) || !TextUtils.isEmpty(usernumber) || !TextUtils.isEmpty(userpassword)){

        }else{
            Toast.makeText(this, "Kindly fill in all details", Toast.LENGTH_LONG).show();
        }

    }
}
