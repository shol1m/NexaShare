package com.example.nexashare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.nexashare.Helper.ValidationHelper;

public class LoginActivity extends AppCompatActivity {
    public Button login;
    public EditText phone,password;
    boolean isAllFieldsChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phone = (EditText) findViewById(R.id.phone_login_edt);
        password = (EditText) findViewById(R.id.password_login_edt);
        login = (Button) findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = checkAllFields();

                if (isAllFieldsChecked){
                    Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    private boolean checkAllFields(){
        ValidationHelper validationHelper = new ValidationHelper();
        boolean allowSave =true;

        String passwordString = password.getText().toString();
        String phoneString = phone.getText().toString();

        if (validationHelper.isNullOrEmpty(passwordString)){
            password.setError("Input password");
            allowSave = false;
        }
        if (validationHelper.isNullOrEmpty(passwordString)){
            password.setError("Input Password");
            allowSave = false;
        }
        if (validationHelper.isNullOrEmpty(phoneString)){
            phone.setError("Input Phone Number");
            allowSave = false;
        }

        if (validationHelper.isValidPhoneNumber(phoneString)){
            phone.setError("Enter a valid phone number");
        }
        return allowSave;
    }
}