package cl.unab.mantentrees;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister, buttonSignIn;
    private EditText editTextPassword, editTextEmail;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);
        buttonSignIn = (Button) findViewById(R.id.signin_button);
        buttonRegister = (Button) findViewById(R.id.register_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.bringToFront();
        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    Log.i("SESION", "Sesion Iniciada con Email: " + user.getEmail());
                }else{
                    Log.i("SESION", "Sesion Cerrada.");
                }
            }
        };

    }

    private void registrar(){
        String emailReg = editTextEmail.getText().toString().trim();
        String passReg = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(emailReg)){
            Toast.makeText(this,"Ingresa un E-Mail.", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailReg).matches()){
            Toast.makeText(this,"Debes Ingresar un E-Mail Válido.", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(passReg)){
            Toast.makeText(this,"Ingresa una Contraseña.", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }
        if(passReg.length()<6){
            Toast.makeText(this,"La contraseña debe contener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailReg,passReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Usuario Registrado Correctamente.",Toast.LENGTH_LONG).show();
                    Log.i("SESION", "Usuario Creado Correctamente.");
                }else{
                    
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(MainActivity.this, "Este usuario ya esta registrado.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void iniciarSesion(){
        String emailInicio = editTextEmail.getText().toString().trim();
        String passInicio = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(emailInicio)){
            Toast.makeText(this,"Ingresa un E-Mail.", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInicio).matches()){
            Toast.makeText(this,"Debes Ingresar un E-Mail Válido.", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(passInicio)){
            Toast.makeText(this,"Ingresa una Contraseña.", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }
        if(passInicio.length()<6){
            Toast.makeText(this,"La contraseña es de 6 caracteres.", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailInicio,passInicio).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Sesion Iniciada Correctamente", Toast.LENGTH_SHORT).show();
                    abrirMenu();
                    Log.i("SESION", "Sesion Iniciada Correctamente");
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
                    Log.e("SESION", task.getException().getMessage()+"");
                }
            }
        });
    }

    public void abrirMenu(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        switch (v.getId()){
            case R.id.signin_button:
                iniciarSesion();
                break;
            case R.id.register_button:
                registrar();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
