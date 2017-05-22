package fr.esiea.ferre.usefuel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //Profile Activity here
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);

        buttonRegister.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //If email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //If password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            //If password2 is empty
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password.equals(password2))) {
            //If mistake in password
            Toast.makeText(this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
            return;
        }


        //IF VALIDATIONS ARE OK

        progressDialog.setMessage("Registering user...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User is successfuly registered
                            //Will open the profil activity
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                            // Deconnexion  to login again
                            firebaseAuth.signOut();

                            // Email verification
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            sendVerificationEmail();

                            // go on login page
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Could not register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            Toast.makeText(RegisterActivity.this, "Email sent, Verify your address before sign in", Toast.LENGTH_LONG).show();

                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(RegisterActivity.this, "Wrong email address, enter a correct one", Toast.LENGTH_LONG).show();

                            //restart this activity
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }





    // Calligraphy dependencies required that
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }

        if (view == textViewSignup){
            //Will open login activity
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}