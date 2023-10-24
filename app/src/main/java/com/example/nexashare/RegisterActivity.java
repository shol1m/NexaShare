package com.example.nexashare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nexashare.Helper.ValidationHelper;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,phone,password,confirmPassword;
    Button register;
    boolean isAllFieldsChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name_reg_edt);
        email = (EditText) findViewById(R.id.email_reg_edt);
        phone = (EditText) findViewById(R.id.phone_reg_edt);
        password = (EditText) findViewById(R.id.password_reg_edt);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_reg_edt);
        register = (Button)findViewById(R.id.register_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = checkAllFields();

                if (isAllFieldsChecked){
                    Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
                    startActivity(intent);
                }
            }
        });





    }

    private boolean checkAllFields(){
        ValidationHelper validationHelper = new ValidationHelper();
        boolean allowSave = true;

        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String phoneString = phone.getText().toString();
        String passwordString = password.getText().toString();
        String confirmPasswordString = confirmPassword.getText().toString();

        if (validationHelper.isNullOrEmpty(nameString)){
            name.setError("Name cannot be empty");
            allowSave = false;
        }

        if (validationHelper.isNullOrEmpty(emailString)){
            email.setError("Email cannot be empty");
            allowSave = false;
        }
        if (validationHelper.isNullOrEmpty(phoneString)){
            phone.setError("Phone number cannot be empty");
            allowSave = false;
        }
        if (validationHelper.isNullOrEmpty(passwordString)){
            password.setError("Password cannot be empty");
            allowSave = false;
        }

        if (validationHelper.isValidPhoneNumber(phoneString)){
            phone.setError("Enter a valid phone number");
            allowSave = false;
        }
        if (validationHelper.isValidEmail(emailString)){
            email.setError("Enter a valid email");
            allowSave = false;
        }
        if (validationHelper.isEqualPassword(passwordString,confirmPasswordString)){
            confirmPassword.setError("Passwords do not match");
            allowSave = false;
        }
        return allowSave;
    }
}