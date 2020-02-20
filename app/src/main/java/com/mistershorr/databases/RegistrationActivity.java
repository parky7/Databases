package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegistrationActivity extends AppCompatActivity {

    public static final String TAG = RegistrationActivity.class.getSimpleName();
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_PASSWORD = "password";

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show the back button

        wireWidgets();
        setListeners();

        // preload the username that they chose(get the username from the intent)
        String username = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
        editTextUsername.setText(username);

    }

    private void setListeners() {
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBackendlessAccount();
            }
        });
    }


    private void createBackendlessAccount() {
        // TODO update to make this work with startActivityForResult

        // create account on backendless
        // do not forget to call Backendless.initApp when your app initializes
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String checkPassword = editTextConfirmPassword.getText().toString();
        String name = editTextName.getText().toString();
        if((!username.isEmpty() && !password.isEmpty() && !email.isEmpty() &&
                !checkPassword.isEmpty() && !name.isEmpty() ) && password.equals(checkPassword)) {

            BackendlessUser user = new BackendlessUser();
            user.setProperty("username", username);
            user.setProperty("email", email);
            user.setProperty("password", password);
            user.setProperty("name", name);

            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                public void handleResponse(BackendlessUser registeredUser) {
                    // user has been registered and now can login
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();
                    Intent registrationCompleteIntent = new Intent();
                    registrationCompleteIntent.putExtra(EXTRA_USERNAME, username);
                    registrationCompleteIntent.putExtra(EXTRA_PASSWORD, password);
                    setResult(RESULT_OK, registrationCompleteIntent);
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }

                public void handleFault(BackendlessFault fault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Toast.makeText(RegistrationActivity.this, fault.getDetail(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // finish the activity

        //send back the username and password



    }

    private void wireWidgets() {
        editTextUsername = findViewById(R.id.edit_text_create_account_username);
        editTextPassword = findViewById(R.id.edit_text_create_account_password);
        editTextName = findViewById(R.id.edit_text_create_account_name);
        editTextEmail = findViewById(R.id.edit_text_create_account_email);
        editTextConfirmPassword = findViewById(R.id.edit_text_create_account_confirm_password);
        buttonCreateAccount = findViewById(R.id.button_create_account);
    }
}
