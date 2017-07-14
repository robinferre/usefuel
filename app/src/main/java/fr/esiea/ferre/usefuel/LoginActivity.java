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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.esiea.ferre.usefuel.DeliveryActivities.MainDeliveryActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        firebaseAuth = FirebaseAuth.getInstance();

        // If already connected
        if (firebaseAuth.getCurrentUser() != null){
            //Profile Activity here
            finish();
            startActivity(new Intent(getApplicationContext(),LoadingScreenActivity.class));

        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSingIn);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        boolean validForm = true;

        if (TextUtils.isEmpty(email)) {
            //If email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
         }
        else if (!findMatch(email,"^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-z0-9.-]+$")) {
            //If email is empty
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            //If password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }

        //IF VALIDATIONS ARE OK
        if(validForm) {

            progressDialog.setMessage("Login...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {

                                //Start profile Activity

                                startActivity(new Intent(getApplicationContext(), LoadingScreenActivity.class));
                            }
                        }

                    });

        }
    }
    private void adminLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        boolean validForm = true;

        if (TextUtils.isEmpty(email)) {
            //If email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }
        else if (!findMatch(email,"^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-z0-9.-]+$")) {
            //If email is empty
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            //If password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }

        //IF VALIDATIONS ARE OK
        if(validForm) {

            progressDialog.setMessage("Login...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {

                                //Start profile Activity

                                startActivity(new Intent(getApplicationContext(), MainDeliveryActivity.class));
                            }
                        }

                    });

        }
    }


    // Calligraphy dependencies required that
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static Boolean findMatch(String myString, String pattern) {

        String match = "";

        // Pattern to find code
        Pattern regEx = Pattern.compile(pattern);

        // Find instance of pattern matches
        Matcher m = regEx.matcher(myString);
        if (m.find()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            if (editTextEmail.getText().toString().equals("raphael.sidoli@gmail.com")){
                adminLogin();
            }
            else {
                userLogin();
            }
        }
        if (view == textViewSignIn){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }
}