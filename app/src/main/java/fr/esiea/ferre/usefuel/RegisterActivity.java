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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private  EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private TextView textViewSignup;
    private TextView textViewBeDeliver;

    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Allows Firebase database offline mode
        /*FirebaseDatabase.getInstance().setPersistenceEnabled(true);*/


        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() != null){
            //Profile Activity here
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        textViewBeDeliver = (TextView) findViewById(R.id.textViewBeDeliver);

        buttonRegister.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        textViewBeDeliver.setOnClickListener(this);
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        boolean validForm = true;

        if (TextUtils.isEmpty(username)) {
            //If username is empty
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }
        else if (TextUtils.isEmpty(email)) {
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

        else if (TextUtils.isEmpty(password2)) {
            //If password2 is empty
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }

        else if (!(password.equals(password2))) {
            //If mistake in password
            Toast.makeText(this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
            validForm = false;
            return;
        }


        //IF VALIDATIONS ARE OK
        if(validForm) {
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

                                // init to database user informations
                                firebaseUser = firebaseAuth.getCurrentUser();
                                String uID = firebaseUser.getUid();
                                User user = new User(username, firebaseUser.getEmail());
                                Car car1 = new Car("none", "none", "none", "none");
                                Car car2 = new Car("none", "none", "none", "none");
                                Car car3 = new Car("none", "none", "none", "none");
                                Car car4 = new Car("none", "none", "none", "none");

                                // OrderFuel orderFuel1 = new OrderFuel("none");

                                mDatabase.child("users").child(uID).setValue(user);
                                mDatabase.child("cars").child(uID).child("car1").setValue(car1);
                                mDatabase.child("cars").child(uID).child("car2").setValue(car2);
                                mDatabase.child("cars").child(uID).child("car3").setValue(car3);
                                mDatabase.child("cars").child(uID).child("car4").setValue(car4);
                                // mDatabase.child("order").child(uID).child("orderFuel1").setValue(orderFuel1);
                                // go on login page
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(RegisterActivity.this, "Could not register, password is too weak", Toast.LENGTH_SHORT).show();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(RegisterActivity.this, "Could not register, credentials are invalid", Toast.LENGTH_SHORT).show();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(RegisterActivity.this, "Could not register, email already registered", Toast.LENGTH_SHORT).show();
                                } catch(Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Could not register, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.hide();
                            }
                        }
                    });


        }
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
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if (view == textViewBeDeliver){
            startActivity(new Intent(this,WBADeliverActivity.class));


        }
    }
}